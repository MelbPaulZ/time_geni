package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingPresenter extends MvpBasePresenter<EventCreateDetailBeforeSendingMvpView> {
    private Context context;
    public EventCreateDetailBeforeSendingPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // update database
    public void addEvent(Event event){
        EventManager.getInstance().setCurrentEvent(event);
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance(context).insertEvent(event);
        if (event.hasTimeslots()) {
            for (TimeSlot timeSlot : event.getTimeslots()) {
                DBManager.getInstance(context).insertTimeSlot(timeSlot);
            }
        }
        if (event.hasAttendee()){
            for (Invitee invitee: event.getInvitee()){
                DBManager.getInstance(context).insertInvitee(invitee);
            }
        }
    }

}
