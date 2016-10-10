package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.Calendar;
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
        initDBFromRemote();

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
            public void onNext(HttpResult<List<Event>> result) {
                List<Event> eventList = result.getData();
                DBManager db = DBManager.getInstance(getBaseContext());
                db.clearDB();
                for (Event event: eventList){
                    db.insertEvent(event);
                    if (event.hasAttendee()){
                        for (Invitee invitee:event.getInvitee()) {
                            db.insertInvitee(invitee);
                        }
                    }
                    if (event.hasTimeslots()){
                        for (TimeSlot timeSlot: event.getTimeslot()){
                            db.insertTimeSlot(timeSlot);
                        }
                    }
                    if (event.hasPhoto()){
                        for (PhotoUrl photoUrl : event.getPhoto()){
                            db.insertPhoto(photoUrl);
                        }
                    }
                }

                // successfully get event from server
                loadDB();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.INIT_DB));
                Log.i(TAG, "onNext: " + result.getData().size());
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
        DBManager.getInstance(getBaseContext()).getAllInvitee();
        long start = System.currentTimeMillis();
        EventManager.getInstance().getEventsMap().clear();
        List<Event> list = DBManager.getInstance(getBaseContext()).getAllEvents();
        int i = 0;
        for (Event ev: list) {
            ev.getTimeslot();
            ev.getPhoto();
            ev.getInvitee();
            EventManager.getInstance().addEvent(ev);
            i++;
            if(i > 100){
                break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
