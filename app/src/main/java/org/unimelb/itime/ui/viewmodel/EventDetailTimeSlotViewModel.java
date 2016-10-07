package org.unimelb.itime.ui.viewmodel;
import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailTimeSlotViewModel extends BaseObservable {
    private EventDetailHostTimeSlotPresenter presenter;
    private Event eventDetailHostEvent;
    private String tag;
    private String hostToolBarString;
    private EventDetailTimeSlotMvpVIew mvpView;
//
    public EventDetailTimeSlotViewModel(EventDetailHostTimeSlotPresenter presenter) {
        this.presenter = presenter;
        Calendar calendar = Calendar.getInstance();
        setHostToolBarString(EventUtil.getMonth(getContext(), calendar.MONTH) + " " + calendar.YEAR);
        this.mvpView = presenter.getView();
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
                            getContext().getString(R.string.timeslot_status_pending),
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
               // here should popup window
               popupTimeSlotWindow(timeSlotView);

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
                if (mvpView!=null){
                    mvpView.onClickBack();
                }
            }
        };
    }

    public View.OnClickListener onDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickDone();
                }
            }
        };
    }

    public void popupTimeSlotWindow(final TimeSlotView timeSlotView){
        final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(presenter.getContext());
        View root = inflater.inflate(R.layout.fragment_timeslot_attendee_response, null);

        TextView button_cancel = (TextView) root.findViewById(R.id.invitee_response_cancel_button);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        TextView button_reject = (TextView) root.findViewById(R.id.invitee_response_select_button);
        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence msg = "select timeslot";
                Toast.makeText(presenter.getContext(), msg, Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                // here should add presenter change event status as reject
                onClickTimeSlotView(timeSlotView);
            }
        });
        alertDialog.setView(root);
        alertDialog.show();
    }

    public void onClickTimeSlotView(TimeSlotView timeSlotView){
        // change status of view and struct
        if (UserUtil.getUserUid() == eventDetailHostEvent.getHostUserUid()){
            // for host , only one timeslot can be selected
//                   if (TimeSlotUtil.getSelectedTimeSlots(getContext(),eventDetailHostEvent.getTimeslots()).size()<1){
//                       boolean newStatus = !timeSlotView.isSelect();
//                       timeSlotView.setStatus(newStatus);
//                       ((WeekView.TimeSlotStruct) timeSlotView.getTag()).status = newStatus;
//                   }else{
//                       for (TimeSlot timeSlot: eventDetailHostEvent.getTimeslots()){
//                           // pending all timeslots
//                           timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
//                       }
//                       boolean newStatus = !timeSlotView.isSelect();
//                       timeSlotView.setStatus(newStatus);
//                       ((WeekView.TimeSlotStruct) timeSlotView.getTag()).status = newStatus;
//                   }
            // change later
        }else {
            boolean newStatus = !timeSlotView.isSelect();
            timeSlotView.setStatus(newStatus);
            ((WeekView.TimeSlotStruct) timeSlotView.getTag()).status = newStatus;
        }


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
