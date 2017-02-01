package org.unimelb.itime.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.ContactApi;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.FriendRequestApi;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class RemoteService extends Service {
    private final static String TAG = "RemoteService";
    private EventApi eventApi;
    private MessageApi msgApi;
    private CalendarApi calendarApi;
    private ContactApi contactApi;
    private FriendRequestApi requestApi;
    private Boolean isStart = true;
    private PollingThread pollingThread;

    private Context context;
    private User user;

    private DBManager dbManager;
    private EventManager eventManager;
    private TokenUtil tokenUtil;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        dbManager = DBManager.getInstance(context);
        eventManager = EventManager.getInstance(context);
        tokenUtil = TokenUtil.getInstance(context);

        eventApi = HttpUtil.createService(context, EventApi.class);
        msgApi = HttpUtil.createService(context, MessageApi.class);
        calendarApi = HttpUtil.createService(context, CalendarApi.class);
        contactApi = HttpUtil.createService(context, ContactApi.class);
        requestApi = HttpUtil.createService(context, FriendRequestApi.class);

        user = UserUtil.getInstance(context).getUser();
        Log.i(TAG, "onCreate: ");
        loadLocalEvents();
        loadRegions();
        //create the polling thread
        pollingThread = new PollingThread();
        pollingThread.start();

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: " + "destroy");
        isStart = false;
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGOUT));
        super.onDestroy();
    }

    private void loadLocalEvents() {
        new Thread() {
            @Override
            public void run() {
                eventManager.refreshEventManager();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        }.start();
    }

    /**
     * if haven't load regions, then init regions
     */
    private void loadRegions(){
        new Thread(){
            @Override
            public void run() {
                if (DBManager.getInstance(context).getCountryList().size() == 0) {
                    DBManager.getInstance(context).initRegion();
                }
            }
        }.start();
    }


    private void fetchCalendar() {
        // here to list calendar;
        String token = tokenUtil.getCalendarToken(user.getUserUid());
        Observable<HttpResult<List<Calendar>>> observable = calendarApi.list(token)
                .map(new Func1<HttpResult<List<Calendar>>, HttpResult<List<Calendar>>>() {
                    @Override
                    public HttpResult<List<Calendar>> call(HttpResult<List<Calendar>> ret) {
                        if (isStart && ret.getStatus() == 1) {
                            tokenUtil.setCalendarToken(user.getUserUid(), ret.getSyncToken());
                            dbManager.insertOrReplace(ret.getData());
                        }
                        return ret;
                    }
                });
        Subscriber<HttpResult<List<Calendar>>> subscriber = new Subscriber<HttpResult<List<Calendar>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "calendarApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "calendarApi");
            }

            @Override
            public void onNext(HttpResult<List<Calendar>> httpResult) {
                if (!isStart) {
                    return;
                }
                //todo need to notify ui changes
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchMessages() {
        final String token = tokenUtil.getMessageToken(user.getUserUid());
        Observable<HttpResult<List<Message>>> observable = msgApi.get(token)
                .map(new Func1<HttpResult<List<Message>>, HttpResult<List<Message>>>() {
                    @Override
                    public HttpResult<List<Message>> call(HttpResult<List<Message>> ret) {
                        if (isStart && ret.getStatus() == 1) {
                            List<Message> msgs = ret.getData();
                            dbManager.insertMessageList(msgs);
                            tokenUtil.setMessageToken(user.getUserUid(), ret.getSyncToken());
                        }
                        return ret;
                    }
                });
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
            public void onNext(HttpResult<List<Message>> ret) {
                if (!isStart) {
                    return;
                }
                if (ret.getStatus() == 1 && ret.getData().size() > 0) {
                    EventBus.getDefault().post(new MessageInboxMessage(ret.getData()));
                }
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }

    public void fetchEvents(final Calendar calendar) {
        final int visibility = calendar.getVisibility();

        String synToken = TokenUtil.getInstance(context).getEventToken(user.getUserUid(), calendar.getCalendarUid());
        Observable<HttpResult<List<Event>>> observable = eventApi.list(calendar.getCalendarUid(), synToken)
                .map(new Func1<HttpResult<List<Event>>, HttpResult<List<Event>>>() {
                    @Override
                    public HttpResult<List<Event>> call(HttpResult<List<Event>> ret) {
                        if (ret.getData().size() > 0) {
                            //if calendar not shown
                            if (visibility == 0) {
                                dbManager.insertOrReplace(ret.getData());
                            } else {
                                eventManager.updateDB(ret.getData());
                            }
                            //update syncToken
                            tokenUtil.setEventToken(user.getUserUid(), calendar.getCalendarUid(), ret.getSyncToken());
                        }

                        return ret;
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
                if (!isStart) {
                    return;
                }
                if(result.getStatus() == 1 && result.getStatus() > 0){
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void fetchContact() {
        Observable<HttpResult<List<Contact>>> observable = contactApi.list().map(new Func1<HttpResult<List<Contact>>, HttpResult<List<Contact>>>() {
            @Override
            public HttpResult<List<Contact>> call(HttpResult<List<Contact>> ret) {
                if(ret.getStatus() == 1 && ret.getData().size() > 0){
                    List<Contact> contactList = ret.getData();
                    for (Contact contact : contactList) {
                        dbManager.insertContact(contact);
                    }
                }
                return ret;
            }
        });
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
            public void onNext(HttpResult<List<Contact>> ret) {
                if (!isStart) {
                    return;
                }
                Log.i(TAG, "onNext: ");
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    // Added by Qiushuo Huang
    private void fetchFriendRequest(){
        Observable<HttpResult<FriendRequestResult>> observable = requestApi.list().map(new Func1<HttpResult<FriendRequestResult>, HttpResult<FriendRequestResult>>() {
            @Override
            public HttpResult<FriendRequestResult> call(HttpResult<FriendRequestResult> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    return null;
                }else {
                    for(FriendRequest request:result.getData().getSend()) {
                        dbManager.insertFriendRequest(request);
                    }
                    for(FriendRequest request:result.getData().getReceive()) {
                        dbManager.insertFriendRequest(request);
                    }
                    return result;
                }
            }
        });

        Subscriber<HttpResult<FriendRequestResult>> subscriber = new Subscriber<HttpResult<FriendRequestResult>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<FriendRequestResult> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){

                }else {
                    int count = 0;
                    for(FriendRequest request:result.getData().getSend()) {
                        if(!request.isRead()){
                            count++;
                        }
                    }
                    for(FriendRequest request:result.getData().getReceive()) {
                        if(!request.isRead()){
                            count++;
                        }
                    }
                    EventBus.getDefault().post(new MessageNewFriendRequest(count));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    /**
     * for polling
     */
    private class PollingThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                fetchCalendar();
                for (Calendar calendar : CalendarUtil.getInstance(getApplication()).getCalendar()) {
                    if (calendar.getDeleteLevel() == 0) {
                        fetchEvents(calendar);
                    }
                }
                fetchMessages();
                fetchContact();
                fetchFriendRequest();

                SystemClock.sleep(5000); // cannot use thread sleep because of interrupt exception when sleep
                // do something
            }
        }
    }

}
