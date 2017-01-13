package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import org.unimelb.itime.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.TimeSlotController;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailTimeSlotViewModel extends CommonViewModel {
    private TimeslotPresenter<EventDetailTimeSlotMvpVIew> presenter;
    private Event eventDetailHostEvent;
    private String hostToolBarString;
    private EventDetailTimeSlotMvpVIew mvpView;


    //
    public EventDetailTimeSlotViewModel(TimeslotPresenter<EventDetailTimeSlotMvpVIew> presenter) {
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

    private void createTimeslotInStatus(DraggableTimeSlotView timeSlotView, String status) {
        Timeslot timeSlot = new Timeslot();
        timeSlot.setTimeslotUid(AppUtil.generateUuid());
        timeSlot.setEventUid(eventDetailHostEvent.getEventUid());
        timeSlot.setStartTime(timeSlotView.getNewStartTime());
        timeSlot.setEndTime(timeSlotView.getNewEndTime());
        timeSlot.setStatus(status);
        timeSlot.setUserUid(UserUtil.getInstance(getContext()).getUserUid());
        eventDetailHostEvent.getTimeslot().add(timeSlot);
        // after add, needs reload to display it
        mvpView.addTimeslot(timeSlot);
        mvpView.reloadTimeslot();
    }



    public TimeSlotController.OnTimeSlotListener onWeekOuterListener() {
        return new TimeSlotController.OnTimeSlotListener() {
            @Override
            public void onTimeSlotCreate(DraggableTimeSlotView timeSlotView) {
                if (mvpView.isClickTSConfirm() && EventUtil.isUserHostOfEvent(getContext(),eventDetailHostEvent)) {
                    // is host and create timeslot as confirmed
                    createTimeslotInStatus(timeSlotView, Timeslot.STATUS_ACCEPTED);
                } else if (EventUtil.isUserHostOfEvent(getContext(), eventDetailHostEvent)) {
                    // is host, and create timeslot as pending
                    createTimeslotInStatus(timeSlotView, Timeslot.STATUS_CREATING);
                }
            }

            // TODO: 11/12/2016 check this method, see if it is right
            @Override
            public void onTimeSlotClick(DraggableTimeSlotView timeSlotView) {
                if (EventUtil.isUserHostOfEvent(getContext(), eventDetailHostEvent)) {
                    onHostClickTimeslotView(timeSlotView);
                }else{
                    // user is as invitee
                   mvpView.onClickTimeSlotView(timeSlotView);
                }
            }

            @Override
            public void onTimeSlotDragStart(DraggableTimeSlotView timeSlotView) {

            }

            @Override
            public void onTimeSlotDragging(DraggableTimeSlotView timeSlotView, int i, int i1) {

            }

            @Override
            public void onTimeSlotDragDrop(DraggableTimeSlotView timeSlotView, long startTime, long endTime) {
                if (eventDetailHostEvent.getHostUserUid().equals(UserUtil.getInstance(getContext()).getUserUid())) {
                    // host:
                    if (presenter.getView() != null) {
                        presenter.getView().reloadTimeslot();
                    }

                    Timeslot calendarTimeSlot = (Timeslot) timeSlotView.getTimeslot();
                    Timeslot timeSlot = TimeSlotUtil.getTimeSlot(eventDetailHostEvent, calendarTimeSlot);
                    if (timeSlot != null) {
                        timeSlot.setStartTime(timeSlotView.getNewStartTime());
                        timeSlot.setEndTime(timeSlotView.getNewEndTime());
                    }
                } else {
                    // do nothing
                }
            }
        };
    }

    private void onHostClickTimeslotView(DraggableTimeSlotView timeslotView){
        Timeslot calendarTimeSlot = (Timeslot) timeslotView.getTimeslot();
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


    public void onClickBack(){
        if (mvpView != null) {
            mvpView.onClickBack();
        }
    }

    public void onClickDone(){
        if (mvpView != null) {
            mvpView.onClickDone(eventDetailHostEvent);
        }
    }


    ////    *****************************************************************************************

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

                if (timeslot.getStatus().equals(Timeslot.STATUS_CREATING)){
                    weekView.addTimeSlot(timeslot,false);
                }else if (timeslot.getStatus().equals(Timeslot.STATUS_PENDING)){
                    weekView.addTimeSlot(timeslot,true);
                }
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
                weekView.addTimeSlot(timeslot,getTimeslotIsSelected(timeslot));
            }
        }
        weekView.reloadTimeSlots(false);
    }

    private boolean getTimeslotIsSelected(Timeslot timeslot){
        if (EventUtil.isUserHostOfEvent(getContext(), eventDetailHostEvent)){
            // user is host
            if (timeslot.getIsConfirmed()==1){
                return true;
            }else if (timeslot.getIsConfirmed()==0){
                return false;
            }
        }else{
            // user is invitee
            if (timeslot.getStatus().equals(Timeslot.STATUS_PENDING)){
                return false;
            }else if (timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED)){
                return true;
            }
        }

        return false;
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
