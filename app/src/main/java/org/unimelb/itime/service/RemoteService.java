package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class RemoteService extends Service{
    private final static String TAG = "RemoteService";
    private EventApi eventApi;
    private CalendarApi calendarApi;
    private ContactApi contactApi;

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
        calendarApi = HttpUtil.createService(getBaseContext(), CalendarApi.class);
        contactApi = HttpUtil.createService(getBaseContext(), ContactApi.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initDBFromRemote();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initDBFromRemote(){
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
                fetchEvents();
            }
        };
        HttpUtil.subscribe(calendarApi.list(), subscriber);
    }

    public void fetchEvents() {
        // here to list events
        String calendarUid = CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid();
        Observable<HttpResult<List<Event>>> observable = eventApi.list(calendarUid);
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
                final DBManager db = DBManager.getInstance(getBaseContext());

                new Thread(){
                    @Override
                    public void run() {
                        for (Event event: eventList){
                            db.insertEvent(event);
                        }
                        // successfully get event from server
                        loadDB();
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                        Log.i(TAG, "onNext: " + result.getData().size());
                    }
                }.start();


//                for (Event event: eventList){
//                    db.insertEvent(event);
//                }
//
//                // successfully get event from server
//                loadDB();
//
//                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
//                Log.i(TAG, "onNext: " + result.getData().size());
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

    private void loadDB(){
        EventManager.getInstance().getEventsMap().clear();
        List<Event> list = DBManager.getInstance(getBaseContext()).getAllEvents();
        for (Event ev: list) {
            EventManager.getInstance().addEvent(ev);
        }
    }

}
