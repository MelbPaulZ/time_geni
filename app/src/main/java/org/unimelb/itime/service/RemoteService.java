package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class RemoteService extends Service{
    private final static String TAG = "RemoteService";
    private EventApi eventApi;
    private MessageApi msgApi;
    private CalendarApi calendarApi;
    private ContactApi contactApi;
    private Boolean isStart = true;
    private PollingThread pollingThread;
    private Thread updateThread;
    private boolean isUpdateThreadRuning, isPollingThreadRunning = false;
    private MessageHandler messageHandler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        eventApi = HttpUtil.createService(getBaseContext(), EventApi.class);
        msgApi = HttpUtil.createService(getBaseContext(), MessageApi.class);
        calendarApi = HttpUtil.createService(getBaseContext(), CalendarApi.class);
        contactApi = HttpUtil.createService(getBaseContext(), ContactApi.class);

        //create the polling thread
        pollingThread = new PollingThread();
        Log.i(TAG, "onCreate: " + "service onCreate");
        pullDataFromRemote();

    }

    @Override
    public void onDestroy() {
        if (messageHandler != null){
            messageHandler.cancel(true);
        }
        isStart = false;
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGOUT));
        super.onDestroy();
    }

    private void pullDataFromRemote(){
        fetchCalendar();
        fetchContact();
    }

    private void fetchCalendar() {
        // here to list calendar;
        Subscriber<HttpResult<List<org.unimelb.itime.bean.Calendar>>> subscriber = new Subscriber<HttpResult<List<org.unimelb.itime.bean.Calendar>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "calendarApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "calendarApi");
            }

            @Override
            public void onNext(HttpResult<List<org.unimelb.itime.bean.Calendar>> httpResult) {
                if (!isStart){
                    return;
                }
                CalendarUtil.getInstance(getApplicationContext()).setCalendar(httpResult.getData());

                SharedPreferences sp = AppUtil.getSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();

                //todo: save it into db
                Gson gson = new Gson();
                String calendarListString = gson.toJson(httpResult.getData());
                editor.putString(C.calendarString.CALENDAR_STRING,  calendarListString);
                editor.apply();

                //update db
                DBManager.getInstance(getApplicationContext()).clearCalendars();
                DBManager.getInstance(getApplicationContext()).insertOrReplace(httpResult.getData());

                pollingThread.start();
            }
        };
        HttpUtil.subscribe(calendarApi.list(), subscriber);
    }

    public void fetchMessages(){
        Observable<HttpResult<List<Message>>> observable = msgApi.get();
        Subscriber<HttpResult<List<Message>>> subscriber = new Subscriber<HttpResult<List<Message>>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "messageApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "messageApi" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Message>> listHttpResult) {
                if (!isStart){
                    return;
                }

                if (messageHandler == null){
                    messageHandler = new MessageHandler();
                    messageHandler.execute(listHttpResult);
                }else if(messageHandler.getStatus() != AsyncTask.Status.RUNNING){
                    messageHandler = new MessageHandler();
                    messageHandler.execute(listHttpResult);
                }

            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents(Calendar calendar) {
        final int visibility = calendar.getVisibility();

        String synToken = AppUtil.getEventSyncToken(getApplicationContext());
        Observable<HttpResult<List<Event>>> observable = eventApi.list(calendar.getCalendarUid(), synToken)
                .map(new Func1<HttpResult<List<Event>>, HttpResult<List<Event>>>() {
            @Override
            public HttpResult<List<Event>> call(HttpResult<List<Event>> listHttpResult) {
                if(listHttpResult.getData().size() > 0){
                    isUpdateThreadRuning = true;

                    //if calendar not shown
                    if (visibility == 0){
                        DBManager.getInstance(getApplicationContext()).insertOrReplace(listHttpResult.getData());
                    }else{
                        EventManager.getInstance(getApplicationContext()).updateDB(listHttpResult.getData());
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                        isUpdateThreadRuning = false;
                    }
                }

                return listHttpResult;
            }
        });
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "eventApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "eventApi" + e.getMessage());
            }

            @Override
            public void onNext(final HttpResult<List<Event>> result) {
                if (!isStart){
                    return ;
                }
                final List<Event> eventList = result.getData();
                Log.i(TAG, "__onNext: " + result.getData().size());
                //update syncToken
                AppUtil.saveEventSyncToken(getApplicationContext(), result.getSyncToken());
                // successfully get event from server
//                if(eventList.size() > 0){
//                    updateThread = new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
//                            isUpdateThreadRuning = true;
//
//                            //if calendar not shown
//                            if (visibility == 1){
//                                DBManager.getInstance(getApplicationContext()).
//                            }else{
//                                EventManager.getInstance(getApplicationContext()).updateDB(eventList);
//                                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                                isUpdateThreadRuning = false;
//                            }
//                        }
//                    };
//                    updateThread.start();
//                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void fetchContact(){
        Observable<HttpResult<List<Contact>>> observable = contactApi.list();
        Subscriber<HttpResult<List<Contact>>> subscriber = new Subscriber<HttpResult<List<Contact>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Contact>> listHttpResult) {
                if (!isStart){
                    return ;
                }
                List<Contact> contactList = listHttpResult.getData();
                for (Contact contact : contactList){
                    DBManager.getInstance(getBaseContext()).insertContact(contact);
                }
                Log.i(TAG, "onNext: ");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private class PollingThread extends Thread {
        @Override
        public void run() {
            isPollingThreadRunning = true;
            while (isStart) {
                // todo: here to list events
                for(Calendar calendar : CalendarUtil.getInstance(getApplication()).getCalendar()){
                    if (calendar.getDeleteLevel() == 0){
                        fetchEvents(calendar);
                    }
                }
                fetchMessages();

                SystemClock.sleep(5000); // cannot use thread sleep because of interrupt exception when sleep
                // do something
            }
            isPollingThreadRunning = false;
        }

    }

    private class MessageHandler extends AsyncTask< HttpResult<List<Message>> , Integer, List<Message>>{
        boolean valid = false;
        String token = "";
        @Override
        protected List<Message> doInBackground(HttpResult<List<Message>>... params) {
            if (!isStart){
                return null;
            }
            HttpResult<List<Message>> listHttpResult = params[0];
            List<Message> msgs = listHttpResult.getData();

//            if (checkMessageValidation(msgs)){
                token = listHttpResult.getSyncToken();
                DBManager.getInstance(getBaseContext()).deleteAllMessages();
                Collections.sort(msgs); // sort data depends on edit time
                Collections.reverse(msgs); // from the new time to old time
                DBManager.getInstance(getBaseContext()).insertMessageList(msgs);
                valid = true;
                //set data to inbox;
//            }

            return msgs;
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            super.onPostExecute(messages);
            if (!isStart){
                return ;
            }
            if (valid){
                //update syncToken
                SharedPreferences sp = AppUtil.getTokenSaver(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, token);
                editor.apply();
                EventBus.getDefault().post(new MessageInboxMessage(messages));
            }else{
                Toast.makeText(getApplicationContext(), "Message cannot find correspond event, dropped." ,Toast.LENGTH_LONG).show();
            }

            valid =false;
        }
    }
}
