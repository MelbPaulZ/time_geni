package org.unimelb.itime.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNewBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageNewEvent;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends MvpFragment<EventCreateNewMvpView, EventCreateNewPresenter> implements EventCreateNewMvpView,FragmentTagListener{

    private FragmentEventCreateNewBinding binding;
    private EventCreateNewVIewModel eventCreateNewVIewModel;
    private Event event;
    private String tag;

    @Override
    public EventCreateNewPresenter createPresenter() {
        return new EventCreateNewPresenter(getContext());
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
        event = new Event();
        eventCreateNewVIewModel = new EventCreateNewVIewModel(getPresenter(), event);
        binding.setEventVM(eventCreateNewVIewModel);

        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
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

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
