package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.databinding.tool.util.L;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.PhotoApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_ACCEPT;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_CONFIRM;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_DELETE;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_INSERT;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_REJECT;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_TIMESLOT_ACCEPT;
import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_TIMESLOT_REJECT;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {

    private Context context;
    private EventApi eventApi;
    private PhotoApi photoApi;
    private final static String TAG = "EventPresenter";

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

    public static final int TASK_UPLOAD_IMAGE= 11;
    public static final int TASK_UPDATE_IMAGE= 12;
    public static final int TASK_DELETE_IMAGE= 13;
    public static final int TASK_SYN_IMAGE= 14;


    public static final String UPDATE_THIS = "this";
    public static final String UPDATE_ALL = "all";
    public static final String UPDATE_FOLLOWING = "following";

    public EventPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
        photoApi = HttpUtil.createService(context, PhotoApi.class);
    }

    public Context getContext(){
        return this.context;
    }

    public void updateEvent(Event event, String type, long originalStartTime){
        if(getView() != null){
            getView().onTaskStart(TASK_EVENT_UPDATE);
        }
        setEventToEventManager(event);
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
                    getView().onTaskError(TASK_EVENT_UPDATE);
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
                    getView().onTaskSuccess(TASK_EVENT_UPDATE, eventHttpResult.getData());
                }
                Log.i(TAG, "onNext: " +"done");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    public void fetchEvent(String calendarUid, String eventUid){
        Observable<HttpResult<Event>> observable = eventApi.get(
                calendarUid
                , eventUid);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {

            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: " + "eventApi");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + "eventApi" + e.getMessage());
                if (getView() != null){
                    getView().onTaskError(TASK_EVENT_GET);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                EventManager.getInstance(context).updateDB(Arrays.asList(eventHttpResult.getData()));
                if (getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_GET,Arrays.asList(eventHttpResult.getData()));
                }
            }

        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void insertEvent(Event event){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_INSERT);
        }
        setEventToEventManager(event);
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.insert(event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_EVENT_INSERT);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                if(eventHttpResult.getStatus() != 1){
                    throw new RuntimeException(eventHttpResult.getInfo());
                }
                Event ev = eventHttpResult.getData().get(0);
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());


                insertEventLocal(ev);

                // if the event is successfully insert into server, then begin to upload photos
                if (ev.hasPhoto()){
                    uploadImage(ev);
                }else {
                    if(getView() != null){
                        getView().onTaskSuccess(TASK_EVENT_INSERT, eventHttpResult.getData());
                    }
                    // todo: put event bus into fragment
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                }
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

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
                    getView().onTaskError(TASK_EVENT_DELETE);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                AppUtil.saveEventSyncToken(context, listHttpResult.getSyncToken());
                synchronizeLocal(listHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_DELETE, listHttpResult.getData());
                }

            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /**
     * upload the photo file to a file server,
     * currently it will be uploaded to our server
     * @param event
     */
    public void uploadImage(final Event event) {
        if (event.hasPhoto()){
            if (getView() != null){
                getView().onTaskStart(TASK_UPLOAD_IMAGE);
            }

            final List<ImageUploadWrapper> wrappers = new ArrayList<>();

            for (final PhotoUrl photoUrl : event.getPhoto()){
                final ImageUploadWrapper wrapper = new ImageUploadWrapper(photoUrl,false);
                wrappers.add(wrapper);

                // create file
                String fileName = event.getEventUid() + "_" + photoUrl.getPhotoUid() + ".png";

                try {
                    final AVFile file = AVFile.withAbsoluteLocalPath(fileName,photoUrl.getLocalPath());
                    file.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e==null){
                                wrapper.getPhoto().setUrl(file.getUrl());
                                wrapper.setUploaded(true);

                                if (imageUploadChecker(wrappers) && getView() != null){
                                    getView().onTaskSuccess(TASK_UPLOAD_IMAGE, Arrays.asList(event));
                                }
                            }else{
                                if (getView() != null){
                                    getView().onTaskError(TASK_UPLOAD_IMAGE);
                                }
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
    public void updatePhotoToServer(final Event event, String photoUid, String url){
        Observable<HttpResult<Event>> observable = photoApi.updatePhoto(event.getCalendarUid(), event.getEventUid(), photoUid, url);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if (getView() != null){
                    getView().onTaskError(TASK_SYN_IMAGE);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                if (getView() != null){
                    getView().onTaskSuccess(TASK_SYN_IMAGE, Arrays.asList(event));
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    public void insertEventLocal(Event event){
        EventManager.getInstance(context).addEvent(event);
        DBManager.getInstance(context).insertEvent(event);
    }

    public void quitEvent(String calendarUid, String eventUid, String type, long originalStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_EVENT_REJECT);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.quitEvent(
                calendarUid,
                eventUid,
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
                    getView().onTaskError(TASK_EVENT_REJECT);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> listHttpResult) {
                synchronizeLocal(listHttpResult.getData());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_REJECT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void acceptEvent(String calendarUid, String eventUid, String type, long orgStartTime){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }
        setEventToEventManager(eventUid);
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
                    getView().onTaskSuccess(TASK_EVENT_ACCEPT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /** call the api to accept timeSlots in a event,
     *  after this api called, it will automatically sync with db
     *
     * */
    public void acceptTimeslots(String calendarUid, String eventUid, HashMap<String, Object> params){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_ACCEPT);
        }

        setEventToEventManager(eventUid);
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptTimeslot(calendarUid,
                eventUid,
                params,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_ACCEPT);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_TIMESLOT_ACCEPT, eventHttpResult.getData());
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
        setEventToEventManager(eventUid);
        Observable<HttpResult<List<Event>>> observable = eventApi.confirm(calendarUid,eventUid, timeslotUid, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_EVENT_CONFIRM);
                }
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_CONFIRM, eventHttpResult.getData());
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }


    public void rejectTimeslots(String calendarUid, String eventUid){
        if (getView()!=null){
            getView().onTaskStart(TASK_TIMESLOT_REJECT);
        }
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.rejectTimeslot(
                calendarUid,
                eventUid,
                syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onTaskError(TASK_TIMESLOT_REJECT);
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
                    getView().onTaskSuccess(TASK_TIMESLOT_REJECT, listHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    /**
     * synchronize the event with local database
     * @param events
     */
    public void synchronizeLocal(List<Event> events){
        for (Event ev: events){
            synchronizeLocal(ev);
        }
    }

    private void synchronizeLocal(Event ev){
        EventManager eventManager = EventManager.getInstance(context);
        Event oldEvent = eventManager.findEventByUid(ev.getEventUid());
        if (oldEvent!=null) {
            eventManager.updateEvent(oldEvent, ev);
        }else{
            eventManager.addEvent(ev);
            DBManager.getInstance(context).insertEvent(ev);
        }
    }

    /**
     * this method is to make calendar automatically scroll to event
     * @param eventUid
     */
    private void setEventToEventManager(String eventUid){
        Event event = EventManager.getInstance(context).findEventByUid(eventUid);
        EventManager.getInstance(context).setCurrentEvent(event);
    }

    /**
     * this method is to make calendar automatically scroll to event
     * @param event
     */
    private void setEventToEventManager(Event event){
        EventManager.getInstance(context).setCurrentEvent(event);

    }

    private boolean imageUploadChecker(List<ImageUploadWrapper> wrappers){
        for (ImageUploadWrapper wrapper:wrappers
             ) {
            if (wrapper.isUploaded()){
                return false;
            }
        }

        return true;
    }

    private class ImageUploadWrapper{
        PhotoUrl photo;
        boolean uploaded;


        ImageUploadWrapper(PhotoUrl photo, boolean uploaded) {
            this.photo = photo;
            this.uploaded = uploaded;
        }

        boolean isUploaded() {
            return uploaded;
        }

        void setUploaded(boolean uploaded) {
            this.uploaded = uploaded;
        }

        PhotoUrl getPhoto() {
            return photo;
        }

        void setPhoto(PhotoUrl photo) {
            this.photo = photo;
        }
    }
}
