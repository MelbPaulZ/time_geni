package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 25/08/2016.
 */
public class EventCreateNewPresenter extends MvpBasePresenter<EventCreateNewMvpView>{

    private String TAG = "EventCreateNewPresenter";
    private Context context;
    public EventCreateNewPresenter(Context context){
        this.context = context;
        eventApi = HttpUtil.createService(context, EventApi.class);
    }
    public Context getContext() {
        return context;
    }
    private EventApi eventApi;

    public void pickPhoto(String tag){
        EventCreateNewMvpView view = getView();
        if (view!=null){
            view.pickPhoto(tag);
        }
    }

    public void addSoloEvent(){
        updateServer();

        Event event = EventManager.getInstance().getCurrentEvent();
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance(getContext()).insertEvent(event);
        event.update();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }

    /**
     *  this is for insert new solo event to server
     */
    public void updateServer(){
        Event event = EventManager.getInstance().getCurrentEvent();
        Observable<HttpResult<Event>> observable = eventApi.insert(event);
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
        HttpUtil.subscribe(observable,subscriber);
    }


}
