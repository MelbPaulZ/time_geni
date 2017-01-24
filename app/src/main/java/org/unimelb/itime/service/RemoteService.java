package org.unimelb.itime.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.User;
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
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

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

    private Context context;
    private User user;

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

        context = getApplicationContext();
        user = UserUtil.getInstance(context).getUser();
        Log.i(TAG, "onCreate: ");
        loadLocalEvents();


        //create the polling thread
        pollingThread = new PollingThread();
        pollingThread.start();

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: " + "destryo");
        if (messageHandler != null){
            messageHandler.cancel(true);
        }
        isStart = false;
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGOUT));
        super.onDestroy();
    }

    private void loadLocalEvents(){
        new Thread(){
            @Override
            public void run() {
                EventManager.getInstance(context).refreshEventManager();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        }.start();
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

                TokenUtil.getInstance(context).setCalendarToken(user.getUserUid(),httpResult.getSyncToken());
                DBManager.getInstance(context).insertOrReplace(httpResult.getData());
            }
        };
        String token = TokenUtil.getInstance(context).getCalendarToken(user.getUserUid());
        HttpUtil.subscribe(calendarApi.list(token), subscriber);
    }

    public void fetchMessages(){
        String token = TokenUtil.getInstance(context).getMessageToken(user.getUserUid());
        Observable<HttpResult<List<Message>>> observable = msgApi.get(token);
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

    public void fetchEvents(final Calendar calendar) {
        final int visibility = calendar.getVisibility();

        String synToken = TokenUtil.getInstance(context).getEventToken(user.getUserUid(),calendar.getCalendarUid());
        Observable<HttpResult<List<Event>>> observable = eventApi.list(calendar.getCalendarUid(), synToken)
                .map(new Func1<HttpResult<List<Event>>, HttpResult<List<Event>>>() {
            @Override
            public HttpResult<List<Event>> call(HttpResult<List<Event>> listHttpResult) {
                if(listHttpResult.getData().size() > 0){
                    isUpdateThreadRuning = true;

                    //if calendar not shown
                    if (visibility == 0){
                        DBManager.getInstance(context).insertOrReplace(listHttpResult.getData());
                    }else{
                        EventManager.getInstance(context).updateDB(listHttpResult.getData());
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
                //update syncToken
                TokenUtil.getInstance(context).setEventToken(user.getUserUid(),calendar.getCalendarUid(),result.getSyncToken());
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
                fetchCalendar();
                for(Calendar calendar : CalendarUtil.getInstance(getApplication()).getCalendar()){
                    if (calendar.getDeleteLevel() == 0){
                        fetchEvents(calendar);
                    }
                }
                fetchMessages();
                fetchContact();

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
                SharedPreferences sp = AppUtil.getTokenSaver(context);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, token);
                editor.apply();
                EventBus.getDefault().post(new MessageInboxMessage(messages));
            }else{
                Toast.makeText(context, "Message cannot find correspond event, dropped." ,Toast.LENGTH_LONG).show();
            }

            valid =false;
        }
    }
}
