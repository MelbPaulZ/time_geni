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
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventDetailForHostBinding;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventDetailGroupPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailHostViewModel;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupFragment extends BaseUiFragment<EventDetailGroupMvpView, EventDetailGroupPresenter> implements EventDetailGroupMvpView {
    private FragmentEventDetailForHostBinding binding;
    private EventDetailHostViewModel eventDetailForHostViewModel;
    private Event event;
    private LayoutInflater inflater;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_for_host, container, false);
        this.inflater = inflater;
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailForHostViewModel = new EventDetailHostViewModel(getPresenter());
        if(event == null){
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        eventDetailForHostViewModel.setEvDtlHostEvent(event);
        binding.setHostDetailVM(eventDetailForHostViewModel);

        // for timeslots, use list view to show
        EventTimeSlotAdapter timeSlotAdapter = new EventTimeSlotAdapter(getContext(), R.layout.listview_timeslot_pick, event.getTimeslots(), eventDetailForHostViewModel);
        timeSlotAdapter.setAdapterEvent(event);
        binding.eventDetailTimeslotListview.setAdapter(timeSlotAdapter);
    }

    public void setEvent(Event event){
        this.event = event;
        if (eventDetailForHostViewModel!=null) {
            eventDetailForHostViewModel.setEvDtlHostEvent(event);
        }
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
        switchFragment(this, eventEditFragment);
    }

    @Override
    public void viewInCalendar() {
        EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
        timeSlotFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event));
        switchFragment(this,timeSlotFragment);
    }

    @Override
    public void viewInviteeResponse(TimeSlot timeSlot) {
        InviteeTimeslotFragment inviteeTimeslotFragment = (InviteeTimeslotFragment) getFragmentManager().findFragmentByTag(InviteeTimeslotFragment.class.getSimpleName());
        switchFragment(this, inviteeTimeslotFragment);
    }


}
