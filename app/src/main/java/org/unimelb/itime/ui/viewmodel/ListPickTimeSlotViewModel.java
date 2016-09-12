package org.unimelb.itime.ui.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.activity.EventDetailActivity;

/**
 * Created by Paul on 12/09/2016.
 */
public class ListPickTimeSlotViewModel extends BaseObservable {
    private Context context;
    private Event listPickTSEvent;
    private int position;


    public ListPickTimeSlotViewModel(Context context) {
        this.context = context;
    }

    public View.OnClickListener onClickTimeSlot(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeSlot timeSlot = listPickTSEvent.getTimeslots().get(position);
                if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_pending))){
                    timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
                }else if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_accept))){
                    timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
                }
                setListPickTSEvent(listPickTSEvent);
                // here show let the editEventFragment know the event is change

            }
        };
    }

    public View.OnClickListener viewInviteeResponse(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EventDetailActivity)context).toAttendeeView(listPickTSEvent.getTimeslots().get(position).getStartTime());
            }
        };
    }

    @Bindable
    public Event getListPickTSEvent() {
        return listPickTSEvent;
    }

    public void setListPickTSEvent(Event listPickTSEvent) {
        this.listPickTSEvent = listPickTSEvent;
        notifyPropertyChanged(BR.listPickTSEvent);
    }

    @Bindable
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Context getContext() {
        return context;
    }
}
