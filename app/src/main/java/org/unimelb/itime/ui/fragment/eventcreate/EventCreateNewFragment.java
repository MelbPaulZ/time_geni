package org.unimelb.itime.ui.fragment.eventcreate;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNewBinding;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageEventTime;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EmptyPresenter;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends BaseUiFragment<EventCreateNewMvpView, EventCreateNewPresenter> implements EventCreateNewMvpView{

    private FragmentEventCreateNewBinding binding;
    private EventCreateNewVIewModel viewModel;
    private Event event;
//    private String tag;
    private EventCreateNewPresenter presenter;
    private int year,month,day,hour,minute;

    private final int ACTIVITY_PHOTOPICKER = 1;

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
        if (viewModel ==null) {
            viewModel = new EventCreateNewVIewModel(getPresenter());
        }
        binding.setEventVM(viewModel);
        event = EventManager.getInstance().getCurrentEvent();
        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        
    }

    public void setPhotos(ArrayList<String> photos){
        viewModel.setPhotos(photos);
    }


    @Override
    public void gotoWeekViewCalendar() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void pickLocation() {
        EventLocationPickerFragment locationPickerFragment = (EventLocationPickerFragment) getFragmentManager().findFragmentByTag(EventLocationPickerFragment.class.getSimpleName());
        switchFragment(this, locationPickerFragment);
    }

    @Override
    public void pickInvitee() {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, inviteeFragment);
    }

    @Override
    public void toCreateSoloEvent() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void pickPhoto(String tag){
        ((EventCreateActivity)getActivity()).checkPermission(tag);
    }


    @Subscribe
    public void getLocationChange(MessageLocation messageLocation){
        if (messageLocation.tag.equals(this.getClassName())){
            event.setLocation(messageLocation.locationString);
            EventManager.getInstance().getCurrentEvent().setLocation(messageLocation.locationString);
            viewModel.setEvent(event);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            viewModel.setEvent(EventManager.getInstance().getCurrentEvent());
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
