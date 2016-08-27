package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeSlotPresenter extends MvpBasePresenter<EventCreateNewTimeSlotMvpView> {
    private final String TAG = "EventCreateTimeSlotPresenter";
    private Context context;
    public EventCreateTimeSlotPresenter(Context context) {
        this.context = context;
    }


}
