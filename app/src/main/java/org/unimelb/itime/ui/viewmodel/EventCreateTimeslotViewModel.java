package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.vendor.BR;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeslotViewModel extends BaseObservable {
    private final String TAG = "TimeslotViewModel";
    private String toolbarString = getMonthName(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR);
    private EventCreateTimeSlotPresenter eventCreateTimeSlotPresenter;

    public EventCreateTimeslotViewModel(EventCreateTimeSlotPresenter eventCreateTimeSlotPresenter) {
        super();
        this.eventCreateTimeSlotPresenter = eventCreateTimeSlotPresenter;
    }

    //    ************************************************
    private String getMonthName(int index){
        String[] Months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        return Months[index];
    }

    @Bindable
    public String getToolbarString() {
        return toolbarString;
    }

    public void setToolbarString(String toolbarString) {
        this.toolbarString = toolbarString;
        notifyPropertyChanged(BR.toolbarString);
    }

//    public TimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChangeListener(){
//        return new TimeSlotView.OnTimeSlotWeekViewChangeListener
//    }

}
