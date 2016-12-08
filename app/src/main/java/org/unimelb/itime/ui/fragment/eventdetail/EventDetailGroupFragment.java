package org.unimelb.itime.ui.fragment.eventdetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.stringtemplate.v4.debug.EvalExprEvent;
import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventTimeSlotAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventDetailGroupPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;
import org.unimelb.itime.vendor.helper.LoadImgHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailGroupFragment extends BaseUiFragment<EventDetailGroupMvpView, EventDetailGroupPresenter> implements EventDetailGroupMvpView {
    private org.unimelb.itime.databinding.FragmentEventDetailBinding binding;
    private EventDetailViewModel eventDetailForHostViewModel;
    private Event event;
    private LayoutInflater inflater;

    private Map<String, List<StatusKeyStruct>> adapterData;

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
            this.adapterData = getAdapterData(event);
        }
        eventDetailForHostViewModel.setEvDtlHostEvent(event);
        eventDetailForHostViewModel.setEvAdapterEvent(adapterData);
        binding.setHostDetailVM(eventDetailForHostViewModel);
        setProposedTimeSlots(event);

        //doing
        refreshInviteeStatus();
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
            this.adapterData = getAdapterData(event);
            eventDetailForHostViewModel.setEvAdapterEvent(this.adapterData);
        }
        setProposedTimeSlots(event);
        //doing
        refreshInviteeStatus();
    }

    private void refreshInviteeStatus(){
        if (this.adapterData != null){
            LinearLayout status_text = (LinearLayout) binding.getRoot().findViewById(R.id.invitees_status_text);
            LinearLayout status_img = (LinearLayout) binding.getRoot().findViewById(R.id.invitees_status_img);
            status_text.removeAllViews();
            status_img.removeAllViews();

            List<Invitee> invitees = event.getInvitee();

            int replyNum = 0;
            int inviteesNum = invitees.size();

            List<Invitee> goings = new ArrayList<>();

            for (Invitee invitee:invitees
                 ) {
                if (invitee.getStatus().equals("accepted")){
                    replyNum += 1;
                    goings.add(invitee);
                }
            }

            TextView repliedTextview = new TextView(getContext());
            repliedTextview.setText(replyNum + (event.getStatus().equals("confirmed") ? " Going" : " Replied"));
            repliedTextview.setTextColor(Color.parseColor("#7bb6ee"));
            status_text.addView(repliedTextview);

            TextView inviteesTextview = new TextView(getContext());
            LinearLayout.LayoutParams inviteesTextviewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            inviteesTextview.setText(inviteesNum + " Invited");
            inviteesTextview.setTextColor(Color.parseColor("#f99e2b"));
            inviteesTextviewParams.leftMargin = 20;
            status_text.addView(inviteesTextview,inviteesTextviewParams);

            int width = DensityUtil.dip2px(getContext(),50);
            for (Invitee invitee:goings
                 ) {
                RelativeLayout frame = new RelativeLayout(getContext());
                RelativeLayout.LayoutParams frameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

                ImageView img = new ImageView(getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
                EventUtil.bindUrlHelper(getContext(),invitee.getAliasPhoto(),img, new CircleTransform());
                frame.addView(img,params);

                ImageView status = new ImageView(getContext());
                RelativeLayout.LayoutParams status_params = new RelativeLayout.LayoutParams(width/4,width/4);
                status_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                status_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                status.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_event_attendee_selected));
                frame.addView(status,status_params);

                status_img.addView(frame,frameParams);
            }
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
        EventManager.getInstance().setCurrentEvent(event);
        switchFragment(this, eventEditFragment);
    }

    @Override
    public void viewInCalendar() {
        EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
        timeSlotFragment.setEvent(EventManager.getInstance().copyCurrentEvent(event), this.adapterData);
        switchFragment(this,timeSlotFragment);
    }

    @Override
    public void viewInviteeResponse(Timeslot timeSlot) {
        InviteeTimeslotFragment inviteeTimeslotFragment = (InviteeTimeslotFragment) getFragmentManager().findFragmentByTag(InviteeTimeslotFragment.class.getSimpleName());
        inviteeTimeslotFragment.setData(this.event, adapterData.get(timeSlot.getTimeslotUid()),timeSlot);
        switchFragment(this, inviteeTimeslotFragment);
    }

    @Override
    public void refreshCalendars() {
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }

    private Map<String, List<StatusKeyStruct>> getAdapterData(Event event){
        List<Invitee> invitees = event.getInvitee();
        List<Timeslot> timeSlots = event.getTimeslot();
        //slo Uid -- List (status - List invitee)
        Map<String,List<StatusKeyStruct>> results = new HashMap<>();

        for (Timeslot slot: timeSlots
             ) {
            List<StatusKeyStruct> structs = new ArrayList<>();
            StatusKeyStruct acp_st = new StatusKeyStruct("accepted");
            structs.add(acp_st);

            StatusKeyStruct rejected_st = new StatusKeyStruct("rejected");
            structs.add(rejected_st);

            StatusKeyStruct pending_st = new StatusKeyStruct("pending");
            structs.add(pending_st);

            results.put(slot.getTimeslotUid(),structs);
        }

        for (Invitee invitee:invitees
             ) {
            List<SlotResponse> responses = invitee.getSlotResponses();

            for (SlotResponse response: responses
                 ) {
                List<StatusKeyStruct> stucts = results.get(response.getTimeslotUid());
                for (int i = 0; i < stucts.size(); i++) {
                    if (stucts.get(i).getStatus().equals(response.getStatus())){
                        stucts.get(i).addInvitee(invitee);
                        break;
                    }
                }

            }
        }

        return results;
    }

    public class StatusKeyStruct{
        String status;
        //status is key
        List<Invitee> response = new ArrayList<>();

        public StatusKeyStruct(String status) {
            this.status = status;
        }

        public String getStatus(){
            return this.status;
        }

        public void addInvitee(Invitee invitee){
            response.add(invitee);
        }

        public List<Invitee> getInviteeList(){
            return this.response;
        }
    }
}
