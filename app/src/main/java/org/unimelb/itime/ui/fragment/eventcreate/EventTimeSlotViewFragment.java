package org.unimelb.itime.ui.fragment.eventcreate;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
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
import java.util.zip.Inflater;


/**
 * Created by Paul on 27/08/2016.
 */
public class EventTimeSlotViewFragment extends BaseUiFragment<EventCreateNewTimeSlotMvpView,EventCreateTimeSlotPresenter>
        implements EventCreateNewTimeSlotMvpView{

    private FragmentEventCreateTimeslotViewBinding binding;
    private EventCreateTimeslotViewModel viewModel;
    private Event event;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView hourWheelView,minuteWheelView;
    private WeekView timeslotWeekView;
    private int hourPosition,minPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view , container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        timeslotWeekView = (WeekView) binding.getRoot().findViewById(R.id.timeslot_week_view);
        viewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(viewModel);

        inflater=LayoutInflater.from(getContext());
    }

    public void setEvent(Event event){
        this.event = event;
        getPresenter().setEvent(event);
        viewModel.setEvent(event);
    }

    public void resetCalendar(Event event){
        timeslotWeekView.resetTimeSlots();
        initTimeSlots(event);
    }

    private void changeTimeSlots(TimeSlotView timeSlotView){
        // change status of view and struct
        boolean newStatus = !timeSlotView.isSelect();
        timeSlotView.setStatus(newStatus);
        ((WeekView.TimeSlotStruct)timeSlotView.getTag()).status = newStatus;
        // change event attributes
        Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot!=null) {
            if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_create))) {
                timeSlot.setStatus(getString(R.string.timeslot_status_pending));
            } else if (timeSlot.getStatus().equals(getString(R.string.timeslot_status_pending))) {
                timeSlot.setStatus(getString(R.string.timeslot_status_create));
            }
        }else{
            Log.i("error", "onTimeSlotClick: " + "no timeslot found");
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        // need to change later

        initListeners();

        if(event != null || event.getTimeslot() == null || event.getTimeslot().size() == 0){
            Calendar calendar = Calendar.getInstance();
            presenter.getTimeSlots(calendar.getTimeInMillis());
        }
    }

    private void initListeners(){
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_relative_layout);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setIsChangeDuration(true);
                initWheelPickers();
            }
        });
        timeslotWeekView.setEventClassName(Event.class);
        timeslotWeekView.enableTimeSlot();
        timeslotWeekView.setDayEventMap(EventManager.getInstance().getEventsMap());
        timeslotWeekView.setOnTimeSlotOuterListener(new FlexibleLenViewBody.OnTimeSlotListener() {
           @Override
           public void onTimeSlotCreate(TimeSlotView timeSlotView) {
               Timeslot timeSlot = new Timeslot();
               timeSlot.setTimeslotUid(AppUtil.generateUuid());
               timeSlot.setEventUid(event.getEventUid());
               timeSlot.setStartTime(((WeekView.TimeSlotStruct)timeSlotView.getTag()).startTime);
               timeSlot.setEndTime(((WeekView.TimeSlotStruct)timeSlotView.getTag()).endTime);
               timeSlot.setStatus(getString(R.string.timeslot_status_create));
               timeSlot.setUserUid(UserUtil.getUserUid());
               event.getTimeslot().add(timeSlot);
               WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct)timeSlotView.getTag();
               struct.object =timeSlot;

           }

           @Override
           public void onTimeSlotClick(TimeSlotView timeSlotView) {
               if (TimeSlotUtil.getPendingTimeSlots(getContext(),event.getTimeslot()).size() >=7){
                   if (timeSlotView.isSelect()){
                       changeTimeSlots(timeSlotView);
                   }else{
                       Toast.makeText(getContext(),"cannot select, maximum 7 timeslots selected", Toast.LENGTH_SHORT).show();
                   }
               }else{
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
                confirmTimePopup(timeSlotView, startTime, endTime);
            }

       });
    }

    private void timeslotDrop(TimeSlotView timeSlotView, long startTime, long endTime){
        // update timeslot struct
        WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct)timeSlotView.getTag();
        struct.startTime = startTime;
        struct.endTime = endTime;
        timeslotWeekView.reloadTimeSlots(false);

        // update timeslot info
        Timeslot calendarTimeSlot = (Timeslot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
        Timeslot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
        if (timeSlot!=null) {
            timeSlot.setStartTime(timeSlotView.getStartTimeM());
            timeSlot.setEndTime(timeSlotView.getEndTimeM());
        }
    }

    private void confirmTimePopup(final TimeSlotView timeSlotView, final long startTime, final long endTime){
        final AlertDialog dialog = new AlertDialog.Builder(presenter.getContext()).create();
        dialog.setTitle("Confirm Timeslot");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View root = inflater.inflate(R.layout.timeslot_move_confirm, null);
        TextView cancel = (TextView) root.findViewById(R.id.timeslot_move_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView done = (TextView) root.findViewById(R.id.timeslot_move_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeslotDrop(timeSlotView, startTime, endTime);
                dialog.dismiss();
            }
        });
        TextView display = (TextView) root.findViewById(R.id.timeslot_move_tv);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSlotView.getStartTimeM());
        display.setText(EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + "/" +
                calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"+
                calendar.get(Calendar.MINUTE));
        dialog.setView(root);
        dialog.show();
    }
