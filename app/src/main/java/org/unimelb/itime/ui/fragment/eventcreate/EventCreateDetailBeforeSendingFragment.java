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
import android.widget.ArrayAdapter;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventCreateBeforeSendingBinding;
import org.unimelb.itime.messageevent.MessageEventDate;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCreateDetailBeforeSendingPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingFragment extends BaseUiFragment<EventCreateDetailBeforeSendingMvpView, EventCreateDetailBeforeSendingPresenter> implements EventCreateDetailBeforeSendingMvpView{
    private FragmentEventCreateBeforeSendingBinding binding;
    private EventCreateDetailBeforeSendingViewModel eventCreateDetailBeforeSendingViewModel;
    private Event event;
    private EventCreateDetailBeforeSendingFragment self;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_before_sending, container, false);
        self = this;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventCreateDetailBeforeSendingViewModel = new EventCreateDetailBeforeSendingViewModel(getPresenter());
        binding.setNewEventDetailVM(eventCreateDetailBeforeSendingViewModel);
        if (event!=null){
            eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
        }
    }

    public void setTimeSlotListView(){
        ArrayList<String> timeslotsArrayList = new ArrayList<>();
        for (Timeslot timeSlot: event.getTimeslot()){
            // only display chosen timeSlot
            if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_pending)))
                timeslotsArrayList.add(EventUtil.getSuggestTimeStringFromLong(getContext(), timeSlot.getStartTime(), timeSlot.getEndTime()) );
        }
        ArrayAdapter timeslotAdapter = new ArrayAdapter<String>(getContext(), R.layout.timeslot_listview_show, R.id.timeslot_listview_text, timeslotsArrayList);
        binding.beforeSendingListview.setAdapter(timeslotAdapter);
    }

    public void setPhotos(ArrayList<String> photos){
        eventCreateDetailBeforeSendingViewModel.setPhotos(photos);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

    }

    @Override
    public void onEnter() {
        super.onEnter();
        EditText editText = (EditText) binding.getRoot().findViewById(R.id.before_sending_title);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onLeave() {
        super.onLeave();
        EditText editText = (EditText) binding.getRoot().findViewById(R.id.before_sending_title);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    @Subscribe
    public void getLocationChange(MessageLocation messageLocation){
        if (messageLocation.tag.equals(getClassName())){
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

    @Subscribe
    public void getinvitees(MessageInvitees messageInvitees){
        if (messageInvitees.tag == getString(R.string.tag_create_event_before_sending)){
            event.setInvitee(messageInvitees.invitees);
            eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
        }
    }

    @Override
    public EventCreateDetailBeforeSendingPresenter createPresenter() {
        return new EventCreateDetailBeforeSendingPresenter(getContext());
    }


    public void setEvent(Event event) {
        this.event = event;
        setTimeSlotListView();
        eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
    }

    @Override
    public void onClickSend() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCancel() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void changeLocation() {
        EventLocationPickerFragment eventLocationPickerFragment = (EventLocationPickerFragment) getFragmentManager().findFragmentByTag(EventLocationPickerFragment.class.getSimpleName());
        eventLocationPickerFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, eventLocationPickerFragment);
    }


    @Override
    public void pickInvitees() {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, inviteeFragment);
    }

    @Override
    public void pickPhoto(String tag) {
        ((EventCreateActivity)getActivity()).checkPermission(tag);
    }

    @Override
    public void onClickProposedTimeslots() {
        EventTimeSlotViewFragment timeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
        switchFragment(this, timeSlotViewFragment);
        timeSlotViewFragment.setTo(this);
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
