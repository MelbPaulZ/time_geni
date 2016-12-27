package org.unimelb.itime.ui.fragment.event;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TimePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateNewBinding;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 23/08/2016.
 */
public class EventCreateNewFragment extends BaseUiFragment<EventCreateNewMvpView, EventCommonPresenter<EventCreateNewMvpView>> implements EventCreateNewMvpView, TimePickerDialog.OnTimeSetListener{

    private final static String TAG = "EventCreateNewFragment";
    private FragmentEventCreateNewBinding binding;
    private EventCreateNewVIewModel viewModel;
    private Event event;

    private TimePickerDialog timePickerDialog;

    @Override
    public EventCommonPresenter<EventCreateNewMvpView> createPresenter() {
            return new EventCommonPresenter<>(getContext());
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
        event = EventManager.getInstance(getContext()).getCurrentEvent();
        viewModel = new EventCreateNewVIewModel(getPresenter());
        binding.setEventVM(viewModel);
        onEnter(); // show key board
    }

    public void showTimePicker(){
        this.timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

    }

    public void showDatePicker(){

    }

    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    @Override
    public void onEnter() {
        super.onEnter();
        EditText editText = (EditText) binding.getRoot().findViewById(R.id.create_event_title_edittext);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onLeave() {
        super.onLeave();
        EditText editText = (EditText) binding.getRoot().findViewById(R.id.create_event_title_edittext);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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
        locationPickerFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event));
        openFragment(this, locationPickerFragment);
    }

    @Override
    public void pickInvitee() {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event));
        openFragment(this, inviteeFragment);
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
            EventManager.getInstance(getContext()).getCurrentEvent().setLocation(messageLocation.locationString);
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

    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {

    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {

    }
}
