package org.unimelb.itime.ui.fragment.eventdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.fragment.eventcreate.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
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
        return new EventEditPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEventEditViewEvent(event);
        binding.setEventEditVM(eventEditViewModel);
        if (event==null) {
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        setProposedTimeSlots(event);
    }

    public void setProposedTimeSlots(Event event){
        if (event.hasTimeslots()){
            ArrayList<String> timeslotArrayList = new ArrayList<>();
            for (TimeSlot timeSlot: event.getTimeslots()){
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
        timeSlotFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this, timeSlotFragment);
    }

    @Override
    public void toInviteePicker() {
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
        EventDetailSoloFragment soloFragment = (EventDetailSoloFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
        switchFragment(this, soloFragment);
    }

    @Override
    public void toSoloEventDetail(Event event) {
        EventDetailSoloFragment soloFragment = (EventDetailSoloFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
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

}
