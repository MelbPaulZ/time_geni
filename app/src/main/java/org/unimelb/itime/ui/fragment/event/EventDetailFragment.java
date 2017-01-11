package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventTimeSlotAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.ViewMainCalendarFragment;
import org.unimelb.itime.ui.fragment.calendars.ViewInCalendarMonthDayFragment;
import org.unimelb.itime.ui.fragment.calendars.ViewInCalendarWeekFragment;
import org.unimelb.itime.ui.mvpview.EventDetailGroupMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailFragment extends EventBaseFragment<EventDetailGroupMvpView, EventCommonPresenter<EventDetailGroupMvpView>> implements EventDetailGroupMvpView {
    private org.unimelb.itime.databinding.FragmentEventDetailBinding binding;
    private EventDetailViewModel eventDetailForHostViewModel;
    private Event event;


    private Map<String, List<EventUtil.StatusKeyStruct>> adapterData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventDetailForHostViewModel = new EventDetailViewModel(getPresenter());
        if(event == null){
            event = EventManager.getInstance(getContext()).copyCurrentEvent(EventManager.getInstance(getContext()).getCurrentEvent());
            this.adapterData = EventUtil.getAdapterData(event);
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
            this.adapterData = EventUtil.getAdapterData(event);
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
            for (Invitee invitee:goings) {
                RelativeLayout frame = new RelativeLayout(getContext());
                LinearLayout.LayoutParams frameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                frameParams.rightMargin = 20;
                ImageView img = new ImageView(getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
                EventUtil.bindUrlHelper(getContext(),invitee.getAliasPhoto(),img, new CircleTransform());
                frame.addView(img,params);

                ImageView status = new ImageView(getContext());
                RelativeLayout.LayoutParams status_params = new RelativeLayout.LayoutParams(width/4,width/4);
                status_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                status_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                status.setImageDrawable(getContext().getResources().getDrawable(
                        invitee.getIsHost() == 1 ? R.drawable.icon_event_host_selected:R.drawable.icon_event_attendee_selected));
                frame.addView(status,status_params);

                status_img.addView(frame,frameParams);
            }
        }
    }

    @Override
    public EventCommonPresenter<EventDetailGroupMvpView> createPresenter() {
        return new EventCommonPresenter<>(getContext());
    }

    private void toCalendar(int resultCode) {
        Intent intent = new Intent();
        getActivity().setResult(resultCode, intent);
        getActivity().finish();
    }

    @Override
    public void toEditEvent() {
        EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
        Event cpyEvent = EventManager.getInstance(getContext()).copyCurrentEvent(event);
        for (Timeslot timeslot: cpyEvent.getTimeslot()){
            timeslot.setStatus(Timeslot.STATUS_PENDING);
        }
        eventEditFragment.setEvent(cpyEvent);

        EventManager.getInstance(getContext()).setCurrentEvent(event);

        openFragment(this, eventEditFragment);
    }

    @Override
    public void viewInCalendar() {

        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){

            ViewMainCalendarFragment viewMainCalendarFragment = (ViewMainCalendarFragment) getFragmentManager().findFragmentByTag(ViewMainCalendarFragment.class.getSimpleName());
            ViewInCalendarWeekFragment viewInCalendarWeekFragment = viewMainCalendarFragment.getWeekViewFrag();
            viewInCalendarWeekFragment.scrollToWithOffset(event.getStartTime());
            viewInCalendarWeekFragment.showAnim(event);

            openFragment(this,viewMainCalendarFragment);
        }else {
            final EventDetailTimeSlotFragment timeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
            timeSlotFragment.setEvent(EventManager.getInstance(getContext()).copyCurrentEvent(event), this.adapterData);
            timeSlotFragment.getWeekView().showTimeslotAnim(event.getTimeslot());
            openFragment(this,timeSlotFragment);
        }
    }

    @Override
    public void viewInviteeResponse(Timeslot timeSlot) {
        InviteeTimeslotFragment inviteeTimeslotFragment = (InviteeTimeslotFragment) getFragmentManager().findFragmentByTag(InviteeTimeslotFragment.class.getSimpleName());
        inviteeTimeslotFragment.setData(this.event, adapterData.get(timeSlot.getTimeslotUid()),timeSlot);
        openFragment(this, inviteeTimeslotFragment);
    }

    @Override
    public void gotoGridView() {
        EventPhotoGridFragment gridFragment = (EventPhotoGridFragment) getFragmentManager().findFragmentByTag(EventPhotoGridFragment.class.getSimpleName());
        gridFragment.setEvent(event);
        openFragment(this, gridFragment);
    }

    @Override
    public void onClickPhotoGridBack() {
        closeFragment(this, getFrom());
    }


    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskError(int task, String errorMsg, int code) {
        Log.i("TAG", "onTaskError: " + errorMsg);
    }

    @Override
    public void onTaskComplete(int task, List<Event> dataList) {
        if (task == EventCommonPresenter.TASK_TIMESLOT_ACCEPT){
            toCalendar(Activity.RESULT_OK);
        }else if (task == EventCommonPresenter.TASK_EVENT_CONFIRM){
            toCalendar(Activity.RESULT_OK);
        }else if (task == EventCommonPresenter.TASK_TIMESLOT_REJECT){
            toCalendar(Activity.RESULT_OK);
        }else if (task == EventCommonPresenter.TASK_BACK){
            toCalendar(Activity.RESULT_CANCELED);
        }else if (task == EventCommonPresenter.TASK_EVENT_ACCEPT){
            toCalendar(Activity.RESULT_OK);
        }else if (task == EventCommonPresenter.TASK_EVENT_REJECT){
            toCalendar(Activity.RESULT_OK);
        }
    }


    @Override
    public void setLeftTitleStringToVM() {
        eventDetailForHostViewModel.setLeftTitleStr(getString(R.string.back));
    }

    @Override
    public void setTitleStringToVM() {
        String title = EventUtil.isUserHostOfEvent(getContext(), event)?
                getString(R.string.event_details) : getString(R.string.new_invitation);
        eventDetailForHostViewModel.setTitleStr(title);
    }

    @Override
    public void setRightTitleStringToVM() {
        eventDetailForHostViewModel.setRightTitleStr(getString(R.string.edit));
    }
}
