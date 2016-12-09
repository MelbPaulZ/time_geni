package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.google.common.collect.ComparisonChain.start;

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
        isStart = false;
        while (isPollingThreadRunning){
            if (!isPollingThreadRunning){
                break;
            }
            Log.i(TAG, "onDestroy: " + "polling thread running");
            SystemClock.sleep(50);
        }
        pollingThread.interrupt();

        while (isUpdateThreadRuning){
            Log.i(TAG, "onDestroy: " + "isUpdateThread running");
            SystemClock.sleep(50);
        }
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
                CalendarUtil.getInstance().setCalendar(httpResult.getData());
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
                Log.i(TAG, "listHttpResult: " + listHttpResult);
                if (checkMessageValidation(listHttpResult.getData())){
                    //update syncToken
                    SharedPreferences sp = AppUtil.getSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, listHttpResult.getSyncToken());
                    editor.apply();

                    DBManager.getInstance(getBaseContext()).deleteAllMessages();
                    Collections.sort(listHttpResult.getData()); // sort data depends on edit time
                    Collections.reverse(listHttpResult.getData()); // from the new time to old time
                    DBManager.getInstance(getBaseContext()).insertMessageList(listHttpResult.getData());

                    //set data to inbox;
                    EventBus.getDefault().post(new MessageInboxMessage(listHttpResult.getData()));
                }else{
                    Toast.makeText(getApplicationContext(), "Message cannot find correspond event, dropped." ,Toast.LENGTH_LONG).show();
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents(String calendarUid) {
        String synToken = AppUtil.getSharedPreferences(getApplicationContext()).getString(C.spkey.EVENT_LIST_SYNC_TOKEN,"");
        Log.i(TAG, "fetchEvents: " + synToken);
        Observable<HttpResult<List<Event>>> observable = eventApi.list(
                calendarUid
                , synToken);
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
                final List<Event> eventList = result.getData();
                Log.i(TAG, "__onNext: " + result.getData().size());
                //update syncToken
                SharedPreferences sp = AppUtil.getSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, result.getSyncToken());
                editor.apply();
                // successfully get event from server
                if(eventList.size() > 0){
                    updateThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Log.i(TAG, "run: " + "create new updateThread");
                            isUpdateThreadRuning = true;
                            Log.i(TAG, "run: " + "updateThread start runs");
                            EventManager.getInstance().updateDB(eventList);
                            EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                            Log.i(TAG, "run: " + "updateThread stop runs");
                            Log.i(TAG, "onNext: " + result.getData().size());
                            isUpdateThreadRuning = false;
                        }
                    };
                    updateThread.start();
                }
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
                List<Contact> contactList = listHttpResult.getData();
                for (Contact contact : contactList){
                    DBManager.getInstance(getBaseContext()).insertContact(contact);
                }
                Log.i(TAG, "onNext: ");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private boolean checkMessageValidation(List<Message> msgs){
        for (Message msg:msgs
             ) {
            Event correspond = DBManager.getInstance().getEvent(msg.getEventUid());

            if (correspond == null){
                Log.i("Error_msg", "checkMessageValidation: " + msg.getEventUid());
                return false;
            }
        }

        return true;
    }

    private class PollingThread extends Thread {
        @Override
        public void run() {
            isPollingThreadRunning = true;
            while (isStart) {
                // todo: here to list events
                for(Calendar calendar : CalendarUtil.getInstance().getCalendar()){
                    fetchEvents(calendar.getCalendarUid());
                }
                fetchMessages();

                SystemClock.sleep(5000); // cannot use thread sleep because of interrupt exception when sleep
                // do something
            }
            isPollingThreadRunning = false;
        }

    }
}
