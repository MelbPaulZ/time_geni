package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

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
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateDetailBeforeSendingMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateDetailBeforeSendingViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 31/08/2016.
 */
public class EventCreateDetailBeforeSendingFragment extends EventBaseFragment<EventCreateDetailBeforeSendingMvpView, EventCommonPresenter<EventCreateDetailBeforeSendingMvpView>> implements EventCreateDetailBeforeSendingMvpView{
    private FragmentEventCreateBeforeSendingBinding binding;
    private EventCreateDetailBeforeSendingViewModel eventCreateDetailBeforeSendingViewModel;
    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (event==null){
            event = EventManager.getInstance(getContext()).getCurrentEvent();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_before_sending, container, false);
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
            if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING))
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
    public EventCommonPresenter<EventCreateDetailBeforeSendingMvpView> createPresenter() {
        return new EventCommonPresenter<>(getContext());
    }


    public void setEvent(Event event) {
        this.event = event;
        setTimeSlotListView();
        eventCreateDetailBeforeSendingViewModel.setNewEvDtlEvent(event);
    }

    @Override
    public void onClickSend() {
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onClickCancel() {
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_CANCELED, intent);
        getActivity().finish();
    }

    @Override
    public void changeLocation() {
        EventLocationPickerFragment eventLocationPickerFragment = (EventLocationPickerFragment) getFragmentManager().findFragmentByTag(EventLocationPickerFragment.class.getSimpleName());
        eventLocationPickerFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event));
        openFragment(this, eventLocationPickerFragment);
    }


    @Override
    public void pickInvitees() {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event));
        openFragment(this, inviteeFragment);
    }

    @Override
    public void pickPhoto() {
        ((EventCreateActivity)getActivity()).checkPermission(getString(R.string.tag_create_event_before_sending));
    }

    @Override
    public void onClickProposedTimeslots() {
        EventTimeSlotViewFragment timeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
        timeSlotViewFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event));
        openFragment(this, timeSlotViewFragment);
        timeSlotViewFragment.setTo(this); // this need to be set manully,coz has to be distinguish with others
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
        AppUtil.showProgressBar(getContext(), "Waiting", "Please waiting");
    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {
        AppUtil.hideProgressBar();
        showDialog("Error", errorMsg);
    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {
        AppUtil.hideProgressBar();
        onClickSend();
    }

    @Override
    public void setLeftTitleStringToVM() {
        eventCreateDetailBeforeSendingViewModel.setLeftTitleStr(getContext().getString(R.string.cancel));
    }

    @Override
    public void setTitleStringToVM() {
        eventCreateDetailBeforeSendingViewModel.setTitleStr(getContext().getString(R.string.new_event));
    }

    @Override
    public void setRightTitleStringToVM() {
        eventCreateDetailBeforeSendingViewModel.setRightTitleStr(getContext().getString(R.string.send));
    }
}
