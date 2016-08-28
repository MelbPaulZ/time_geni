package org.unimelb.itime.ui.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;
import org.unimelb.itime.vendor.eventview.Event;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.timeslotview.WeekTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;

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


//        ******************************simulate data
        WeekTimeSlotView weekTimeSlotView = (WeekTimeSlotView) binding.getRoot().findViewById(R.id.week_time_slot_view);
        // simulate timeSlots
        ArrayList<Long> simulateTimeSlots = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,29);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);
        simulateTimeSlots.add(calendar.getTime().getTime());
        weekTimeSlotView.setTimeSlots(simulateTimeSlots,60);

        // simulate Events
        ArrayList<ITimeEventInterface> eventArrayList = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH,30);
        calendar1.set(Calendar.HOUR_OF_DAY, 3);
        calendar1.set(Calendar.MINUTE,0);
        Event event = new Event();
        event.setTitle("itime meeting");
        event.setStartTime(calendar1.getTimeInMillis());
        event.setEndTime(calendar1.getTimeInMillis() + 3600000*2);
        event.setEventType(Event.Type.GROUP);
        event.setStatus(Event.Status.COMFIRM);

        eventArrayList.add((ITimeEventInterface) event);
        weekTimeSlotView.setEvent(eventArrayList);
//




        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventCreateTimeslotViewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(eventCreateTimeslotViewModel);
    }

    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext());
    }
}
