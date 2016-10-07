package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;

import java.util.List;

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
        EventManager.getInstance().addEvent(event);
        DBManager.getInstance(getContext()).insertEvent(event);
        for (TimeSlot timeSlot:event.getTimeslots()){
            DBManager.getInstance(getContext()).insertTimeSlot(timeSlot);
        }
        for (Invitee invitee:event.getInvitee()){
            DBManager.getInstance(getContext()).insertInvitee(invitee);
        }

    }

}
