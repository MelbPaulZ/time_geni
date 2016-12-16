package org.unimelb.itime.ui.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.CommonMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

import static java.security.AccessController.getContext;

/**
 * Created by Paul on 3/10/16.
 */

public class CommonPresenter<T extends CommonMvpView> extends MvpBasePresenter<T> {

    public Context context;
    private EventApi eventApi;
    private String TAG = "CommonPresenter";
    private OnUpdateEvent onUpdateEvent;
    private OnInsertEvent onInsertEvent;
    private boolean isUpdateFinish, isInsertFinish = false;

    public OnUpdateEvent getOnUpdateEvent() {
        return onUpdateEvent;
    }



    public void setOnUpdateEvent(OnUpdateEvent onUpdateEvent) {
        this.onUpdateEvent = onUpdateEvent;
    }


    public CommonPresenter() {
        Log.i(TAG, "CommonPresenter: ");
    }

    public CommonPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public void updateEventToServer(Event event){
        getView().onShowDialog();
        EventManager.getInstance().getWaitingEditEventList().add(event);

        Observable<HttpResult<Event>> observable = eventApi.update(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid(),event.getEventUid(),event);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {
                if (onUpdateEvent != null){
                    onUpdateEvent.onComplete();
                }
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                getView().onHideDialog();
                if (onUpdateEvent != null){
                    onUpdateEvent.onError(e);
                }
                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                synchronizeLocal(eventHttpResult.getData());
                if (onUpdateEvent != null){
                    onUpdateEvent.onNext(eventHttpResult);
                }
                Log.i(TAG, "onNext: " +"done");
                getView().onHideDialog();
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void synchronizeLocal(Event newEvent){
        Event oldEvent = EventManager.getInstance().findEventByUUID(newEvent.getEventUid());
        Log.i(TAG, "APPP: synchronizeLocal: "+"call");
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
        EventManager.getInstance().getWaitingEditEventList().remove(oldEvent);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
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
                if (onInsertEvent!=null){
                    onInsertEvent.onComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                if (onInsertEvent!=null){
                    onInsertEvent.onError(e);
                }
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Event ev = eventHttpResult.getData();
                insertEventLocal(ev);
                if (onInsertEvent!=null){
                    onInsertEvent.onNext(eventHttpResult);
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        };
        HttpUtil.subscribe(observable,subscriber);
    }

    private void insertEventLocal(Event event){
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance().insertEvent(event);
    }

    public interface OnUpdateEvent{
        void onComplete();
        void onError(Throwable e);
        void onNext(HttpResult<Event> eventHttpResult);
    }

    public interface OnInsertEvent{
        void onComplete();
        void onError(Throwable e);
        void onNext(HttpResult<Event> eventHttpResult);
    }

}
