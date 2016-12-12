package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
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

    public EventDetailGroupPresenter(Context context) {
        this.context = context;
        eventApi = HttpUtil.createService(getContext(),EventApi.class);
    }

    public void acceptTimeslots(Event event){
        EventManager.getInstance().getWaitingEditEventList().add(event);
        ArrayList<String> timeslotUids = new ArrayList<>();
        for (Timeslot timeslot: event.getTimeslot()){
            if (timeslot.getStatus().equals(context.getString(R.string.accepted))){
                timeslotUids.add(timeslot.getTimeslotUid());
            }
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("timeslots", timeslotUids);
        Observable<HttpResult<Event>> observable = eventApi.acceptTimeslot(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid(), event.getEventUid(), parameters);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
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
        HttpUtil.subscribe(observable, subscriber);
    }

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
