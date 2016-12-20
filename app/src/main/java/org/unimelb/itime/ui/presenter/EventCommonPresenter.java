package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
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
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */

public class EventCommonPresenter<T extends EventCommonMvpView> extends MvpBasePresenter<T> {

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

    // todo fetch all calendars
    public void updateEventToServer(Event event){
        if(getView() != null){
            getView().onTaskStart();
        }

        Gson gson = new Gson();
        String str = gson.toJson(event);
        Log.i(TAG, "updateEventToServer: " + str);


        eventManager.getWaitingEditEventList().add(event);
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.update(calendarUtil.getCalendar().get(0).getCalendarUid(),event.getEventUid(),event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(getView() != null){
                    getView().onTaskError(e);
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
                    getView().onTaskComplete(eventHttpResult.getData());
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
            Event oldEvent = eventManager.findEventByUUID(ev.getEventUid());
            eventManager.updateEvent(oldEvent, ev);
            eventManager.getWaitingEditEventList().remove(oldEvent);
        }
        Log.i(TAG, "APPP: synchronizeLocal: "+"call");
    }

    public void updateAndInsertEvent(Event orgEvent, Event newEvent){
        updateEventToServer(orgEvent);
        insertNewEventToServer(newEvent);
    }

    /**
     * call the api to insert a event to server
     * after this api is called, it will automatically sync with local db
     * @param event
     */
    public void insertNewEventToServer(Event event){
        String syncToken = AppUtil.getEventSyncToken(context);
        Observable<HttpResult<List<Event>>> observable = eventApi.insert(event, syncToken);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                if(getView() != null){
                    getView().onTaskError(e);
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
                    getView().onTaskComplete(eventHttpResult.getData());
                }
                // todo: put event bus into fragment
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
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
    public void uploadImage(final Event event){
        if (event.hasPhoto()){
            for (PhotoUrl photoUrl : event.getPhoto()){
                // create file
                File file = new File(photoUrl.getLocalPath());
                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
                // generate photoUid part
                RequestBody photoUidRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoUrl.getPhotoUid());
                Observable<HttpResult<PhotoUrl>> observable = photoApi.uploadPhoto(photoUidRequestBody, body);
                Subscriber<HttpResult<PhotoUrl>> subscriber = new Subscriber<HttpResult<PhotoUrl>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(HttpResult<PhotoUrl> photoUrlHttpResult) {
                        PhotoUrl photoUrl = photoUrlHttpResult.getData();
                        for (PhotoUrl url : event.getPhoto()){
                            if (photoUrl.getPhotoUid().equals(url.getPhotoUid())){
                                url.setUrl(photoUrl.getUrl());
                                updatePhotoToServer(event, url);
                            }
                        }
                    }
                };
                HttpUtil.subscribe(observable, subscriber);
            }
        }
    }

    /**
     * update the photo url to server
     * @param event
     * @param photoUrl
     */
    private void updatePhotoToServer(Event event, PhotoUrl photoUrl){
        Observable<HttpResult<Event>> observable = photoApi.updatePhoto(event.getCalendarUid(), event.getEventUid(), photoUrl.getPhotoUid(), photoUrl.getUrl());
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
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
