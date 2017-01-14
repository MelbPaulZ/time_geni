package org.unimelb.itime.ui.presenter;

import android.content.Context;
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

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static org.unimelb.itime.ui.presenter.EventCommonPresenter.TASK_EVENT_INSERT;

/**
 * Created by yinchuandong on 12/1/17.
 */

public class EventPresenter<V extends TaskBasedMvpView<List<Event>>> extends MvpBasePresenter<V> {

    private Context context;
    private EventApi eventApi;
    private PhotoApi photoApi;
    private final static String TAG = "EventPresenter";

    public EventPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
        photoApi = HttpUtil.createService(context, PhotoApi.class);
    }

    public Context getContext(){
        return this.context;
    }




    public void updateEvent(Event event, String type, long originalStartTime){
        // TODO: 14/1/17 implement update event

        if (getView()!=null){
            getView().onTaskSuccess(0 , null);
        }
    }


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

                // if the event is successfully insert into server, then begin to upload photos
                uploadImage(ev);
                insertEventLocal(ev);
                if(getView() != null){
                    getView().onTaskSuccess(TASK_EVENT_INSERT, eventHttpResult.getData());
                }
                // todo: put event bus into fragment
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    public void deleteEvent(Event event, String type, long orgStartTime) {

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
                synchronizeLocal(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    public void insertEventLocal(Event event){
        EventManager.getInstance(context).addEvent(event);
        DBManager.getInstance(context).insertEvent(event);
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
}
