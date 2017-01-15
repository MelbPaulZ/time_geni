package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.PhotoApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

import static android.R.attr.type;

/**
 * Created by Paul on 3/10/16.
 */

public class EventCommonPresenter<T extends EventCommonMvpView> extends MvpBasePresenter<T> {

    public static final int TASK_EVENT_UPDATE = 1;
    public static final int TASK_EVENT_INSERT = 2;
    public static final int TASK_EVENT_DELETE = 3;
    public static final int TASK_EVENT_GET = 4;
    public static final int TASK_EVENT_CONFIRM = 5;
    public static final int TASK_EVENT_ACCEPT = 6;
    public static final int TASK_EVENT_REJECT = 7;
    public static final int TASK_TIMESLOT_ACCEPT = 8;
    public static final int TASK_TIMESLOT_REJECT = 9;
    public static final int TASK_BACK = 10;


    public static final String UPDATE_THIS = "this";
    public static final String UPDATE_ALL = "all";
    public static final String UPDATE_FOLLOWING = "following";

    private String TAG = "EventCommonPresenter";

    public Context context;
    protected EventApi eventApi;
    protected PhotoApi photoApi;
    protected CalendarUtil calendarUtil;
    protected EventManager eventManager;



    public EventCommonPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
        photoApi = HttpUtil.createService(context, PhotoApi.class);

