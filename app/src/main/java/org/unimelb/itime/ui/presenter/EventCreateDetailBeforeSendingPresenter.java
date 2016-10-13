package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.util.HttpUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingPresenter extends MvpBasePresenter<EventCreateDetailBeforeSendingMvpView> {
    public static final String TAG = EventCreateDetailBeforeSendingPresenter.class.getSimpleName();
    private Context context;
    private EventApi eventApi;
    public EventCreateDetailBeforeSendingPresenter(Context context) {
        this.context = context;
        this.eventApi = HttpUtil.createService(context, EventApi.class);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // update database
    public void addEvent(Event event){
        Observable<HttpResult<Event>> observable = eventApi.insert(event);
        Subscriber<HttpResult<Event>> subscriber = new Subscriber<HttpResult<Event>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Log.d(TAG, "onNext: ");
            }
        };
        HttpUtil.subscribe(observable, subscriber);

//        EventManager.getInstance().addEvent(event);
//        DBManager.getInstance(getContext()).insertEvent(event);
//        for (Timeslot timeSlot:event.getTimeslot()){
//            DBManager.getInstance(getContext()).insertTimeSlot(timeSlot);
//        }
//        for (Invitee invitee:event.getInvitee()){
//            DBManager.getInstance(getContext()).insertInvitee(invitee);
//        }

    }

}
