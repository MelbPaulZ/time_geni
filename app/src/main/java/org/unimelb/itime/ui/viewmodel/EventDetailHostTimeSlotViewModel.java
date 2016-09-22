package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;

import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.timeslotview.WeekTimeSlotView;

import java.util.Calendar;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotViewModel extends BaseObservable {
    private EventDetailHostTimeSlotPresenter presenter;
    private Event eventDetailHostEvent;
    private String tag;
    private String hostToolBarString;

    public EventDetailHostTimeSlotViewModel(EventDetailHostTimeSlotPresenter presenter) {
        this.presenter = presenter;
    }
//    ********************************************************************************************

    public WeekTimeSlotView.OnTimeSlotClickListener onTimeSlotClick(){
        return new WeekTimeSlotView.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotClick(long l) {
                for (TimeSlot timeSlot: eventDetailHostEvent.getTimeslots()){
                    if (timeSlot.getStartTime() == l){
                        if (timeSlot.getStatus()==getContext().getString(R.string.timeslot_status_pending))
                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
                        else if(timeSlot.getStatus() == getContext().getString(R.string.timeslot_status_accept)){
                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
                        }
                    }
                }
            }
        };
    }

    public WeekTimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChange(){
        return new WeekTimeSlotView.OnTimeSlotWeekViewChangeListener() {
            @Override
            public void onWeekChanged(Calendar calendar) {
                String tmp = EventUtil.getMonth(presenter.getContext() ,calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
                // here should show the month and year
            }
        };
    }


    public View.OnClickListener toHostEventDetail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag.equals(getContext().getString(R.string.tag_host_event_detail))){
                    presenter.toHostEventDetail();
                }else if (tag.equals(getContext().getString(R.string.tag_host_event_edit))){
                    presenter.toHostEventEdit();
                }
            }
        };
    }

//    *****************************************************************************************
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Context getContext(){
        return presenter.getContext();
    }

    @Bindable
    public Event getEventDetailHostEvent() {
        return eventDetailHostEvent;
    }

    public void setEventDetailHostEvent(Event eventDetailHostEvent) {
        this.eventDetailHostEvent = eventDetailHostEvent;
        notifyPropertyChanged(BR.eventDetailHostEvent);
    }

    @Bindable
    public String getHostToolBarString() {
        return hostToolBarString;
    }

    public void setHostToolBarString(String hostToolBarString) {
        this.hostToolBarString = hostToolBarString;
        notifyPropertyChanged(BR.hostToolBarString);
    }
}
