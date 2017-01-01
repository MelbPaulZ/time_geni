package org.unimelb.itime.ui.fragment.event;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.InviteeInnerResponseAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventDetailTimeslotHostViewBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailTimeSlotViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 10/09/2016.
 */
public class EventDetailTimeSlotFragment extends EventBaseFragment<EventDetailTimeSlotMvpVIew, TimeslotCommonPresenter<EventDetailTimeSlotMvpVIew>>
        implements EventDetailTimeSlotMvpVIew {
    private FragmentEventDetailTimeslotHostViewBinding binding;
    private EventDetailTimeSlotViewModel viewModel;
    private Event event;
    private WeekView weekView;
    private Map<String, List<EventUtil.StatusKeyStruct>> adapterData;
    private EventManager eventManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail_timeslot_host_view, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new EventDetailTimeSlotViewModel(presenter);
        if (event == null) {
            event = eventManager.copyCurrentEvent(eventManager.getCurrentEvent());
        }
        viewModel.setEventDetailHostEvent(event);
        binding.setTimeSlotHostVM(viewModel);
        if (UserUtil.getInstance(getContext()).getUserUid().equals(event.getHostUserUid())) {
            // which means this is host event
        } else {
            // which means this is invitee event
            weekView.removeAllOptListener();
        }
    }

    private void init(){
        eventManager = EventManager.getInstance(getContext());
        weekView = (WeekView) binding.getRoot().findViewById(R.id.edit_timeslot_weekview);

        weekView.enableTimeSlot();
        weekView.setEventClassName(Event.class);
        weekView.setDayEventMap(eventManager.getEventsPackage());
    }

    //
    public Event getEvent() {
        return event;
    }

    //
    public void setEvent(Event event, Map<String, List<EventUtil.StatusKeyStruct>> adapterData) {
        this.event = event;
        this.adapterData = adapterData;
        if (viewModel != null) {
            viewModel.setEventDetailHostEvent(event);
        }
    }


    private void initTimeslotsFromEditFragment(WeekView weekView) {
        viewModel.initTimeslotsFromEditFragment(weekView);
    }

    private void initTimeslotsFromInviteeFragment(WeekView weekView) {
        viewModel.initTimeslotsFromInviteeFragment(weekView);
    }

    private void initTimeslotsFromDetailFragment(WeekView weekView) {
        viewModel.initTimeslotsFromDetailFragment(weekView);
    }


    public void setEvent(Event event) {
        this.setEvent(event, this.adapterData);
    }


    @Override
    public TimeslotCommonPresenter<EventDetailTimeSlotMvpVIew> createPresenter() {
        return new TimeslotCommonPresenter<>(getContext());
    }


    @Override
    public void onClickBack() {
        if (getFrom() instanceof EventDetailFragment) {
            closeFragment(this, (EventDetailFragment) getFrom());
        } else if (getFrom() instanceof EventEditFragment) {
            closeFragment(this, (EventEditFragment) getFrom());
        } else if (getFrom() instanceof InviteeFragment) {
            closeFragment(this, (InviteeFragment) getFrom());
        }
    }

    @Override
    public void onClickDone(Event event) {
        if (getFrom() instanceof EventEditFragment) {
            ((EventEditFragment) getFrom()).setEvent(event);
            openFragment(this, (EventEditFragment) getFrom());
        } else if (getFrom() instanceof EventDetailFragment) {
            ((EventDetailFragment) getFrom()).setEvent(event);
            openFragment(this, (EventDetailFragment) getFrom());
        } else if (getFrom() instanceof InviteeFragment) {
            EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
            eventEditFragment.setEvent(eventManager.copyCurrentEvent(event));
            openFragment(this, eventEditFragment);
        }
    }


    @Override
    public void reloadTimeslot() {
        weekView.reloadTimeSlots(false);
    }

    @Override
    public void addTimeslot(Timeslot timeslot) {
        weekView.addTimeSlot(timeslot);
        List<Invitee> invitees = this.event.getInvitee();
        for (Invitee invitee : invitees
                ) {
            SlotResponse defaultResponse = new SlotResponse();
            defaultResponse.setStatus("pending");
            defaultResponse.setRate(1);
            defaultResponse.setTimeslotUid(timeslot.getTimeslotUid());
            defaultResponse.setInviteeUid(invitee.getInviteeUid());
            defaultResponse.setEventUid(this.event.getEventUid());
            defaultResponse.setUserUid(Integer.parseInt(invitee.getUserUid()));
            invitee.getSlotResponses().add(defaultResponse);
        }
        //refresh event and slots data
        this.adapterData = EventUtil.getAdapterData(event);
        weekView.reloadTimeSlots(false);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        if (getFrom() instanceof EventDetailFragment) {
            initTimeslotsFromDetailFragment(weekView);
            weekView.removeAllOptListener();
        } else if (getFrom() instanceof InviteeFragment) {
            initTimeslotsFromInviteeFragment(weekView);
            presenter.getTimeSlots(event, event.getStartTime());
            weekView.enableTimeSlot();
        } else if (getFrom() instanceof EventEditFragment) {
            initTimeslotsFromEditFragment(weekView);
            presenter.getTimeSlots(event, event.getStartTime());
            weekView.enableTimeSlot();
        }
    }

    /**
     * need to check if fragment is doing creating task or editing task,
     * if doing editing task, do we still recommend?
     *
     * @param list
     */
    @Override
    public void onRecommend(List<Timeslot> list) {
        // only from edit fragment and invitee fragment can receive new suggested timeslot
        if (getFrom() instanceof EventEditFragment || getFrom() instanceof InviteeFragment) {
            if (!event.hasTimeslots()) {
                event.setTimeslot(new ArrayList<Timeslot>());
            }
            for (Timeslot timeSlot : list) {
                if (eventManager.isTimeslotExistInEvent(event, timeSlot)) {
                    // already exist, then do nothing
                } else {
                    // have to do this
                    timeSlot.setEventUid(event.getEventUid());
                    timeSlot.setStatus(Timeslot.STATUS_CREATING);
                    // todo: need to check if this timeslot already exists in a map
                    event.getTimeslot().add(timeSlot);
                    weekView.addTimeSlot(timeSlot);
                }
            }
//            weekView.reloadTimeSlots(false);
        }
    }

    @Override
    public boolean isClickTSConfirm() {
        if (getFrom() instanceof EventDetailFragment) {
            return true;
        } else if (getFrom() instanceof EventEditFragment) {
            return false;
        } else if (getFrom() instanceof InviteeFragment) {
            return false;
        }
        return false;
    }


    @Override
    public void popupTimeSlotWindow(final TimeSlotView timeSlotView) {
        final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(presenter.getContext());
        View root = inflater.inflate(R.layout.fragment_timeslot_attendee_response, null);

        ListView response_view = (ListView) root.findViewById(R.id.response_view);
        InviteeInnerResponseAdapter innerAdapter = new InviteeInnerResponseAdapter(getContext());
        Timeslot timeslot = (Timeslot) timeSlotView.getTimeslot();

        TextView timeslot_title = (TextView) root.findViewById(R.id.timeslot_title);
        timeslot_title.setText(this.getTimeTitle(timeslot));

        TextView timeslot_subtitle = (TextView) root.findViewById(R.id.timeslot_subtitle);
        timeslot_subtitle.setText(this.getSubTimeTitle(timeslot));

        innerAdapter.setInvitees(this.adapterData.get(timeslot.getTimeslotUid()), event);

        response_view.setAdapter(innerAdapter);

        TextView button_cancel = (TextView) root.findViewById(R.id.invitee_response_cancel_button);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        TextView button_select = (TextView) root.findViewById(R.id.invitee_response_select_button);
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (EventUtil.isUserHostOfEvent(getContext(), event)) {

                    changeTimeSlotView(timeSlotView);
                    hostClickTimeSlot(timeSlotView);
                } else {
                    changeTimeSlotView(timeSlotView);
                    changeEventAttributes(timeSlotView);
                }
            }
        });
        alertDialog.setView(root);
        alertDialog.show();
    }

    private String getTimeTitle(Timeslot timeslot) {
        if (timeslot == null) {
            return "";
        }

        return EventUtil.getSlotStringFromLong(presenter.getContext()
                , timeslot.getStartTime()
                , timeslot.getEndTime());
    }

    private String getSubTimeTitle(Timeslot timeslot) {
        if (timeslot == null) {
            return "";
        }

        return EventUtil.getDayOfWeekString(presenter.getContext()
                , timeslot.getStartTime());
    }

    @Override
    public void onClickTimeSlotView(TimeSlotView timeSlotView) {
        if (getFrom() instanceof EventDetailFragment) {
            // change status of view and struct
            if (EventUtil.isUserHostOfEvent(getContext(), event)) {
                // for host , only one timeslot can be selected
                if (TimeSlotUtil.getSelectedTimeSlots(getContext(), event.getTimeslot()).size() < 1) {
                    // if no timeslot has been selected
                    popupTimeSlotWindow(timeSlotView);
                } else {
                    // if other timeslot has been selected
                    if (timeSlotView.isSelect() == true) {
                        changeTimeSlotView(timeSlotView);
                        hostClickTimeSlot(timeSlotView);
                    } else {
                        Toast.makeText(presenter.getContext(), "already select one timeslot, please unclick and click another one", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // for invitees, can choose multi timeslots
                if (timeSlotView.isSelect() == true) {
                    // mute unselect
                    changeTimeSlotView(timeSlotView);
                    changeEventAttributes(timeSlotView);
                } else {
                    // popup select
                    popupTimeSlotWindow(timeSlotView);
                }
            }
        }else{
            // from invitee fragment or editFragment
                changeTimeSlotView(timeSlotView);
                changeTimeslotCreateAndPending(timeSlotView);
        }
    }

    private void hostClickTimeSlot(TimeSlotView timeSlotView) {
        Timeslot calendarTimeSlot = (Timeslot) timeSlotView.getTimeslot();
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot != null) {
            if (timeSlot.getIsConfirmed() == 1) {
                timeSlot.setIsConfirmed(0);
            } else {
                timeSlot.setIsConfirmed(1);
            }
        }
    }


    private void changeEventAttributes(TimeSlotView timeSlotView) {
        Timeslot calendarTimeSlot = (Timeslot) timeSlotView.getTimeslot();
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot != null) {
            if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING)) {
                timeSlot.setStatus(Timeslot.STATUS_ACCEPTED);
            } else if (timeSlot.getStatus().equals(Timeslot.STATUS_ACCEPTED)) {
                timeSlot.setStatus(Timeslot.STATUS_PENDING);
            }
        } else {
            Log.i("error", "onTimeSlotClick: " + "no timeslot found");
        }
    }

    private void changeTimeSlotView(TimeSlotView timeSlotView) {
        // if clicked -> unclicked; if unclicked -> clicked
        boolean newStatus = !timeSlotView.isSelect();
        timeSlotView.setStatus(newStatus);
        timeSlotView.getTimeslot().setDisplayStatus(newStatus);
    }

    private void changeTimeslotCreateAndPending(TimeSlotView timeSlotView){
        Timeslot calendarTimeSlot = (Timeslot) timeSlotView.getTimeslot();
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot!=null){
            if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING)){
                timeSlot.setStatus(Timeslot.STATUS_CREATING);
            }else if (timeSlot.getStatus().equals(Timeslot.STATUS_CREATING)){
                timeSlot.setStatus(Timeslot.STATUS_PENDING);
            }
        }
    }

    public WeekView getWeekView(){
        return this.weekView;
    }
}
