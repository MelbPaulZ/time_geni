package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
        pollingThread.interrupt();
        Log.i(TAG, "onDestroy: " + "is destroyed");
        updateThread.interrupt();
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
                //update syncToken
                SharedPreferences sp = AppUtil.getSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, listHttpResult.getSyncToken());
                editor.apply();

                DBManager.getInstance(getBaseContext()).deleteAllMessages();
                DBManager.getInstance(getBaseContext()).insertMessageList(listHttpResult.getData());

                //set data to inbox;
                EventBus.getDefault().post(new MessageInboxMessage(listHttpResult.getData()));
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents(String calendarUid) {
        String synToken = AppUtil.getSharedPreferences(getApplicationContext()).getString(C.spkey.EVENT_LIST_SYNC_TOKEN,"");

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
                Log.i(TAG, "onError: " + "eventApi");
            }

            @Override
            public void onNext(final HttpResult<List<Event>> result) {
                final List<Event> eventList = result.getData();
                //update syncToken
                SharedPreferences sp = AppUtil.getSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, result.getSyncToken());
                editor.apply();


                // successfully get event from server
                updateThread = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        EventManager.getInstance().updateDB(eventList);
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                        Log.i(TAG, "onNext: " + result.getData().size());
                    }
                };
                updateThread.start();

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


    private class PollingThread extends Thread {
        @Override
        public void run() {

            while (isStart) {

                try {

                    // todo: here to list events
                    for(Calendar calendar : CalendarUtil.getInstance().getCalendar()){
                        fetchEvents(calendar.getCalendarUid());
                        Thread.sleep(5000);
                    }
                    fetchMessages();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // do something
            }

        }

    }
}
