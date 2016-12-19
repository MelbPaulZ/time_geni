package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.util.AppUtil;
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
        calendar.getTime();
        setHostToolBarString(EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR));
        this.mvpView = presenter.getView();
    }



    public WeekView.OnHeaderListener onWeekViewChange() {
        return new WeekView.OnHeaderListener() {
            @Override
            public void onMonthChanged(MyCalendar myCalendar) {
                String title = EventUtil.getMonth(getContext(), myCalendar.getMonth()) + " " + myCalendar.getYear();
                setHostToolBarString(title);
                Calendar calendar = Calendar.getInstance();
                calendar.set(myCalendar.getYear(), myCalendar.getMonth(), myCalendar.getDay(), 0, 0, 0);
                long weekStartTime = calendar.getTimeInMillis();
                presenter.getTimeSlots(eventDetailHostEvent, weekStartTime);
            }
        };
    }

    private void createTimeslotInStatus(TimeSlotView timeSlotView, String status) {
        Timeslot timeSlot = new Timeslot();
        timeSlot.setTimeslotUid(AppUtil.generateUuid());
        timeSlot.setEventUid(eventDetailHostEvent.getEventUid());
        timeSlot.setStartTime(((WeekView.TimeSlotStruct) timeSlotView.getTag()).startTime);
        timeSlot.setEndTime(((WeekView.TimeSlotStruct) timeSlotView.getTag()).endTime);
        timeSlot.setStatus(status);
        timeSlot.setUserUid(UserUtil.getUserUid());
        eventDetailHostEvent.getTimeslot().add(timeSlot);
        WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct) timeSlotView.getTag();
        struct.object = timeSlot;
        // after add, needs reload to display it
        mvpView.addTimeslot(struct);
        mvpView.reloadTimeslot();
    }



    public FlexibleLenViewBody.OnTimeSlotListener onWeekOuterListener() {
        return new FlexibleLenViewBody.OnTimeSlotListener() {
            @Override
            public void onTimeSlotCreate(TimeSlotView timeSlotView) {
                if (mvpView.isClickTSConfirm() && EventUtil.isUserHostOfEvent(eventDetailHostEvent)) {
                    // is host and create timeslot as confirmed
                    createTimeslotInStatus(timeSlotView, getContext().getString(R.string.accepted));
                } else if (EventUtil.isUserHostOfEvent(eventDetailHostEvent)) {
                    // is host, and create timeslot as pending
                    createTimeslotInStatus(timeSlotView, getContext().getString(R.string.timeslot_status_create));
                }
            }

            // TODO: 11/12/2016 check this method, see if it is right
            @Override
            public void onTimeSlotClick(TimeSlotView timeSlotView) {
                if (EventUtil.isUserHostOfEvent(eventDetailHostEvent)) {
                    onHostClickTimeslotView(timeSlotView);
                }else{
                    // user is as invitee
                   mvpView.onClickTimeSlotView(timeSlotView);
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
                if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getUserUid())) {
                    // host:
                    WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct) timeSlotView.getTag();
                    struct.startTime = startTime;
                    struct.endTime = endTime;
                    if (presenter.getView() != null) {
                        presenter.getView().reloadTimeslot();
                    }

                    Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct) timeSlotView.getTag()).object;
                    Timeslot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
                    if (timeSlot != null) {
                        timeSlot.setStartTime(timeSlotView.getStartTimeM());
                        timeSlot.setEndTime(timeSlotView.getEndTimeM());
                    }
                } else {
                    // do nothing
                }
            }
        };
    }

    private void onHostClickTimeslotView(TimeSlotView timeslotView){
        Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct) timeslotView.getTag()).object;
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
        if (mvpView.isClickTSConfirm()) {
            // click timeslot then status become accepted and isconfirm = 1, from GroupDetailFragment
            if (timeSlot != null) {
                if (timeSlot.getStatus().equals("pending")) {
                    mvpView.popupTimeSlotWindow(timeslotView);
                } else {
                    mvpView.onClickTimeSlotView(timeslotView);
                }
            }
        } else {
            // click timeslot then status from pending to accept, from EditFragment
            mvpView.onClickTimeSlotView(timeslotView);
        }
    }



    public View.OnClickListener onBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView != null) {
                    mvpView.onClickBack();
                }
            }
        };
    }

    public View.OnClickListener onDone() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if from editTimeSlotFragment
//                if (mvpView.isFromEditFragment() || mvpView.isFromInviteeFragment()) {
//                    List<Timeslot> timeslotList = EventUtil.getTimeslotFromStatus(eventDetailHostEvent, getContext().getString(R.string.pending));
//                    eventDetailHostEvent.setTimeslot(timeslotList);
//                }

                if (mvpView != null) {
                    mvpView.onClickDone(eventDetailHostEvent);
                }
            }
        };
    }

    ////    *****************************************************************************************
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Context getContext() {
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

    public void initTimeslotsFromEditFragment(WeekView weekView){
        weekView.resetTimeSlots();
        if (eventDetailHostEvent.hasTimeslots()){
            for (Timeslot timeslot : eventDetailHostEvent.getTimeslot()){
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeslot.getStartTime();
                struct.endTime = timeslot.getEndTime();
                struct.object = timeslot;
                if (timeslot.getStatus().equals(getContext().getString(R.string.timeslot_status_create))){
                    struct.status = false;
                }else if (timeslot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))){
                    struct.status = true;
                }
                weekView.addTimeSlot(struct);
            }
        }
        final WeekView wv = weekView;
        weekView.postDelayed(new Runnable() {
            @Override
            public void run() {
                wv.reloadTimeSlots(false);
            }
        }, 100);
    }

    public void initTimeslotsFromInviteeFragment(WeekView weekView){
        initTimeslotsFromEditFragment(weekView);
    }

    public void initTimeslotsFromDetailFragment(WeekView weekView){
        weekView.resetTimeSlots();
        if (eventDetailHostEvent.hasTimeslots()){
            for (Timeslot timeslot : eventDetailHostEvent.getTimeslot()){
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeslot.getStartTime();
                struct.endTime = timeslot.getEndTime();
                struct.object = timeslot;
                setTimeslotFromDetailFragment(timeslot, struct);
                weekView.addTimeSlot(struct);
            }
        }
        final WeekView wv = weekView;
        weekView.postDelayed(new Runnable() {
            @Override
            public void run() {
                wv.reloadTimeSlots(false);
            }
        }, 100);
    }

    private void setTimeslotFromDetailFragment(Timeslot timeslot, WeekView.TimeSlotStruct struct){
        if (EventUtil.isUserHostOfEvent(eventDetailHostEvent)){
            // user is host
            if (timeslot.getIsConfirmed()==1){
                struct.status = true;
            }else if (timeslot.getIsConfirmed()==0){
                struct.status = false;
            }
        }else{
            // user is invitee
            if (timeslot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))){
                struct.status = false;
            }else if (timeslot.getStatus().equals(getContext().getString(R.string.timeslot_status_accept))){
                struct.status = true;
            }
        }
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
