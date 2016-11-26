package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.DBManager;
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
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                updateLocalDB(eventHttpResult.getData());
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void updateLocalDB(Event newEvent){
        // update local db
        Event oldEvent = EventManager.getInstance().getCurrentEvent();
        // here update DB
        Event dbOldEvent = DBManager.getInstance(context).getEvent(oldEvent.getEventUid());
        dbOldEvent.delete();
        // here update EventManager
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
        // update db or eventmanager?
        DBManager.getInstance(context).insertEvent(newEvent);
        DBManager.getInstance(context).getAllEvents();
    }

    public void updateEvent(Event newEvent){
        updateServer(newEvent);
//        updateLocalDB(newEvent);


    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
