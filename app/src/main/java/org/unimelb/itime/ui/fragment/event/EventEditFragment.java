package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends EventBaseFragment<EventEditMvpView, EventCommonPresenter<EventEditMvpView>> implements EventEditMvpView {

    private static final String TAG = "EdifFragment";
    private FragmentEventEditDetailBinding binding;
    private EventEditViewModel eventEditViewModel;
    private Event event;
    private EventManager eventManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventCommonPresenter<EventEditMvpView> createPresenter() {
        EventCommonPresenter<EventEditMvpView> presenter = new EventCommonPresenter<>(getContext());
        return presenter;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        if (event==null) {
            event = eventManager.copyCurrentEvent(EventManager.getInstance(getContext()).getCurrentEvent());
        }
        eventEditViewModel.setEventEditViewEvent(event);
        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if ( getFrom() instanceof EventDetailFragment){
            EditText editText = (EditText) binding.getRoot().findViewById(R.id.edit_event_title);
            editText.setFocusable(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onLeave() {
        super.onLeave();
        EditText editText = (EditText) binding.getRoot().findViewById(R.id.edit_event_title);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void setEvent(Event event){
        this.event = event;
        if (eventEditViewModel!=null){
            eventEditViewModel.setEventEditViewEvent(event);
        }
    }

    public void setPhotos(ArrayList<String> photos){
        eventEditViewModel.setPhotos(photos);
    }

    @Override
    public void toHostEventDetail() {
        EventDetailFragment detailFragment = (EventDetailFragment) getFragmentManager().findFragmentByTag(EventDetailFragment.class.getSimpleName());
        closeFragment(this, detailFragment);
    }

    @Override
    public void changeLocation() {
        EventLocationPickerFragment eventLocationPickerFragment = (EventLocationPickerFragment) getFragmentManager().findFragmentByTag(EventLocationPickerFragment.class.getSimpleName());
        eventLocationPickerFragment.setEvent(eventManager.copyCurrentEvent(event));
        openFragment(this,eventLocationPickerFragment);
    }

    @Override
    public void toTimeSlotView(Event event) {
        EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
        Event cpyEvent = eventManager.copyCurrentEvent(event);
        Invitee me = EventUtil.getSelfInInvitees(getContext(), cpyEvent);
        // if the user is host, then reset all his timeslot as create
        if (me!=null) {
            for (SlotResponse slotResponse : me.getSlotResponses()) {
                slotResponse.setStatus(Timeslot.STATUS_CREATING);
            }
        }

        timeSlotFragment.setEvent(cpyEvent);
        openFragment(this, timeSlotFragment);
    }


    @Override
    public void toInviteePicker(Event event) {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(eventManager.copyCurrentEvent(event));
        openFragment(this, inviteeFragment);
    }


    @Override
    public void toPhotoPicker() {
        ((EventDetailActivity)getActivity()).checkPermission();
    }

    @Subscribe
    public void getLocation(MessageLocation messageLocation){
        if (messageLocation.tag.equals(EventEditFragment.class.getSimpleName())){
            event.setLocation(messageLocation.locationString);
            eventEditViewModel.setEventEditViewEvent(event);
        }
    }

    @Subscribe
    public void getInvitees(MessageInvitees messageInvitees){
        if (messageInvitees.tag == getString(R.string.tag_host_event_edit)){
            event.setInvitee(messageInvitees.invitees);
            eventEditViewModel.setEventEditViewEvent(event);
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
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {
        Log.i(TAG, "onTaskError: " + errorMsg);
        AppUtil.hideProgressBar();
    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {
        AppUtil.hideProgressBar();
        switch (task){
            case EventCommonPresenter.TASK_EVENT_INSERT:
                break;
            case EventCommonPresenter.TASK_EVENT_UPDATE:{
                toCalendar();
                break;
            }case EventCommonPresenter.TASK_EVENT_DELETE:
                toCalendar();
                break;
        }
    }

    private void toCalendar(){
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.cancel));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.edit_event));
    }

    @Override
    public void setRightTitleStringToVM() {
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
    }

    @Override
    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolbarViewModel() {
        return new ToolbarViewModel<>(this);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}
