package org.unimelb.itime.ui.fragment.eventcreate;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventCreateBeforeSendingBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingFragment extends MvpFragment<EventCreateDetailBeforeSendingMvpView, EventCreateDetailBeforeSendingPresenter> implements EventCreateDetailBeforeSendingMvpView,FragmentTagListener{
    private FragmentEventCreateBeforeSendingBinding binding;
    private EventCreateDetailBeforeSendingViewModel eventCreateDetailBeforeSendingViewModel;
    private Event event;
    private EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment;
    private String tag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_before_sending, container, false);
        eventCreateDetailBeforeSendingFragment = this;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle==null){
            this.event = new Event();
        }else{
            if (bundle.containsKey(getString(R.string.new_event))) {
                this.event = (Event) bundle.getSerializable(getString(R.string.new_event));
            }
        }
        eventCreateDetailBeforeSendingViewModel = new EventCreateDetailBeforeSendingViewModel(getPresenter(),this.event);
        binding.setNewEventDetailVM(eventCreateDetailBeforeSendingViewModel);

        //set timeslot listview
        ArrayList<String> timeslotsArrayList = new ArrayList<>();
        for (TimeSlot timeSlot: event.getTimeslots()){
            // only display chosed timeslot
            if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_pending)))
                timeslotsArrayList.add(EventUtil.getSuggestTimeStringFromLong(getContext(), timeSlot.getStartTime(), timeSlot.getEndTime()) );
        }

        ArrayAdapter timeslotAdapter = new ArrayAdapter<String>(getContext(), R.layout.timeslot_listview_show, R.id.timeslot_listview_text, timeslotsArrayList);

        binding.beforeSendingListview.setAdapter(timeslotAdapter);

        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

    }



    @Subscribe
    public void getLocationChange(MessageLocation messageLocation){
        if (messageLocation.tag == getString(R.string.tag_create_event_before_sending)){
            event.setLocation(messageLocation.locationString);
            eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
        }
    }

    @Subscribe
    public void getEndRepeatDate(MessageEventDate messageEventDate){
        if (messageEventDate.tag == getString(R.string.tag_create_event_before_sending)){
            Calendar calendar = Calendar.getInstance();
            calendar.set(messageEventDate.year, messageEventDate.month, messageEventDate.day);
            event.setRepeatEndsTime(calendar.getTimeInMillis());
            eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public EventCreateDetailBeforeSendingPresenter createPresenter() {
        return new EventCreateDetailBeforeSendingPresenter(getContext());
    }


    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void sendEvent(Event event) {
        ((EventCreateActivity)getActivity()).sendEvent(event);
    }


    // this is for review timeslot or click back to rechoose timeslot
    @Override
    public void backToTimeSlotView() {
        ((EventCreateActivity)getActivity()).goBackToTimeSlot(eventCreateDetailBeforeSendingFragment);
    }

    @Override
    public void changeLocation(String tag) {
        ((EventCreateActivity)getActivity()).toLocationPicker(this,tag);
    }

    @Override
    public void changeEndRepeatDate(String tag) {
        ((EventCreateActivity)getActivity()).toDatePicker(this,tag);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}