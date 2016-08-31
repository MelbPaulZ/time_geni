package org.unimelb.itime.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;

import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.timeslotview.WeekTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventTimeSlotViewFragment extends MvpFragment<EventCreateNewTimeSlotMvpView,EventCreateTimeSlotPresenter>
        implements EventCreateNewTimeSlotMvpView{


    FragmentEventCreateTimeslotViewBinding binding;
    EventCreateTimeslotViewModel eventCreateTimeslotViewModel;

    public EventTimeSlotViewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventCreateTimeslotViewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(eventCreateTimeslotViewModel);
        initData();
    }

    public void initData(){
        //        ******************************simulate data
        WeekTimeSlotView weekTimeSlotView = (WeekTimeSlotView) binding.getRoot().findViewById(R.id.week_time_slot_view);
        // simulate timeSlots
        Map<Long,Boolean> simulateTimeSlots = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,29);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);
        simulateTimeSlots.put(calendar.getTime().getTime(),false);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH,30);
        calendar1.set(Calendar.HOUR_OF_DAY,8);
        calendar1.set(Calendar.MINUTE,45);
        simulateTimeSlots.put(calendar1.getTime().getTime(),true);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 30);
        calendar2.set(Calendar.HOUR_OF_DAY,4);
        calendar2.set(Calendar.MINUTE,0);
        simulateTimeSlots.put(calendar2.getTimeInMillis(),false);

        weekTimeSlotView.setTimeSlots(simulateTimeSlots,60);

        // simulate Events
//        Event event = new Event();
////        Event event = new Event();
//        event.setTitle("itime meeting");
//        event.setStatus(Event.Status.COMFIRM); // 5== pending, 6== confirm
//        event.setEventType(1); //0 == private, 1== group, 2== public
//        Calendar calendar1 =Calendar.getInstance();
//        calendar1.set(Calendar.DAY_OF_MONTH,30);
//        calendar1.set(Calendar.HOUR_OF_DAY,4);
//        calendar1.set(Calendar.MINUTE,15);
//        calendar1.set(Calendar.SECOND,0);
//        event.setStartTime(calendar1.getTimeInMillis());
//        event.setEndTime(calendar1.getTimeInMillis() + 3600000*2);
//
//        WeekView weekView = (WeekView) binding.getRoot().findViewById(R.id.week_view);
//        weekView.setEvent(event);
//
//        weekTimeSlotView.setEvent(event);
//
    }

    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext());
    }
}