        calendarUtil = CalendarUtil.getInstance(context);
        eventManager = EventManager.getInstance(context);
    }

    public Context getContext() {
        return context;
    }

    /**
     *
     * @param event an event set the start time and endtime
     * @param type in {this, following, all}
     */
    public void updateEvent(Event event, String type, long originalStartTime){
        if(getView() != null){
            getView().onTaskStart(TASK_EVENT_UPDATE);
        }
        // orgCalendarUid to get the previous org event in server link
        String orgCalendarUid = EventManager.getInstance(context).getCurrentEvent().getCalendarUid();
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.update(
                orgCalendarUid,
                event.getEventUid(),
                event,
                type ,
                originalStartTime,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_EVENT_UPDATE, e.getMessage(), -1);
                }
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                if(eventHttpResult.getStatus() != 1){
                    throw new RuntimeException(eventHttpResult.getInfo());
                }
                synchronizeLocal(eventHttpResult.getData());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                if(getView() != null){
                    getView().onTaskComplete(TASK_EVENT_UPDATE, eventHttpResult.getData());
                }
                Log.i(TAG, "onNext: " +"done");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    /**
     * synchronize the event with local database
     * @param events
     */
    public void synchronizeLocal(List<Event> events){
        for (Event ev: events){
            synchronizeLocal(ev);
        }
        Log.i(TAG, "APPP: synchronizeLocal: "+"call");
    }

    private void synchronizeLocal(Event ev){
        Event oldEvent = eventManager.findEventByUid(ev.getEventUid());
        if (oldEvent!=null) {
            eventManager.updateEvent(oldEvent, ev);
        }else{
            eventManager.addEvent(ev);
            DBManager.getInstance(context).insertEvent(ev);
        }
    }


    /**
     * call the api to insert a event to server
     * after this api is called, it will automatically sync with local db
     * @param event
     */
    public void insertEvent(Event event){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_INSERT);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.insert(event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_EVENT_INSERT, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                if(eventHttpResult.getStatus() != 1){
                    throw new RuntimeException(eventHttpResult.getInfo());
                }
                Event ev = eventHttpResult.getData().get(0);
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());

                // if the event is successfully insert into server, then begin to upload photos
                uploadImage(ev);
                insertEventLocal(ev);
                if(getView() != null){
                    getView().onTaskComplete(TASK_EVENT_INSERT, eventHttpResult.getData());
                }
                // todo: put event bus into fragment
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }


    /** call the api to accept timeSlots in a event,
     *  after this api called, it will automatically sync with db
     *  @param event
     * */
    public void acceptTimeslots(Event event){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }

        ArrayList<String> timeslotUids = new ArrayList<>();
        for (Timeslot timeslot: event.getTimeslot()){
            if (timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED)){
                timeslotUids.add(timeslot.getTimeslotUid());
            }
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("timeslots", timeslotUids);
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptTimeslot(event.getCalendarUid(), event.getEventUid(), parameters, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_ACCEPT, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                if (getView()!=null){
                    getView().onTaskComplete(TASK_TIMESLOT_ACCEPT, eventHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void acceptEvent(String calendarUid, String eventUid, String type, long orgStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }

        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptEvent(
                calendarUid,
                eventUid,
                type,
                orgStartTime,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());

                AppUtil.saveEventSyncToken(context, listHttpResult.getSyncToken());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                if (getView()!=null){
                    getView().onTaskComplete(TASK_EVENT_ACCEPT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void rejectTimeslots(final Event event){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_REJECT);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.rejectTimeslot(event.getCalendarUid() ,event.getEventUid(),syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_REJECT, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());

                // after synchronizeLocal, remove this event from EventManager
                for (Event ev: listHttpResult.getData()){
                    if (ev.getShowLevel()!=1){
//                        EventManager.getInstance(context).deleteEvent(event);
                    }
                }


                AppUtil.saveEventSyncToken(context, listHttpResult.getSyncToken());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                if (getView()!=null){
                    getView().onTaskComplete(TASK_TIMESLOT_REJECT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /** call the api to confirm event to server,
     *  after this api called, it will automatically sync db
     *
     * @param calendarUid
     * @param eventUid
     * @param timeslotUid
     */
    public void confirmEvent(String calendarUid, String eventUid, String timeslotUid){
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.confirm(calendarUid,eventUid, timeslotUid, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_CONFIRM, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                if (getView()!=null){
                    getView().onTaskComplete(TASK_EVENT_CONFIRM, eventHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    /** call the api to delete event from server,
     *  after delete event, should sync local db
     *  todo sync db
     *  @param event
     * */
    public void deleteEvent(Event event, String type, long orgStartTime) {
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_DELETE);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.delete(
                event.getCalendarUid(),
                event.getEventUid(),
                type,
                orgStartTime,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_DELETE, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                AppUtil.saveEventSyncToken(context, listHttpResult.getSyncToken());
                synchronizeLocal(listHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskComplete(TASK_EVENT_DELETE, listHttpResult.getData());
                }

            }

//            @Override
//            public void onNext(HttpResult<Event> eventHttpResult) {
//                Log.i(TAG, "onNext: " + eventHttpResult.getData().getSummary());
//                // todo delete event
//
//            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }



    public void insertEventLocal(Event event){
        EventManager.getInstance(context).addEvent(event);
        DBManager.getInstance(context).insertEvent(event);
    }

    /**
     * upload the photo file to a file server,
     * currently it will be uploaded to our server
     * @param event
     */
    public void uploadImage(final Event event) {
        if (event.hasPhoto()){
            for (final PhotoUrl photoUrl : event.getPhoto()){
                // create file
                String fileName = event.getEventUid() + "_" + photoUrl.getPhotoUid() + ".png";

                try {
                    final AVFile file = AVFile.withAbsoluteLocalPath(fileName,photoUrl.getLocalPath());
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e==null){
                                updatePhotoToServer(event, photoUrl.getPhotoUid(), file.getUrl());
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * update the photo url to server
     * @param event
     * @param url
     */
    private void updatePhotoToServer(Event event, String photoUid, String url){
        Observable<HttpResult<Event>> observable = photoApi.updatePhoto(event.getCalendarUid(), event.getEventUid(), photoUid, url);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Log.i(TAG, "onNext: ");
                synchronizeLocal(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void quitEvent(Event event, String type, long originalStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_REJECT);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.quitEvent(
                event.getCalendarUid(),
                event.getEventUid(),
                type,
                originalStartTime,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_REJECT, e.getMessage(), -1);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskComplete(TASK_EVENT_REJECT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }



}
