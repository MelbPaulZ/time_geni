package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotPresenter extends CommonPresenter<EventDetailTimeSlotMvpVIew> {
    private Context context;

    public EventDetailHostTimeSlotPresenter(Context context) {
        super(context);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void updateEvent(Event event){
        updateEventToServer(event);
    }
}
