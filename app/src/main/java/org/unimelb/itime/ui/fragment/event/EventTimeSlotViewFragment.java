package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 27/08/2016.
 */
public class EventTimeSlotViewFragment extends EventBaseFragment<EventCreateNewTimeSlotMvpView, TimeslotCommonPresenter<EventCreateNewTimeSlotMvpView>>
        implements EventCreateNewTimeSlotMvpView {

    private FragmentEventCreateTimeslotViewBinding binding;
    private EventCreateTimeslotViewModel viewModel;
    private Event event;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView timeWheelView;
    private WeekView timeslotWeekView;
    private int timePosition;
    private EventTimeSlotViewFragment self;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        self = this;
        timeslotWeekView = (WeekView) binding.getRoot().findViewById(R.id.timeslot_week_view);
        viewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(viewModel);
        inflater = LayoutInflater.from(getContext());
    }



    public void setEvent(Event event) {
        if (this.event!=null) {
            ArrayList<Timeslot> previousTimeslots = (ArrayList<Timeslot>) this.event.getTimeslot();
            event.setTimeslot(previousTimeslots);
        }
        this.event = event;
        viewModel.setEvent(event);
    }

    public void resetCalendar(Event event) {
        timeslotWeekView.resetTimeSlots();
        initTimeSlots(event);
//        scrollToFstTimeSlot(event);
    }

    private void scrollToFstTimeSlot(Event event){
        List<Timeslot> slots = event.getTimeslot();
        if (slots.size() > 0){
            timeslotWeekView.scrollToWithOffset(slots.get(0).getStartTime());
        }
    }

    private void changeTimeSlots(TimeSlotView timeSlotView) {
        // change status of view and struct
        boolean newStatus = !timeSlotView.isSelect();
        Timeslot timeSlot = (Timeslot) timeSlotView.getTimeslot();
        timeSlotView.setIsSelected(newStatus);
        // change event attributes
        if (timeSlot != null) {
//            timeSlot.setDisplayStatus(newStatus);

            if (timeSlot.getStatus().equals(Timeslot.STATUS_CREATING)) {
                timeSlot.setStatus(Timeslot.STATUS_PENDING);
            } else if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING)) {
                timeSlot.setStatus(Timeslot.STATUS_CREATING);
            }
        } else {
            Log.i("error", "onTimeSlotClick: " + "no timeslot found");
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // need to change later
        initListeners();

        if (event != null || event.getTimeslot() == null || event.getTimeslot().size() == 0) {
            Calendar calendar = Calendar.getInstance();
            presenter.getTimeSlots(event, calendar.getTimeInMillis());
        }
        timeslotWeekView.reloadTimeSlots(false);
    }

    public void createTimeSlot(TimeSlotView timeSlotView) {
        Timeslot timeSlot = new Timeslot();
        timeSlot.setTimeslotUid(AppUtil.generateUuid());
        timeSlot.setEventUid(event.getEventUid());
        // what is the difference between getTag().startTime and startTimeM .... discuss with David
        timeSlot.setStartTime(timeSlotView.getNewStartTime());
        timeSlot.setEndTime(timeSlotView.getNewEndTime());
        timeSlot.setStatus(Timeslot.STATUS_CREATING);
        timeSlot.setUserUid(UserUtil.getInstance(getContext()).getUserUid());
        event.getTimeslot().add(timeSlot);
        timeslotWeekView.addTimeSlot(timeSlot);
        timeslotWeekView.reloadTimeSlots(false);
    }

    private void initListeners() {
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_relative_layout);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setIsChangeDuration(true);
                initWheelPickers();
            }
        });
        timeslotWeekView.setEventClassName(Event.class);
        //Enable function of creating time block
        timeslotWeekView.enableTimeSlot();
        timeslotWeekView.setDayEventMap(EventManager.getInstance(getContext()).getEventsPackage());
        timeslotWeekView.setOnTimeSlotOuterListener(new FlexibleLenViewBody.OnTimeSlotListener() {
            @Override
            public void onTimeSlotCreate(TimeSlotView timeSlotView) {
                // popup timeslot create page
                EventTimeSlotCreateFragment eventTimeSlotCreateFragment = (EventTimeSlotCreateFragment) getFragmentManager().findFragmentByTag(EventTimeSlotCreateFragment.class.getSimpleName());
                eventTimeSlotCreateFragment.setTimeSlotView(timeSlotView);
                openFragment(self, eventTimeSlotCreateFragment);
            }

            @Override
            public void onTimeSlotClick(TimeSlotView timeSlotView) {
                if (TimeSlotUtil.getPendingTimeSlots(getContext(), event.getTimeslot()).size() >= 7) {
                    if (timeSlotView.isSelect()) {
                        changeTimeSlots(timeSlotView);
                    } else {
                        Toast.makeText(getContext(), "cannot select, maximum 7 timeslots selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    changeTimeSlots(timeSlotView);
                }
            }

            @Override
            public void onTimeSlotDragStart(TimeSlotView timeSlotView) {

            }

            @Override
            public void onTimeSlotDragging(TimeSlotView timeSlotView, int i, int i1) {

            }

            @Override
            public void onTimeSlotDragDrop(TimeSlotView timeSlotView, long startTime, long endTime) {
                timeslotDrop(timeSlotView, startTime, endTime);
            }

        });
    }

    private void timeslotDrop(TimeSlotView timeSlotView, long startTime, long endTime) {
        // update timeslot info
        Timeslot timeslot = (Timeslot) timeSlotView.getTimeslot();
        timeslot.setStartTime(startTime);
        timeslot.setEndTime(endTime);
        timeslotWeekView.reloadTimeSlots(false);

    }

    // todo init
    private void initTimeSlots(Event event) {
        if (event.hasTimeslots()) {
            for (Timeslot timeSlot : event.getTimeslot()) {
                timeslotWeekView.addTimeSlot(timeSlot);
            }
        }
        timeslotWeekView.reloadTimeSlots(false);
    }

//
//    /**
//     * need to check if fragment is doing creating task or editing task,
//     * if doing editing task, do we still recommend?
//     *
//     * @param list
//     */
    @Override
    public void onRecommend(List<Timeslot> list) {
        if (!event.hasTimeslots()) {
            event.setTimeslot(new ArrayList<Timeslot>());
        }
        for (Timeslot timeSlot : list) {
            if (EventManager.getInstance(getContext()).isTimeslotExistInEvent(event, timeSlot)) {
                // already exist, then do nothing
            } else {
                // have to do this
                timeSlot.setEventUid(event.getEventUid());
                timeSlot.setStatus(Timeslot.STATUS_CREATING);
                // todo: need to check if this timeslot already exists in a map
                event.getTimeslot().add(timeSlot);
                timeslotWeekView.addTimeSlot(timeSlot);
            }
        }
        timeslotWeekView.reloadTimeSlots(false);
    }

    //***************************************************************************************
    @Override
    public TimeslotCommonPresenter<EventCreateNewTimeSlotMvpView> createPresenter() {
        return new TimeslotCommonPresenter<>(getContext());
    }

    @Override
    public void onClickDone() {
        if (getFrom() instanceof InviteeFragment || getFrom() instanceof EventTimeSlotCreateFragment) {
            EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
            beforeSendingFragment.setEvent(event);
            openFragment(this, beforeSendingFragment);
        } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment) {
            EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFrom();
            beforeSendingFragment.setEvent(event);
            openFragment(this, getFrom());
        }
    }

    @Override
    public void onClickBack() {
        if (getFrom() instanceof InviteeFragment) {
            openFragment(this, (InviteeFragment) getFrom());
        } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment && getTo() instanceof InviteeFragment) {
            InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
            openFragment(this, inviteeFragment);
        } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment && getTo() instanceof EventCreateDetailBeforeSendingFragment) {
            openFragment(this, (EventCreateDetailBeforeSendingFragment) getFrom());
        }else if (getFrom() instanceof EventTimeSlotCreateFragment){
            openFragment(this, (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName()));
        }
    }

    public void initWheelPickers() {
        View root = inflater.inflate(R.layout.timeslot_duration_picker, null);
        final TextView durationTime = (TextView) root.findViewById(R.id.popup_duration);
        timeWheelView = (WheelView) root.findViewById(R.id.time_wheelview);
        timeWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        timeWheelView.setSkin(WheelView.Skin.Holo);
        timeWheelView.setWheelData(EventUtil.getDurationTimes());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#000000");
        style.holoBorderColor = Color.LTGRAY;
        style.backgroundColor = Color.WHITE;
        style.textColor = Color.GRAY;
        style.selectedTextSize = 15;
        timeWheelView.setStyle(style);
        timeWheelView.setWheelSize(5);
        timeWheelView.setLoop(true);
        timeWheelView.setSelection(timePosition);
        timeWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                durationTime.setText(EventUtil.getDurationTimes().get(position));
                timePosition = position;
            }
        });

        final PopupWindow popupWindow = new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT, 1200);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationPopup);
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.bottom_bar), Gravity.BOTTOM, 0, -600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                viewModel.setDurationTimeString(EventUtil.getDurationTimes().get(timePosition));
                viewModel.setIsChangeDuration(true);
                timeslotWeekView.updateTimeSlotsDuration(EventUtil.getDurationInMintues(timePosition) * 60 * 1000, false); // ? animate?

                // avoid of no timeslot error
                if (!event.hasTimeslots()) {
                    event.setTimeslot(new ArrayList<Timeslot>());
                }
                for (Timeslot timeSlot : event.getTimeslot()) {
                    timeSlot.setEndTime(timeSlot.getStartTime() + EventUtil.getDurationInMintues(timePosition) * 60 * 1000);
                }
                viewModel.setEvent(event);
                timeslotWeekView.reloadTimeSlots(false); // for page refresh
            }
        });

        TextView duration = (TextView) root.findViewById(R.id.popup_duration);
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

}
