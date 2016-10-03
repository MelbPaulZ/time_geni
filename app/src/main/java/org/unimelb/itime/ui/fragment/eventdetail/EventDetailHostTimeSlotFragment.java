package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventDetailTimeslotHostViewBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventDetailHostTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailHostTimeSlotViewModel;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailHostTimeSlotFragment extends MvpFragment<EventDetailHostTimeSlotMvpVIew, EventDetailHostTimeSlotPresenter>
        implements EventDetailHostTimeSlotMvpVIew, FragmentTagListener{
    private String tag;
    private FragmentEventDetailTimeslotHostViewBinding binding;
    private EventDetailHostTimeSlotViewModel viewModel;
    private Event event;
    private WeekView weekView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_timeslot_host_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventDetailHostTimeSlotViewModel(presenter);
        viewModel.setTag(tag);
        viewModel.setEventDetailHostEvent(event);
        binding.setTimeSlotHostVM(viewModel);

        weekView = (WeekView) binding.getRoot().findViewById(R.id.edit_timeslot_weekview);
        weekView.enableTimeSlot();
        weekView.setEventClassName(Event.class);
        weekView.setDayEventMap(EventManager.getInstance().getEventsMap());
        initTimeSlots();
    }

    public void initTimeSlots(){
        if (event.hasTimeslots()) {
            for (TimeSlot timeSlot : event.getTimeslots()) {
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                weekView.addTimeSlot(struct);
            }
        }
    }
//
    @Override
    public void setTag(String tag) {
        this.tag = tag;
        if (viewModel!=null){
            viewModel.setTag(tag);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
//
    public Event getEvent() {
        return event;
    }
//
    public void setEvent(Event event) {
        this.event = event;
        if (viewModel!=null){
            viewModel.setEventDetailHostEvent(event);
        }

    }


    @Override
    public EventDetailHostTimeSlotPresenter createPresenter() {
        return new EventDetailHostTimeSlotPresenter(getContext());
    }

    @Override
    public void toHostEventDetail(Event event) {
        ((EventDetailActivity)getActivity()).toHostEventDetail(event, this);
    }

    @Override
    public void toHostEventEdit(Event event) {
        ((EventDetailActivity)getActivity()).fromTimeSlotToHostEdit(event,this);
    }
}
