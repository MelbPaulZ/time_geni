package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends BaseUiFragment<EventEditMvpView, EventEditPresenter> implements EventEditMvpView {

    private FragmentEventEditDetailBinding binding;
    private EventEditViewModel eventEditViewModel;
    private Event event;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventEditPresenter createPresenter() {
        EventEditPresenter presenter = new EventEditPresenter(getContext());
        presenter.setOnUpdateEvent(new CommonPresenter.OnUpdateEvent() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<Event> eventHttpResult) {
                Event ev = eventHttpResult.getData();
                if (ev.getEventType().equals(getContext().getString(R.string.group))) {
                    toHostEventDetail(ev);
                }else{
                    toSoloEventDetail(ev);
                }
            }
        });
        return presenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventEditViewModel = new EventEditViewModel(getPresenter());
        if (event==null) {
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        eventEditViewModel.setEventEditViewEvent(event);
        binding.setEventEditVM(eventEditViewModel);
        setProposedTimeSlots(event);
    }

    // todo try databinding to bind the edit text
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden){
//            // pupup keyboard for title
//            if (getFrom() instanceof EventDetailSoloFragment || getFrom() instanceof EventDetailGroupFragment){
//                Log.i("show edit text", "onHiddenChanged: " + "show keyboard");
//                EditText editText = (EditText) binding.getRoot().findViewById(R.id.edit_event_title);
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
//            }
//        }
//    }


    @Override
    public void onHiddenChanged(boolean hidden) {
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if (getFrom() instanceof EventDetailSoloFragment || getFrom() instanceof EventDetailGroupFragment){
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

    public void setProposedTimeSlots(Event event){
        if (event.hasTimeslots()){
            ArrayList<String> timeslotArrayList = new ArrayList<>();
            for (Timeslot timeSlot: event.getTimeslot()){
                timeslotArrayList.add(EventUtil.getSuggestTimeStringFromLong(getContext(), timeSlot.getStartTime(), timeSlot.getEndTime()));
            }
            ArrayAdapter stringAdapter = new ArrayAdapter(getContext(), R.layout.timeslot_listview_show, R.id.timeslot_listview_text, timeslotArrayList);
            binding.eventEditListview.setAdapter(stringAdapter);
        }
    }

    public void setEvent(Event event){
        this.event = event;
        if (eventEditViewModel!=null){
            eventEditViewModel.setEventEditViewEvent(event);
        }
        setProposedTimeSlots(event);
    }

    public void setPhotos(ArrayList<String> photos){
        eventEditViewModel.setPhotos(photos);
    }

    @Override
    public void toHostEventDetail(Event event) {
        EventDetailGroupFragment hostFragment = (EventDetailGroupFragment) getFragmentManager().findFragmentByTag(EventDetailGroupFragment.class.getSimpleName());
        hostFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, hostFragment);
    }

    @Override
    public void toHostEventDetail() {
        EventDetailGroupFragment hostFragment = (EventDetailGroupFragment) getFragmentManager().findFragmentByTag(EventDetailGroupFragment.class.getSimpleName());
        switchFragment(this, hostFragment);
    }

    @Override
    public void changeLocation() {
        EventLocationPickerFragment eventLocationPickerFragment = (EventLocationPickerFragment) getFragmentManager().findFragmentByTag(EventLocationPickerFragment.class.getSimpleName());
        eventLocationPickerFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this,eventLocationPickerFragment);
    }

    @Override
    public void toTimeSlotView(Event event) {
        EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
        timeSlotFragment.setClickFromFragment(this);
        switchFragment(this, timeSlotFragment);
    }

    @Override
    public void toInviteePicker(Event event) {
        InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
        inviteeFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, inviteeFragment);
    }


    @Override
    public void toPhotoPicker() {
        ((EventDetailActivity)getActivity()).checkPermission();
    }

    @Override
    public void toSoloEventDetail() {
        EventDetailSoloFragment soloFragment = (EventDetailSoloFragment) getFragmentManager().findFragmentByTag(EventDetailSoloFragment.class.getSimpleName());
        switchFragment(this, soloFragment);
    }

    @Override
    public void toSoloEventDetail(Event event) {
        EventDetailSoloFragment soloFragment = (EventDetailSoloFragment) getFragmentManager().findFragmentByTag(EventDetailSoloFragment.class.getSimpleName());
        soloFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, soloFragment);
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
    public void onShowDialog() {
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onHideDialog() {
        AppUtil.hideProgressBar();
    }
}
