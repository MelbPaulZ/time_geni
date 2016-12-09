package org.unimelb.itime.ui.fragment.eventdetail;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import org.unimelb.itime.ui.mvpview.EventDetailTimeSlotMvpVIew;
import org.unimelb.itime.ui.presenter.EventDetailHostTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailTimeSlotViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.sql.Struct;
import java.util.List;
import java.util.Map;

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
    private Map<String, List<EventUtil.StatusKeyStruct>> adapterData;

    private Fragment clickFromFragment;

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
        weekView.setDayEventMap(EventManager.getInstance().getEventsPackage());

        if (UserUtil.getUserUid().equals(event.getHostUserUid())){
            // which means this is host event
        }else{
            // which means this is invitee event
            weekView.removeAllOptListener();
        }
    }


    public Fragment getClickFromFragment() {
        return clickFromFragment;
    }

    public void setClickFromFragment(Fragment clickFromFragment) {
        this.clickFromFragment = clickFromFragment;
        if (this.viewModel != null){
            this.viewModel.setFromFragment(this.clickFromFragment);
        }
    }
//
    public Event getEvent() {
        return event;
    }
//
    public void setEvent(Event event, Map<String, List<EventUtil.StatusKeyStruct>> adapterData) {
        this.event = event;
        this.adapterData = adapterData;
        if (viewModel!=null){
            viewModel.setEventDetailHostEvent(event);
        }
        viewModel.initTimeSlots(weekView);
    }

    public void setEvent(Event event){
        this.setEvent(event, this.adapterData);
    }


    @Override
    public EventDetailHostTimeSlotPresenter createPresenter() {
        return new EventDetailHostTimeSlotPresenter(getActivity());
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

    @Override
    public void reloadTimeslot() {
        weekView.reloadTimeSlots(false);
    }

    @Override
    public void addTimeslot(WeekView.TimeSlotStruct timeSlotStruct) {
        weekView.addTimeSlot(timeSlotStruct);
        List<Invitee> invitees = this.event.getInvitee();
        for (Invitee invitee:invitees
             ) {
            SlotResponse defaultResponse = new SlotResponse();
            defaultResponse.setStatus("pending");
            defaultResponse.setRate(1);
            defaultResponse.setTimeslotUid(((Timeslot)timeSlotStruct.object).getTimeslotUid());
            defaultResponse.setInviteeUid(invitee.getInviteeUid());
            defaultResponse.setEventUid(this.event.getEventUid());
            defaultResponse.setUserUid(Integer.parseInt(invitee.getUserUid()));
            invitee.getSlotResponses().add(defaultResponse);
        }


        //refresh event and slots data
        this.adapterData = EventUtil.getAdapterData(event);
    }

    @Override
    public void onEnter() {
        super.onEnter();
        if (!(getFrom() instanceof EventEditFragment)){
            weekView.removeAllOptListener();
        }else{
            weekView.enableTimeSlot();
        }
    }

    @Override
    public void popupTimeSlotWindow(final TimeSlotView timeSlotView) {
        final AlertDialog alertDialog = new AlertDialog.Builder(presenter.getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(presenter.getContext());
        View root = inflater.inflate(R.layout.fragment_timeslot_attendee_response, null);

        ListView response_view = (ListView) root.findViewById(R.id.response_view);
        InviteeInnerResponseAdapter innerAdapter = new InviteeInnerResponseAdapter(getContext());
        Timeslot timeslot = (Timeslot) (((WeekView.TimeSlotStruct)timeSlotView.getTag()).object);

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

        TextView button_reject = (TextView) root.findViewById(R.id.invitee_response_select_button);
        button_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                // here should add presenter change event status as reject
                onClickTimeSlotView(timeSlotView);
            }
        });
        alertDialog.setView(root);
        alertDialog.show();
    }

    private String getTimeTitle(Timeslot timeslot){
        if (timeslot == null){
            return  "";
        }

        return EventUtil.getSlotStringFromLong(presenter.getContext()
                ,timeslot.getStartTime()
                ,timeslot.getEndTime());
    }

    private String getSubTimeTitle(Timeslot timeslot){
        if (timeslot == null){
            return "";
        }

        return EventUtil.getDayOfWeekString(presenter.getContext()
                , timeslot.getStartTime());
    }

    @Override
    public void onClickTimeSlotView(TimeSlotView timeSlotView){
//        if (this.event.getStatus().equals("confirmed")){
//            Toast.makeText(presenter.getContext(), "This event is already confirmed",Toast.LENGTH_SHORT).show();
//        }else{

        // change status of view and struct
        if (UserUtil.getUserUid().equals(event.getHostUserUid())){
            // for host , only one timeslot can be selected
            // change here
            if (TimeSlotUtil.getSelectedTimeSlots(getContext(),event.getTimeslot()).size()<1){
                // if no timeslot has been selected
                changeTimeSlotView(timeSlotView);
                hostClickTimeSlot(timeSlotView);
            }else{
                // if other timeslot has been selected
                if (((WeekView.TimeSlotStruct)timeSlotView.getTag()).status==true){
                    changeTimeSlotView(timeSlotView);
                    hostClickTimeSlot(timeSlotView);
                }else{
                    Toast.makeText(presenter.getContext(), "already select one timeslot, please unclick and click another one",Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            // for invitees, can choose multi timeslots
            changeTimeSlotView(timeSlotView);
            changeEventAttributes(timeSlotView);
        }
//        }

    }

    private void hostClickTimeSlot(TimeSlotView timeSlotView){
        Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot!=null){
            if (timeSlot.getIsConfirmed()==1){
                timeSlot.setIsConfirmed(0);
            }else{
                timeSlot.setIsConfirmed(1);
            }
        }
    }


    private void changeEventAttributes(TimeSlotView timeSlotView){
        Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot!=null) {
            if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_pending))) {
                timeSlot.setStatus(getContext().getString(R.string.timeslot_status_accept));
            } else if (timeSlot.getStatus().equals(getContext().getString(R.string.timeslot_status_accept))) {
                timeSlot.setStatus(getContext().getString(R.string.timeslot_status_pending));
            }
        }else{
            Log.i("error", "onTimeSlotClick: " + "no timeslot found");
        }
    }

    private void changeTimeSlotView(TimeSlotView timeSlotView){
        // if clicked -> unclicked; if unclicked -> clicked
        boolean newStatus = !timeSlotView.isSelect();
        timeSlotView.setStatus(newStatus);
        ((WeekView.TimeSlotStruct) timeSlotView.getTag()).status = newStatus;
    }

    @Override
    public void onShowDialog() {
        AppUtil.showProgressBar(getActivity(), "Updating", "Please wait...");
    }

    @Override
    public void onHideDialog() {
        AppUtil.hideProgressBar();
    }
}