//
//
//
    public void initWheelPickers(){
        View root = inflater.inflate(R.layout.timeslot_duration_picker, null);
        final TextView durationTime = (TextView) root.findViewById(R.id.popup_duration);
        final int[] pickHour = {0};
        final int[] pickMinute = {0};

        hourWheelView = (WheelView) root.findViewById(R.id.hour_wheelview);
        hourWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        hourWheelView.setSkin(WheelView.Skin.Holo);
        hourWheelView.setWheelData(createHours());
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.selectedTextColor = Color.parseColor("#000000");
        style.holoBorderColor=Color.LTGRAY;
        style.backgroundColor =Color.WHITE;
        style.textColor = Color.GRAY;
        style.selectedTextSize = 15;
        hourWheelView.setStyle(style);
        hourWheelView.setWheelSize(5);
        hourWheelView.setLoop(true);
        hourWheelView.setExtraText("Hours", Color.parseColor("#000000"), 40, 100);
        hourWheelView.setSelection(hourPosition);
        hourWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                pickHour[0] = Integer.parseInt(createHours().get(position));
                durationTime.setText(getTimeString(pickHour[0], pickMinute[0]));
                hourPosition = position;
            }
        });

        minuteWheelView = (WheelView) root.findViewById(R.id.minute_wheelview);
        minuteWheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        minuteWheelView.setSkin(WheelView.Skin.Holo);
        minuteWheelView.setWheelData(createMinutes());
        minuteWheelView.setStyle(style);
        minuteWheelView.setLoop(true);
        minuteWheelView.setWheelSize(5);
        minuteWheelView.setExtraText("Min", Color.parseColor("#000000"), 40, 100);
        minuteWheelView.setSelection(minPosition);
        minuteWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                pickMinute[0] = Integer.parseInt(createMinutes().get(position));
                durationTime.setText(getTimeString(pickHour[0], pickMinute[0]));
                minPosition = position;
            }
        });


        final PopupWindow popupWindow = new PopupWindow(root,ViewGroup.LayoutParams.MATCH_PARENT,850);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationPopup);
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.bottom_bar), Gravity.BOTTOM,0, -600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                viewModel.setDurationTimeString(getTimeString(pickHour[0],pickMinute[0]));
                viewModel.setIsChangeDuration(true);
                timeslotWeekView.updateTimeSlotsDuration((pickHour[0] * 60 + pickMinute[0])*60*1000, true); // ? animate?

                // avoid of no timeslot error
                if (!event.hasTimeslots()){
                    event.setTimeslot(new ArrayList<Timeslot>());
                }
                for (Timeslot timeSlot: event.getTimeslot()){
                    timeSlot.setEndTime(timeSlot.getStartTime() + 1000 * 60 * (pickHour[0] * 60 + pickMinute[0]));
                }
                viewModel.setEvent(event);


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

    public String getTimeString(int hour, int minute){
        String hourStr = hour==0? "": hour>1? hour + " hours" : hour + " hour";
        String minStr = minute + " min";
        return hourStr +" "+ minStr;
    }


    private ArrayList<String> createHours() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 24; i++) {
            if (i < 10) {
                list.add(String.valueOf(i));
            } else {
                list.add("" + i);
            }
        }
        list.add("0");
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            list.add("" + i*15);
        }
        return list;
    }

    @Override
    public void onClickDone() {
        if (getFrom() instanceof InviteeFragment) {
            EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
            beforeSendingFragment.setEvent(event);
            switchFragment(this, beforeSendingFragment);
        }else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment){
            EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment)getFrom();
            beforeSendingFragment.setEvent(event);
            switchFragment(this, (EventCreateDetailBeforeSendingFragment)getFrom());
        }
    }

    @Override
    public void onClickBack() {
        if (getFrom() instanceof InviteeFragment) {
            switchFragment(this, (InviteeFragment)getFrom());
        }else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment && getTo() instanceof InviteeFragment){
            InviteeFragment inviteeFragment = (InviteeFragment) getFragmentManager().findFragmentByTag(InviteeFragment.class.getSimpleName());
            switchFragment(this, inviteeFragment);
        }else if (getFrom() instanceof  EventCreateDetailBeforeSendingFragment && getTo() instanceof EventCreateDetailBeforeSendingFragment){
            switchFragment(this, (EventCreateDetailBeforeSendingFragment)getFrom());
        }
    }

    @Override
    public void initTimeSlots(Event event) {
        if (event.hasTimeslots()) {
            for (Timeslot timeSlot : event.getTimeslot()) {
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                timeslotWeekView.addTimeSlot(struct);
            }
        }
    }


    /**
     * need to check if fragment is doing creating task or editing task,
     * if doing editing task, do we still recommend?
     * @param list
     */
    @Override
    public void onRecommend(List<Timeslot> list) {
        if(!event.hasTimeslots()){
            event.setTimeslot(new ArrayList<Timeslot>());
        }
        for (Timeslot timeSlot: list){
            if (EventManager.getInstance().isTimeslotExistInEvent(event, timeSlot)){
                // already exist, then do nothing
            }else {
                // have to do this
                timeSlot.setEventUid(event.getEventUid());
                timeSlot.setStatus(getString(R.string.timeslot_status_create));
                // todo: need to check if this timeslot already exists in a map
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                struct.status = false;
                event.getTimeslot().add(timeSlot);
                timeslotWeekView.addTimeSlot(struct);
            }
        }
        timeslotWeekView.reloadTimeSlots(true);
    }

    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext());
    }
}
