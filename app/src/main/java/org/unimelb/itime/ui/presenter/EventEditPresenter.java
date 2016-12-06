package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditPresenter extends MvpBasePresenter<EventEditMvpView> {
    private Context context;
    private EventApi eventApi;
    private String TAG = "EventEditPresenter";

    public EventEditPresenter(Context context) {
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public void changeLocation(){
        EventEditMvpView view = getView();
        if (view != null){
            view.changeLocation();
        }
    }

    private void updateServer(Event event){
        Observable<HttpResult<Event>> observable = eventApi.update(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid(),event.getEventUid(),event);
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
                Log.i(TAG, "onNext: " +"done");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void synchronizeLocal(Event newEvent){
        Event oldEvent = EventManager.getInstance().getCurrentEvent();
        Log.i(TAG, "APPP: synchronizeLocal: EventeditPresenter");
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
    }

    public void updateEvent(Event newEvent){
        updateServer(newEvent);
    }

    /** This method is update one of the repeat event, not all of them
     * */
    public void updateOnlyThisEvent(Event orgEvent,Event event){
        updateOrgEventToServer(orgEvent);
        insertNewEventToServer(event);
    }

    private void updateOrgEventToServer(Event orgEvent){
        Observable<HttpResult<Event>> observable = eventApi.update(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid(), orgEvent.getEventUid(), orgEvent);
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
                Log.i(TAG, "onNext: " + "done");
            }
        };
        HttpUtil.subscribe( observable ,  subscriber);
    }

    private void insertNewEventToServer(Event event){
        Observable<HttpResult<Event>> observable = eventApi.insert(event);
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
                insertEventLocal(eventHttpResult.getData());
                Log.i(TAG, "onNext: " + "done");
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void insertEventLocal(Event event){
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance().insertEvent(event);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
