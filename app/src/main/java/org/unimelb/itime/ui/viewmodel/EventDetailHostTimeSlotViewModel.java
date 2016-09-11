package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;

import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
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
                // here change the event timeslot
            }
        };
    }

    public WeekTimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChange(){
        return new WeekTimeSlotView.OnTimeSlotWeekViewChangeListener() {
            @Override
            public void onWeekChanged(Calendar calendar) {
                String tmp = EventUtil.getMonth(presenter.getContext() ,calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
//                setHostToolBarString(tmp);
            }
        };
    }


    public View.OnClickListener toHostEventDetail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.toHostEventDetail();
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
