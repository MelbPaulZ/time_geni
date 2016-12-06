package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */

public class CommonPresenter extends MvpBasePresenter<MvpView> {

    public Context context;
    private EventApi eventApi;
    private String TAG = "CommonPresenter";
    public CommonPresenter() {
    }

    public CommonPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public void updateEventToServer(Event event){
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
        Event oldEvent = EventManager.getInstance().findEventByUUID(newEvent.getEventUid());
        Log.i(TAG, "APPP: synchronizeLocal: "+"call");
        EventManager.getInstance().updateEvent(oldEvent, newEvent);
        EventManager.getInstance().getWaitingEditEventList().remove(oldEvent);
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }


}
