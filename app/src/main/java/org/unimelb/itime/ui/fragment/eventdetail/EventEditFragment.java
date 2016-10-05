package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.presenter.EventEditPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends MvpFragment<EventEditMvpView, EventEditPresenter> implements EventEditMvpView {

    private FragmentEventEditDetailBinding binding;
    private EventEditViewModel eventEditViewModel;
    private Event event;
    private String tag;

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
        tag = getString(R.string.tag_edit_event);

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

    }

    public void setPhotos(ArrayList<String> photos){
        eventEditViewModel.setPhotos(photos);
    }

    @Override
    public void toHostEventDetail(Event event) {
        ((EventDetailActivity)getActivity()).toEventDetail(event);
    }

    @Override
    public void changeLocation() {
        ((EventDetailActivity)getActivity()).toLocationPicker(tag,this);
    }

    @Override
    public void toTimeSlotView(String tag, Event event) {
        ((EventDetailActivity)getActivity()).fromHostEditToTimeSlotView(tag, event,this);
    }

    @Override
    public void toInviteePicker(String tag) {
        ((EventDetailActivity)getActivity()).toInviteePicker(tag, this);
    }


    @Override
    public void toPhotoPicker(String tag) {
        ((EventDetailActivity)getActivity()).toPhotoPicker();
    }

    @Override
    public void toSoloEventDetail() {
        ((EventDetailActivity)getActivity()).toDetailSoloEvent(event);
    }

    @Override
    public void toSoloEventDetail(Event event) {
        ((EventDetailActivity)getActivity()).toDetailSoloEvent(event);
    }

//    @Override
//    public void setTag(String tag) {
//        this.tag = tag;
//    }

    @Subscribe
    public void getLocation(MessageLocation messageLocation){
        if (messageLocation.tag.equals(tag)){
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
