package org.unimelb.itime.ui.fragment.eventdetail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventDetailTimeslotHostViewBinding;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailTimeSlotViewModel;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.weekview.WeekView;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailTimeSlotFragment extends BaseUiFragment<EventDetailTimeSlotMvpVIew, EventDetailHostTimeSlotPresenter>
        implements EventDetailTimeSlotMvpVIew {
    private String tag;
    private FragmentEventDetailTimeslotHostViewBinding binding;
    private EventDetailTimeSlotViewModel viewModel;
    private Event event;
    private WeekView weekView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_timeslot_host_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventDetailTimeSlotViewModel(presenter);
        viewModel.setTag(tag);
        if (event==null) {
            event = EventManager.getInstance().copyCurrentEvent(EventManager.getInstance().getCurrentEvent());
        }
        viewModel.setEventDetailHostEvent(event);
        binding.setTimeSlotHostVM(viewModel);

        weekView = (WeekView) binding.getRoot().findViewById(R.id.edit_timeslot_weekview);
        weekView.enableTimeSlot();
        weekView.setEventClassName(Event.class);
        weekView.setDayEventMap(EventManager.getInstance().getEventsMap());
        if (UserUtil.getUserUid().equals(event.getHostUserUid())){
            // which means this is host event
        }else{
            // which means this is invitee event
            weekView.removeAllOptListener();
        }
    }

    public void initTimeSlots(){
        weekView.resetTimeSlots();
        if (event.hasTimeslots()) {
            for (TimeSlot timeSlot : event.getTimeslots()) {
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_pending))){
                    struct.status=false;
                }else if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_accept))){
                    struct.status = true;
                }
                weekView.addTimeSlot(struct);
            }
        }
        weekView.reloadTimeSlots(true);
    }

//
    public Event getEvent() {
        return event;
    }
//
    public void setEvent(Event event) {
        this.event = event;
        if (viewModel!=null){
            viewModel.setEventDetailHostEvent(event);
        }
        initTimeSlots();
    }


    @Override
    public EventDetailHostTimeSlotPresenter createPresenter() {
        return new EventDetailHostTimeSlotPresenter(getContext());
    }


    @Override
    public void onClickBack() {
        if (getFrom() instanceof EventDetailGroupFragment){
            switchFragment(this, (EventDetailGroupFragment)getFrom());
        }else if (getFrom() instanceof EventEditFragment){
            switchFragment(this,(EventEditFragment)getFrom());
        }
    }

    @Override
    public void onClickDone() {
        if (getFrom() instanceof EventEditFragment){
            ((EventEditFragment) getFrom()).setEvent(event);
            switchFragment(this, (EventEditFragment) getFrom());
        }else if (getFrom() instanceof EventDetailGroupFragment){
            ((EventDetailGroupFragment) getFrom()).setEvent(event);
            switchFragment(this, (EventDetailGroupFragment)getFrom());
        }
    }

}
