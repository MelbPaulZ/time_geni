package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulapi.PhotoApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingPresenter extends MvpBasePresenter<EventCreateDetailBeforeSendingMvpView> {
    public static final String TAG = EventCreateDetailBeforeSendingPresenter.class.getSimpleName();
    private Context context;
    private EventApi eventApi;
    private PhotoApi photoApi;
    public EventCreateDetailBeforeSendingPresenter(Context context) {
        this.context = context;
        this.eventApi = HttpUtil.createService(context, EventApi.class);
        this.photoApi = HttpUtil.createService(context, PhotoApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // update database
    public void addEvent(Event event){
        Observable<HttpResult<Event>> observable = eventApi.insert(event);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Log.d(TAG, "onNext: ");
                // after inset event into server, upload images

                Event event = eventHttpResult.getData();
                EventManager.getInstance().addEvent(event);
                DBManager.getInstance(getContext()).insertEvent(event);
                uploadImage(event);
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    private void uploadImage(final Event event){
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
