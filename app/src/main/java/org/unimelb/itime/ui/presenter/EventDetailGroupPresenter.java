package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupPresenter extends EventCommonPresenter<EventDetailGroupMvpView> {
    private String TAG = "EventDetailPresenter";

    public EventDetailGroupPresenter(Context context) {
        super(context);
    }

    public void acceptTimeslots(Event event){
        eventManager.getWaitingEditEventList().add(event);
        ArrayList<String> timeslotUids = new ArrayList<>();
        for (Timeslot timeslot: event.getTimeslot()){
            if (timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED)){
                timeslotUids.add(timeslot.getTimeslotUid());
            }
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("timeslots", timeslotUids);
        Observable<HttpResult<List<Event>>> observable = eventApi.acceptTimeslot(CalendarUtil.getInstance(getContext()).getCalendar().get(0).getCalendarUid(), event.getEventUid(), parameters);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void confirmEvent(Event newEvent, String timeslotUid){
        Observable<HttpResult<List<Event>>> observable = eventApi.confirm(newEvent.getCalendarUid(), newEvent.getEventUid(), timeslotUid,newEvent);
        Subscriber<HttpResult<List<Event>>> subscriber = new Subscriber<HttpResult<List<Event>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<List<Event>> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                if(getView() != null){
                    getView().refreshCalendars();
                }
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }




}
