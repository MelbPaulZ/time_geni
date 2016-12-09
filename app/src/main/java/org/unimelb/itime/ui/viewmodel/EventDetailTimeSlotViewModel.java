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
import org.unimelb.itime.adapter.InviteeInnerResponseAdapter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulapi.EventApi;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;
import java.util.List;

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
        calendar.getTime();
        setHostToolBarString(EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));
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
                    Timeslot timeSlot = new Timeslot();
                    timeSlot.setTimeslotUid(AppUtil.generateUuid());
                    timeSlot.setEventUid(eventDetailHostEvent.getEventUid());
                    timeSlot.setStartTime(((WeekView.TimeSlotStruct)timeSlotView.getTag()).startTime);
                    timeSlot.setEndTime(((WeekView.TimeSlotStruct)timeSlotView.getTag()).endTime);
                    timeSlot.setStatus(getContext().getString(R.string.accepted)); // host create timeslot should be accepted
                    timeSlot.setUserUid(UserUtil.getUserUid());
                    eventDetailHostEvent.getTimeslot().add(timeSlot);
                    WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct)timeSlotView.getTag();
                    struct.object =timeSlot;
                    // after add, needs reload to display it
                    mvpView.addTimeslot(struct);
                    mvpView.reloadTimeslot();
                }else{
                    // do nothing
                }
           }

           @Override
           public void onTimeSlotClick(TimeSlotView timeSlotView) {
               if (presenter.getView() != null){
                   if(eventDetailHostEvent.getStatus().equals("confirmed")){
                       presenter.getView().popupTimeSlotWindow(timeSlotView);
                   }else{
                       // here should popup window
                       if(timeSlotView.isSelect()){
                           // silence unselect
                           presenter.getView().onClickTimeSlotView(timeSlotView);
                       }else{
                           presenter.getView().popupTimeSlotWindow(timeSlotView);
                       }
                   }
               }
           }

           @Override
           public void onTimeSlotDragStart(TimeSlotView timeSlotView) {

           }

           @Override
           public void onTimeSlotDragging(TimeSlotView timeSlotView, int i, int i1) {

           }

           @Override
           public void onTimeSlotDragDrop(TimeSlotView timeSlotView, long startTime, long endTime) {
               if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getUserUid())){
                   // host:
                   WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct) timeSlotView.getTag();
                   struct.startTime = startTime;
                   struct.endTime = endTime;
                   if (presenter.getView()!=null) {
                       presenter.getView().reloadTimeslot();
                   }

                   Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
                   Timeslot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
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
                //if time slot changed
                if (EventManager.getInstance().getCurrentEvent().getTimeslot().size()
                        != eventDetailHostEvent.getTimeslot().size()){
                    List<Invitee> invitees = eventDetailHostEvent.getInvitee();
                    for (Invitee invitee:invitees
                            ) {
                        for (SlotResponse response: invitee.getSlotResponses()
                             ) {
                            response.setStatus("pending");
                        }
                    }

                    presenter.updateEvent(eventDetailHostEvent);
                }

                if (mvpView!=null){
                    mvpView.onClickDone();
                }
            }
        };
    }
//    &******************************

//    public void popupTimeSlotWindow(final TimeSlotView timeSlotView){
//        final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
//        LayoutInflater inflater = LayoutInflater.from(presenter.getContext());
//        View root = inflater.inflate(R.layout.fragment_timeslot_attendee_response, null);
//
//        TextView button_cancel = (TextView) root.findViewById(R.id.invitee_response_cancel_button);
//        button_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//            }
//        });
//
//        TextView button_reject = (TextView) root.findViewById(R.id.invitee_response_select_button);
//        button_reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismiss();
//                // here should add presenter change event status as reject
//                onClickTimeSlotView(timeSlotView);
//            }
//        });
//        alertDialog.setView(root);
//        alertDialog.show();
//    }

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

    public void initTimeSlots(WeekView weekView){
        weekView.resetTimeSlots();
        if (eventDetailHostEvent.hasTimeslots()) {
            for (Timeslot timeSlot : eventDetailHostEvent.getTimeslot()) {
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getUserUid())){
                    // this is host event
                    if (timeSlot.getIsConfirmed()==1){
                        struct.status = true;
                    }else{
                        struct.status=false;
                    }
                }else{
                    if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))){
                        struct.status=false;
                    }else if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_accept))){
                        struct.status = true;
                    }
                }
                weekView.addTimeSlot(struct);
            }
        }
//        weekView.reloadTimeSlots(false);
        final WeekView  wv = weekView;
        weekView.postDelayed(new Runnable() {
            @Override
            public void run() {
                wv.reloadTimeSlots(false);
            }
        },100);
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
