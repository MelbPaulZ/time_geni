package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onStartCommand: " + "start remoteservice");
        eventApi = HttpUtil.createService(getBaseContext(), EventApi.class);
        msgApi = HttpUtil.createService(getBaseContext(), MessageApi.class);
        calendarApi = HttpUtil.createService(getBaseContext(), CalendarApi.class);
        contactApi = HttpUtil.createService(getBaseContext(), ContactApi.class);

        //create the polling thread
        pollingThread = new PollingThread();

        pullDataFromRemote();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isStart = false;
        pollingThread.interrupt();
        EventManager.getInstance().clearManager();
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

                //start to load local db than start polling thread
                new Thread(){
                    @Override
                    public void run() {
                        //load local DB to manager
                        EventManager.getInstance().loadDB(getApplicationContext());
                        //start to polling
                        pollingThread.start();
                    }
                }.start();
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

                DBManager.getInstance().deleteAllMessages();
                DBManager.getInstance().insertMessageList(listHttpResult.getData());

                //set data to inbox;
                EventBus.getDefault().post(new MessageInboxMessage(listHttpResult.getData()));
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents() {
        // here to list events
        String calendarUid = CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid();
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

                new Thread(){
                    @Override
                    public void run() {
                        // successfully get event from server
                        EventManager.getInstance().updateDB(eventList);
                        fetchMessages(); // after insert event in db, then fetch events, and will never cannot find eventUid
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                        Log.i(TAG, "onNext: " + result.getData().size());
                    }
                }.start();
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
                    fetchEvents();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // do something
            }

        }

    }
}
