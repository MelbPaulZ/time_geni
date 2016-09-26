package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import org.unimelb.itime.vendor.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeslotViewModel extends BaseObservable {

    private String titleString;
    private EventCreateTimeSlotPresenter presenter;
    private Event event;
    private String tag;
    private ObservableField<Boolean> isChangeDuration = new ObservableField<>(false);
    private String durationTimeString = "1 hour";

    public EventCreateTimeslotViewModel(EventCreateTimeSlotPresenter presenter){
        this.presenter = presenter;
        titleString = initToolBarTitle();

    }

    public WeekView.OnHeaderListener onWeekViewChange(){
       return new WeekView.OnHeaderListener() {
           @Override
           public void onMonthChanged(MyCalendar myCalendar) {
               setTitleString((EventUtil.getMonth(presenter.getContext(), myCalendar.getMonth()) + " "  + myCalendar.getYear()));
           }
       };
    }

    @Bindable
    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
        notifyPropertyChanged(BR.titleString);
    }


//    private final String TAG = "TimeslotViewModel";
//    private String toolbarString = initToolBarTitle();
//    private EventCreateTimeSlotPresenter presenter;
//    private ObservableField<Boolean> isChangeDuration = new ObservableField<>(false);
//    private String durationTimeString = "1 hour";
//    private Event newEvent;
//    private String tag;
//
//    public EventCreateTimeslotViewModel(EventCreateTimeSlotPresenter presenter, Event event) {
//        super();
//        this.presenter = presenter;
//        this.newEvent = event;
//    }
//
//
    public String initToolBarTitle(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = -(calendar.get(Calendar.DAY_OF_WEEK)-1);
        calendar.add(Calendar.DATE,delta);
        return EventUtil.getMonth(presenter.getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }
//
//    public Context getContext(){
//        return presenter.getContext();
//    }
//
//
//    //    ************************************************
//    private String getMonthName(int index){
//        String[] Months = {"January","February","March","April","May","June",
//                "July","August","September","October","November","December"};
//        return Months[index];
//    }
//
//    @Bindable
//    public String getToolbarString() {
//        return toolbarString;
//    }
//
//    public void setToolbarString(String toolbarString) {
//        this.toolbarString = toolbarString;
//        notifyPropertyChanged(BR.toolbarString);
//    }

//    public WeekTimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChange(){
//        return new WeekTimeSlotView.OnTimeSlotWeekViewChangeListener() {
//            @Override
//            public void onWeekChanged(Calendar calendar) {
//                String tmp = getMonthName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
//                setToolbarString(tmp);
//            }
//        };
//    }
//
//    public WeekTimeSlotView.OnTimeSlotClickListener onTimeSlotClick(){
//        return new WeekTimeSlotView.OnTimeSlotClickListener() {
//            @Override
//            public void onTimeSlotClick(long l) {
//                for (TimeSlot timeSlot: newEvent.getTimeslots()){
//                    if (timeSlot.getStartTime() == l){
//                        if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))){
//                            // if it is choosed, not init create status
//                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_create));
//                        }else if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_create))){
//                            // pending means it will be the suggest timeslot showing for invitees and host
//                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
//                        }
//                    }
//                }
//                setNewEvent(newEvent);
//            }
//        };
//    }
//
//
//    public View.OnClickListener toInviteePicker(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                presenter.toInviteePicker(tag);
//            }
//        };
//    }
//
//
//
    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(RelativeLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }
//
//    @Bindable
//    public boolean getIsChangeDuration() {
//        return isChangeDuration.get();
//    }
//
//    public void setIsChangeDuration(boolean isChangeDuration) {
//        this.isChangeDuration.set(isChangeDuration);
//        notifyPropertyChanged(BR.isChangeDuration);
//    }
//
//    @Bindable
//    public String getDurationTimeString() {
//        return durationTimeString;
//    }
//
//    public void setDurationTimeString(String durationTimeString) {
//        this.durationTimeString = durationTimeString;
//        notifyPropertyChanged(BR.durationTimeString);
//        // need to update event duration
//    }
//
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public Boolean getIsChangeDuration() {
        return isChangeDuration.get();
    }

    public void setIsChangeDuration(Boolean isChangeDuration) {
        this.isChangeDuration.set(isChangeDuration);
        notifyPropertyChanged(BR.isChangeDuration);
    }

    @Bindable
    public String getDurationTimeString() {
        return durationTimeString;
    }

    public void setDurationTimeString(String durationTimeString) {
        this.durationTimeString = durationTimeString;
        notifyPropertyChanged(BR.durationTimeString);
    }
//
//
//    public void setNewEvent(Event event){
//        this.newEvent = event;
//        notifyPropertyChanged(BR.newEvent);
//    }
//
//    @Bindable
//    public Event getNewEvent(){
//        return newEvent;
//    }
}
