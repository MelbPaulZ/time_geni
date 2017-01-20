package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import org.unimelb.itime.util.TokenUtil;
import org.unimelb.itime.util.UserUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

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

    public void updateEvent(final Event event, String type, long originalStartTime){
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
                    getView().onTaskError(TASK_EVENT_UPDATE, null);
                }
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                if(eventHttpResult.getStatus() != 1){
                    throw new RuntimeException(eventHttpResult.getInfo());
                }

                if (getView()!=null){
                    getView().onTaskSuccess(TASK_EVENT_UPDATE, eventHttpResult.getData());
                }
                synchronizeLocal(eventHttpResult.getData());
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
                AppUtil.saveEventSyncToken(context, eventHttpResult.getSyncToken());
                updateImage(event);
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
                    getView().onTaskError(TASK_EVENT_GET, null);
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
//        String syncToken = AppUtil.getEventSyncToken(context);
        String syncToken = TokenUtil.getInstance(context).getEventToken(
                UserUtil.getInstance(context).getUserUid(),
                event.getCalendarUid()
        );
        Observable<HttpResult<List<Event>>> observable = eventApi.insert(event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(TASK_EVENT_INSERT, null);
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
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));

                if(getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_INSERT, eventHttpResult.getData());
                }

                // if the event is successfully insertOrReplace into server, then begin to upload photos
                if (ev.hasPhoto()){
                    uploadImage(ev);
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
                    getView().onTaskError(TASK_EVENT_DELETE, null);
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
                // create file
                final String fileName = event.getEventUid() + "_" + photoUrl.getPhotoUid() + ".png";

                File image = new File(photoUrl.getLocalPath());
                Luban.get(context)
                        .load(image)                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .asObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        })
                        .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                            @Override
                            public Observable<? extends File> call(Throwable throwable) {
                                return Observable.empty();
                            }
                        })
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                final ImageUploadWrapper wrapper = new ImageUploadWrapper(photoUrl,false);
                                wrappers.add(wrapper);
                                final AVFile avFile;
                                try {
                                    avFile = AVFile.withFile(fileName,file);
                                    avFile.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e==null){
                                                wrapper.getPhoto().setUrl(avFile.getUrl());
                                                wrapper.setUploaded(true);

                                                if (imageUploadChecker(wrappers)){
                                                    Toast.makeText(getContext(),"Image Uploaded to leanCloud",Toast.LENGTH_SHORT).show();
                                                    //start to syn server
                                                    for (PhotoUrl photoUrl:event.getPhoto()
                                                            ) {
                                                        updatePhotoToServer(event, photoUrl.getPhotoUid(), photoUrl.getUrl());
                                                    }
                                                }else{
                                                    Log.i(TAG, "done: ");
                                                }
                                            }else{
                                                if (getView() != null){
                                                    getView().onTaskError(TASK_UPLOAD_IMAGE, null);
                                                }
                                            }
                                        }
                                    });
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
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
                    getView().onTaskError(TASK_SYN_IMAGE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                Toast.makeText(getContext(),"Image Synchronized to ITime",Toast.LENGTH_SHORT).show();
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
                    getView().onTaskError(TASK_EVENT_REJECT, null);
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
                    getView().onTaskError(TASK_TIMESLOT_ACCEPT, null);
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
                    getView().onTaskError(TASK_EVENT_CONFIRM, null);
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
                    getView().onTaskError(TASK_TIMESLOT_REJECT, null);
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
            eventManager.setCurrentEvent(ev);
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
            if (!wrapper.isUploaded()){
                return false;
            }
        }

        return true;
    }

    private void updateImage(Event event){
        List<PhotoUrl> newImageSet = new ArrayList<>();
        for (PhotoUrl url: event.getPhoto()
             ) {
            if (url.getUrl().equals("") && !url.getLocalPath().equals("")){
                newImageSet.add(url);
            }
        }
        if (newImageSet.size()!=0){
            event.setPhoto(newImageSet);
            uploadImage(event);
        }else{
            if (getView()!=null){
                getView().onTaskSuccess(TASK_EVENT_UPDATE,null);
            }
        }
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
