package org.unimelb.itime.ui.fragment.eventcreate;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNewBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EventCreateNewPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

import java.util.ArrayList;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends BaseUiFragment<EventCreateNewMvpView, EventCreateNewPresenter> implements EventCreateNewMvpView, TimePickerDialog.OnTimeSetListener{

    private final static String TAG = "EventCreateNewFragment";
    private FragmentEventCreateNewBinding binding;
    private EventCreateNewVIewModel viewModel;
    private Event event;
    private int year,month,day,hour,minute;

    private final int ACTIVITY_PHOTOPICKER = 1;

    private TimePickerDialog timePickerDialog;

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
        event = EventManager.getInstance().getCurrentEvent();
        viewModel = new EventCreateNewVIewModel(getPresenter());
        binding.setEventVM(viewModel);

        // hide soft key board
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        // for time picker , spinner theme
//         timePickerDialog = new TimePickerDialog(getActivity(),
//                TimePickerDialog.THEME_HOLO_LIGHT, null, 11,11,false);
    }

    public void showTimePicker(){
        this.timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

    }

    public void showDatePicker(){

    }




    public void setEvent(Event event){
        this.event = event;
        viewModel.setEvent(event);
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
        locationPickerFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
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
