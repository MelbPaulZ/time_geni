package org.unimelb.itime.ui.fragment.eventcreate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNewBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageEventTime;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends MvpFragment<EventCreateNewMvpView, EventCreateNewPresenter> implements EventCreateNewMvpView,FragmentTagListener{

    private FragmentEventCreateNewBinding binding;
    private EventCreateNewVIewModel eventCreateNewVIewModel;
    private Event event;
    private String tag;
    private EventCreateNewPresenter presenter;
    private int year,month,day,hour,minute;

    private final int ACTIVITY_PHOTOPICKER = 1;

    @Override
    public EventCreateNewPresenter createPresenter() {
        if (presenter==null)
            return new EventCreateNewPresenter(getContext());
        else
            return presenter;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_event_create_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (event == null) {
            event = new Event();
//            event.setEventUid(EventUtil.generateUid());
        }
        if (eventCreateNewVIewModel==null) {
            eventCreateNewVIewModel = new EventCreateNewVIewModel(getPresenter());
            eventCreateNewVIewModel.setEvent(event);
        }
        event.setEventUid(EventUtil.generateUid());
        event.setHostUserUid(UserUtil.getUserUid());
        binding.setEventVM(eventCreateNewVIewModel);

        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    public void setStartTime(long time){
        if (event == null) {
            event = new Event();
        }
        event.setStartTime(time);
    }

    public void setEndTime(long time){
        if (event == null){
            event = new Event();
        }
        event.setEndTime(time);
    }

    public void setPhotos(ArrayList<String> photos){
        eventCreateNewVIewModel.setPhotos(photos);
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void pickDate(String tag) {
        ((EventCreateActivity)getActivity()).toDatePicker(this, tag);
    }

    @Override
    public void gotoWeekViewCalendar() {
        ((EventCreateActivity)getActivity()).toWeekViewCalendar(this);
    }

    @Override
    public void pickLocatioin(String tag) {
        ((EventCreateActivity)getActivity()).toLocationPicker(this, tag);
    }

    @Override
    public void pickAttendee() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.new_event), event);
        ((EventCreateActivity)getActivity()).toAttendeePicker(this,bundle);
    }

    @Override
    public void toCreateSoloEvent(Event event) {
        ((EventCreateActivity)getActivity()).createSoloEvent(event);
    }

    public void pickPhoto(){
        ((EventCreateActivity)getActivity()).checkPermission();
    }


    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Subscribe
    public void getDateChange(MessageEventDate messageEventDate){
        if (messageEventDate.tag == getString(R.string.tag_start_time)){
            year = messageEventDate.year;
            month = messageEventDate.month;
            day = messageEventDate.day;
        }else if (messageEventDate.tag == getString(R.string.tag_end_time)){
            year = messageEventDate.year;
            month = messageEventDate.month;
            day = messageEventDate.day;
        }else if (messageEventDate.tag == getString(R.string.tag_end_repeat)){
            Calendar calendar = Calendar.getInstance();
            calendar.set(messageEventDate.year, messageEventDate.month, messageEventDate.day);
            event.setRepeatEndsTime(calendar.getTimeInMillis());
            eventCreateNewVIewModel.setEvent(event);
        }
    }

    @Subscribe
    public void getTimeChange(MessageEventTime messageEventTime){
        Calendar calendar = Calendar.getInstance();
        hour = messageEventTime.hour;
        minute = messageEventTime.minute;
        calendar.set( year, month, day, hour, minute);
        if (messageEventTime.tag == getString(R.string.tag_start_time)){
            event.setStartTime(calendar.getTimeInMillis());
            eventCreateNewVIewModel.setEvent(event);
        }else if (messageEventTime.tag == getString(R.string.tag_end_time)){
            event.setEndTime(calendar.getTimeInMillis());
            eventCreateNewVIewModel.setEvent(event);
        }
    }

    @Subscribe
    public void getLocationChange(MessageLocation messageLocation){
        if (messageLocation.tag == getString(R.string.tag_create_event)){
        event.setLocation(messageLocation.locationString);
        eventCreateNewVIewModel.setEvent(event);
        }
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
