package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.EventDetailHostTimeSlotMvpVIew;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotPresenter extends MvpBasePresenter<EventDetailHostTimeSlotMvpVIew> {
    private Context context;
    public EventDetailHostTimeSlotPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
