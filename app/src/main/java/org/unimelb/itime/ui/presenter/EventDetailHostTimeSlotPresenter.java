package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotPresenter extends EventCommonPresenter<EventDetailTimeSlotMvpVIew> {
    private final String TAG = "DetailHostTSPresenter";

    public EventDetailHostTimeSlotPresenter(Context context) {
        super(context);
    }

    public void getTimeSlots(Event event, long startTime) {
        List<Invitee> invitees = event.getInvitee();

        Observable<HttpResult<List<Timeslot>>> observable = eventApi.recommend(invitees, startTime);
        Subscriber<HttpResult<List<Timeslot>>> subscriber = new Subscriber<HttpResult<List<Timeslot>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<Timeslot>> result) {
                if(getView() != null){
                    getView().onRecommend(result.getData());
                }
                Log.i(TAG, "onNext: ");
            }
        };

        HttpUtil.subscribe(observable, subscriber);
    }
}
