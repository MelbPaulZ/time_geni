package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */

public class EventCommonPresenter<T extends EventCommonMvpView> extends MvpBasePresenter<T> {

    public Context context;
    private EventApi eventApi;
    private String TAG = "EventCommonPresenter";
    private CalendarUtil calendarUtil;

    public EventCommonPresenter() {
        Log.i(TAG, "EventCommonPresenter: ");
    }

    public EventCommonPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
        calendarUtil = CalendarUtil.getInstance(context);
    }

    // todo fetch all calendars

    public void updateEventToServer(Event event){
        if(getView() != null){
            getView().onTaskStart();
        }
        EventManager.getInstance().getWaitingEditEventList().add(event);
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
                List<Event> events = eventHttpResult.getData();
                for (Event ev: events){
                    synchronizeLocal(ev);
                }
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

    private void synchronizeLocal(Event newEvent){
        Event oldEvent = EventManager.getInstance().findEventByUUID(newEvent.getEventUid());
        Log.i(TAG, "APPP: synchronizeLocal: "+"call");
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
        EventManager.getInstance().getWaitingEditEventList().remove(oldEvent);
    }

    public void updateAndInsertEvent(Event orgEvent, Event newEvent){
        updateEventToServer(orgEvent);
        insertNewEventToServer(newEvent);
    }

    private void insertNewEventToServer(Event event){
        Observable<HttpResult<Event>> observable = eventApi.insert(event);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
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
            public void onNext(HttpResult<Event> eventHttpResult) {
                Event ev = eventHttpResult.getData();
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

    private void insertEventLocal(Event event){
        EventManager.getInstance(context).addEvent(event);
        DBManager.getInstance(context).insertEvent(event);
    }

}
