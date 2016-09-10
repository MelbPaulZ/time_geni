package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.BR;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.presenter.EventDetailForHostPresenter;
import org.unimelb.itime.util.TimeSlotUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailForHostViewModel extends BaseObservable {
    private EventDetailForHostPresenter presenter;
    private Event EvDtlHostEvent;

    private boolean isSelectTimeSlot;
//    private boolean[] timeSlotChooseArray = {false, false, false};
//    private ArrayList<View> timeslots = new ArrayList<>();
    private String tag;

    public EventDetailForHostViewModel(EventDetailForHostPresenter presenter) {
        this.presenter = presenter;
        tag = presenter.getContext().getString(R.string.tag_host_event_detail);
    }

    public Context getContext(){
        return presenter.getContext();
    }

    public View.OnClickListener editEvent(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toEditEvent(EvDtlHostEvent);
            }
        };
    }

    private void unSelectRestTimeSlots(int selectTimeSlotIndex){
        for (int i = 0; i < EvDtlHostEvent.getTimeslots().size() ; i++){
            if (i != selectTimeSlotIndex){
                EvDtlHostEvent.getTimeslots().get(i).setStatus(getContext().getString(R.string.timeslot_status_pending));
            }
        }
    }

    public View.OnClickListener onHostTimeSlotSelect1() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(0))){
                    EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_pending));
                }else{
                    if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())) {
                        unSelectRestTimeSlots(0);
                    }
                    EvDtlHostEvent.getTimeslots().get(0).setStatus(getContext().getString(R.string.timeslot_status_accept));
                }
                setEvDtlHostEvent(EvDtlHostEvent);
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect2() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(1))){
                    EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_pending));
                }else{
                    if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())){
                        unSelectRestTimeSlots(1);
                    }
                    EvDtlHostEvent.getTimeslots().get(1).setStatus(getContext().getString(R.string.timeslot_status_accept));
                }
                setEvDtlHostEvent(EvDtlHostEvent);
            }
        };
    }


    public View.OnClickListener onHostTimeSlotSelect3() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TimeSlotUtil.isTimeSlotSelected(getContext(), EvDtlHostEvent.getTimeslots().get(2))){
                    EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_pending));
                }else{
                    if (TimeSlotUtil.chooseAtLeastOnTimeSlot(getContext(), EvDtlHostEvent.getTimeslots())){
                        unSelectRestTimeSlots(2);
                    }
                    EvDtlHostEvent.getTimeslots().get(2).setStatus(getContext().getString(R.string.timeslot_status_accept));
                }
                setEvDtlHostEvent(EvDtlHostEvent);
            }
        };
    }

    public View.OnClickListener viewInCalendar(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.viewInCalendar(tag);
            }
        };
    }

    public View.OnClickListener gotoUrl() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = EvDtlHostEvent.getUrl();
                EventBus.getDefault().post(new MessageUrl(url));
            }
        };
    }

    public View.OnClickListener onClickConfirm() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to save data later, implement later
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener onClickBack(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                presenter.toEventDetailHost(); // wrong
                presenter.toWeekView();
            }
        };
    }

    public View.OnClickListener toAttendeeView1(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(0).getStartTime());
            }
        };
    }

    public View.OnClickListener toAttendeeView2(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(1).getStartTime());
            }
        };
    }

    public View.OnClickListener toAttendeeView3(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                presenter.toAttendeeView(EvDtlHostEvent.getTimeslots().get(2).getStartTime());
            }
        };
    }



//    private boolean isHasSelectAtLeastOneTimeSlot() {
//        for (Boolean timeslotSelect : timeSlotChooseArray) {
//            if (timeslotSelect)
//                return true;
//        }
//        return false;
//    }


    public void setSelectTimeSlot(boolean selectTimeSlot) {
        isSelectTimeSlot = selectTimeSlot;
        notifyPropertyChanged(BR.selectTimeSlot);
    }


//    ***************************************************************

    @Bindable
    public Event getEvDtlHostEvent() {
        return EvDtlHostEvent;
    }

    public void setEvDtlHostEvent(Event evDtlHostEvent) {
        EvDtlHostEvent = evDtlHostEvent;
        notifyPropertyChanged(BR.evDtlHostEvent);
    }

    @Bindable
    public boolean isSelectTimeSlot() {
        return isSelectTimeSlot;
    }

//    public void setTimeSlotChooseArray(boolean[] timeSlotChooseArray) {
//        this.timeSlotChooseArray = timeSlotChooseArray;
//
//    }

}
