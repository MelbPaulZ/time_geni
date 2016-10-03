package org.unimelb.itime.ui.viewmodel;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

import butterknife.OnClick;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotViewModel extends BaseObservable {
    private EventDetailHostTimeSlotPresenter presenter;
    private Event eventDetailHostEvent;
    private String tag;
    private String hostToolBarString;
//
    public EventDetailHostTimeSlotViewModel(EventDetailHostTimeSlotPresenter presenter) {
        this.presenter = presenter;
        Calendar calendar = Calendar.getInstance();
        setHostToolBarString(EventUtil.getMonth(getContext(), calendar.MONTH) + " " + calendar.YEAR);
    }

    public WeekView.OnHeaderListener onWeekViewChange(){
        return new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                String title = EventUtil.getMonth(getContext(), myCalendar.getMonth()) + " " + myCalendar.getYear();
                setHostToolBarString(title);
            }
        };
    }

   public FlexibleLenViewBody.OnTimeSlotListener onWeekOuterListener(){
       return new FlexibleLenViewBody.OnTimeSlotListener() {
           @Override
           public void onTimeSlotCreate(TimeSlotView timeSlotView) {
                if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getUserUid())){
                    // I am host
                    TimeSlot timeSlot = new TimeSlot(EventUtil.generateTimeSlotUid(),
                            eventDetailHostEvent.getEventUid(),
                            ((WeekView.TimeSlotStruct)timeSlotView.getTag()).startTime,
                            ((WeekView.TimeSlotStruct)timeSlotView.getTag()).endTime,
                            getContext().getString(R.string.timeslot_status_create),
                            0,
                            0);
                    eventDetailHostEvent.getTimeslots().add(timeSlot);
                    WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct)timeSlotView.getTag();
                    struct.object =timeSlot;
                }else{
                    // do nothing
                }
           }

           @Override
           public void onTimeSlotClick(TimeSlotView timeSlotView) {
               // change status of view and struct
               boolean newStatus = !timeSlotView.isSelect();
               timeSlotView.setStatus(newStatus);
               ((WeekView.TimeSlotStruct)timeSlotView.getTag()).status = newStatus;


               // change event attributes
               TimeSlot calendarTimeSlot = (TimeSlot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
               TimeSlot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
               if (timeSlot!=null) {
                   if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))) {
                       timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
                   } else if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_accept))) {
                       timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
                   }
               }else{
                   Log.i("error", "onTimeSlotClick: " + "no timeslot found");
               }
           }

           @Override
           public void onTimeSlotDragStart(TimeSlotView timeSlotView) {

           }

           @Override
           public void onTimeSlotDragging(TimeSlotView timeSlotView, int i, int i1) {

           }

           @Override
           public void onTimeSlotDragDrop(TimeSlotView timeSlotView) {
                if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getUserUid())){
                    // host:
                    TimeSlot calendarTimeSlot = (TimeSlot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
                    TimeSlot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
                    if (timeSlot!=null) {
                        timeSlot.setStartTime(timeSlotView.getStartTimeM());
                        timeSlot.setEndTime(timeSlotView.getEndTimeM());
                    }
                }else{
                    // do nothing
                }
           }
       };
   }

    public View.OnClickListener onBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag == getContext().getString(R.string.tag_host_event_detail)){
                    presenter.toHostEventDetail(EventUtil.getEventInDB(getContext(),eventDetailHostEvent.getEventUid()));
                }else if (tag == getContext().getString(R.string.tag_host_event_edit)){
                    presenter.toHostEventEdit(EventUtil.getEventInDB(getContext(),eventDetailHostEvent.getEventUid()));
                }
            }
        };
    }

    public View.OnClickListener onDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag == getContext().getString(R.string.tag_host_event_detail)){
                    presenter.toHostEventDetail(eventDetailHostEvent);
                }else if (tag == getContext().getString(R.string.tag_host_event_edit))
                    presenter.toHostEventEdit(eventDetailHostEvent);
            }
        };
    }


////    ********************************************************************************************
//
//    public WeekTimeSlotView.OnTimeSlotClickListener onTimeSlotClick(){
//        return new WeekTimeSlotView.OnTimeSlotClickListener() {
//            @Override
//            public void onTimeSlotClick(long l) {
//                for (TimeSlot timeSlot: eventDetailHostEvent.getTimeslots()){
//                    if (timeSlot.getStartTime() == l){
//                        if (timeSlot.getStatus()==getContext().getString(R.string.timeslot_status_pending))
//                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
//                        else if(timeSlot.getStatus() == getContext().getString(R.string.timeslot_status_accept)){
//                            timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
//                        }
//                    }
//                }
//            }
//        };
//    }
//
//    public WeekTimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChange(){
//        return new WeekTimeSlotView.OnTimeSlotWeekViewChangeListener() {
//            @Override
//            public void onWeekChanged(Calendar calendar) {
//                String tmp = EventUtil.getMonth(presenter.getContext() ,calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
//                // here should show the month and year
//            }
//        };
//    }
//
//
//    public View.OnClickListener toHostEventDetail(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (tag.equals(getContext().getString(R.string.tag_host_event_detail))){
//                    presenter.toHostEventDetail();
//                }else if (tag.equals(getContext().getString(R.string.tag_host_event_edit))){
//                    presenter.toHostEventEdit();
//                }
//            }
//        };
//    }
//
////    *****************************************************************************************
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
