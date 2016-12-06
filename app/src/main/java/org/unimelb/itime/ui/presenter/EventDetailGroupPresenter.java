package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupPresenter extends MvpBasePresenter<EventDetailGroupMvpView> {
    private Context context;
    private LayoutInflater inflater;
    private EventApi eventApi;
    private String TAG = "EventDetailPresenter";

    public EventDetailGroupPresenter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        eventApi = HttpUtil.createService(getContext(),EventApi.class);
    }


//    public void updateEvent(Event event){
//        Event oldEvent = EventManager.getInstance().getCurrentEvent();
//        // here update EventManager
//        EventManager.getInstance().updateEvent(oldEvent, event);
//        // update db or eventmanager?
//        // here update DB
//        oldEvent.delete();
//        DBManager.getInstance(context).insertEvent(event);
//
//    }

    public void confirmEvent(Event newEvent, String timeslotUid){
//        long startTime = newEvent.
        Observable<HttpResult<Event>> observable = eventApi.confirm(newEvent.getCalendarUid(), newEvent.getEventUid(), timeslotUid,newEvent);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void synchronizeLocal(Event newEvent){
        Event oldEvent = EventManager.getInstance().findEventByUUID(newEvent.getEventUid());
        Log.i(TAG, "APPP: synchronizeLocal: + EventDetail"+"call");

        EventManager.getInstance().updateEvent(oldEvent,newEvent);
        getView().refreshCalendars();
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

}
