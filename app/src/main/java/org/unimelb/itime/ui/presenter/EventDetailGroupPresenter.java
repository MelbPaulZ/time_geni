package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupPresenter extends MvpBasePresenter<EventDetailGroupMvpView> {
    private Context context;
    private LayoutInflater inflater;

    public EventDetailGroupPresenter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
    }


    public void updateEvent(Event event){
        Event oldEvent = EventManager.getInstance().getCurrentEvent();
        // here update EventManager
        EventManager.getInstance().updateEvent(oldEvent, event);
        // update db or eventmanager?
        // here update DB
        oldEvent.delete();
        DBManager.getInstance(context).insertEvent(event);

    }

    public void confirmEvent(Event newEvent, TimeSlot newTimeSlot){
//        long startTime = newEvent.
        newEvent.setStartTime(newTimeSlot.getStartTime());
        newEvent.setEndTime(newTimeSlot.getEndTime());

        // to do
        // call server to confirm
        this.updateEvent(newEvent);
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

}
