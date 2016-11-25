package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventTimeSlotAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventDetailGroupPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupFragment extends BaseUiFragment<EventDetailGroupMvpView, EventDetailGroupPresenter> implements EventDetailGroupMvpView {
    private org.unimelb.itime.databinding.FragmentEventDetailBinding binding;
    private EventDetailViewModel eventDetailForHostViewModel;
    private Event event;
    private LayoutInflater inflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        this.inflater = inflater;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailForHostViewModel = new EventDetailViewModel(getPresenter());
        if(event == null){
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        eventDetailForHostViewModel.setEvDtlHostEvent(event);
        binding.setHostDetailVM(eventDetailForHostViewModel);
        setProposedTimeSlots(event);
    }

    public void setProposedTimeSlots(Event event){
        // for timeslots, use list view to show
        EventTimeSlotAdapter timeSlotAdapter = new EventTimeSlotAdapter(getContext(), R.layout.listview_timeslot_pick, event.getTimeslot(), eventDetailForHostViewModel);
        timeSlotAdapter.setAdapterEvent(event);
        binding.eventDetailTimeslotListview.setAdapter(timeSlotAdapter);
    }

    public void setEvent(Event event){
        this.event = event;
        if (eventDetailForHostViewModel!=null) {
            eventDetailForHostViewModel.setEvDtlHostEvent(event);
        }
        setProposedTimeSlots(event);
    }

    @Override
    public EventDetailGroupPresenter createPresenter() {
        return new EventDetailGroupPresenter(getContext(),inflater);
    }


    @Override
    public void toCalendar() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void toEditEvent() {
        EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
        eventEditFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        EventManager.getInstance().setCurrentEvent(event);
        switchFragment(this, eventEditFragment);
    }

    @Override
    public void viewInCalendar() {
        EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
        timeSlotFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this,timeSlotFragment);
    }

    @Override
    public void viewInviteeResponse(Timeslot timeSlot) {
        InviteeTimeslotFragment inviteeTimeslotFragment = (InviteeTimeslotFragment) getFragmentManager().findFragmentByTag(InviteeTimeslotFragment.class.getSimpleName());
        switchFragment(this, inviteeTimeslotFragment);
    }


}
