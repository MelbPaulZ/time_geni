package org.unimelb.itime.ui.fragment.eventcreate;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.databinding.FragmentLoginBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.dayview.FlexibleLenViewBody;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.timeslot.TimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Paul on 27/08/2016.
 */
public class EventTimeSlotViewFragment extends MvpFragment<EventCreateNewTimeSlotMvpView,EventCreateTimeSlotPresenter>
        implements EventCreateNewTimeSlotMvpView,FragmentTagListener{

    FragmentEventCreateTimeslotViewBinding binding;
    EventCreateTimeslotViewModel viewModel;
    private Event event;
    private String tag;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView hourWheelView,minuteWheelView;
    private WeekView timeslotWeekView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view , container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(viewModel);

        viewModel.setTag(this.tag);
        inflater=LayoutInflater.from(getContext());

        Bundle bundle = getArguments();
        this.event = (Event) bundle.getSerializable(getString(R.string.new_event));
        timeslotWeekView = (WeekView) binding.getRoot().findViewById(R.id.timeslot_week_view);
        initData();
        initTimeSlots(); // this must after  initData()
        initListeners();

    }

    public void initTimeSlots(){
        if (event.hasTimeslots()) {
            for (TimeSlot timeSlot : event.getTimeslots()) {
                WeekView.TimeSlotStruct struct = new WeekView.TimeSlotStruct();
                struct.startTime = timeSlot.getStartTime();
                struct.endTime = timeSlot.getEndTime();
                struct.object = timeSlot;
                timeslotWeekView.addTimeSlot(struct);
            }
        }
    }

    //
//
//    FragmentEventCreateTimeslotViewBinding binding;
//    EventCreateTimeslotViewModel eventCreateTimeslotViewModel;
//    private RelativeLayout durationRelativeLayout;
//    private LayoutInflater inflater;
//    private WheelView hourWheelView,minuteWheelView;
////    WeekTimeSlotView weekTimeSlotView;
//    Map<Long,Boolean> simulateTimeSlots;
//    private Event newEvent;
//    private String tag;
//
//    // tag can be tag_create_event, tag_event_detail_before_sending
//
//    public void setEvent(Event event){
//        this.event = event;
//        initTimeSlots();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view, container, false);
//        this.inflater = inflater;
//        return binding.getRoot();
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Bundle bundle = getArguments();
//
//        this.newEvent = (Event) bundle.getSerializable(getString(R.string.new_event));
//
////        initData();
//        eventCreateTimeslotViewModel = new EventCreateTimeslotViewModel(getPresenter(),this.newEvent);
//        eventCreateTimeslotViewModel.setTag(this.tag);
//        binding.setTimeslotVM(eventCreateTimeslotViewModel);
////        initListeners();
//    }



    public void initListeners(){
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_relative_layout);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setIsChangeDuration(true);
                initWheelPickers();
            }
        });

        TextView timeSlotDoneBtn = (TextView) binding.getRoot().findViewById(R.id.timeslot_view_done_btn);
        timeSlotDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.new_event),event);
                toNewEventDetailBeforeSending(bundle);
            }
        });

        TextView backBtn = (TextView) binding.getRoot().findViewById(R.id.timeslot_weekview_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag==getString(R.string.tag_before_sending_review)){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(getString(R.string.new_event),event);
                    toNewEventDetailBeforeSending(bundle);

                }else if (tag==getString(R.string.tag_before_sending_back)){
                    toInviteePicker(tag);
                }
            }
        });
        timeslotWeekView.setEventClassName(Event.class);
        timeslotWeekView.enableTimeSlot();
        timeslotWeekView.setOnTimeSlotOuterListener(new FlexibleLenViewBody.OnTimeSlotListener() {
           @Override
           public void onTimeSlotCreate(TimeSlotView timeSlotView) {
               TimeSlot timeSlot = new TimeSlot(EventUtil.generateTimeSlotUid(),
                       event.getEventUid(),
                       ((WeekView.TimeSlotStruct)timeSlotView.getTag()).startTime,
                       ((WeekView.TimeSlotStruct)timeSlotView.getTag()).endTime,
                       getString(R.string.timeslot_status_create),
                       0,
                       0);
               event.getTimeslots().add(timeSlot);
               WeekView.TimeSlotStruct struct = (WeekView.TimeSlotStruct)timeSlotView.getTag();
               struct.object =timeSlot;

           }

           @Override
           public void onTimeSlotClick(TimeSlotView timeSlotView) {
               // change status of view and struct
               boolean newStatus = !timeSlotView.isSelect();
               timeSlotView.setStatus(newStatus);
               ((WeekView.TimeSlotStruct)timeSlotView.getTag()).status = newStatus;


               // change event attributes
               TimeSlot calendarTimeSlot = (TimeSlot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
               TimeSlot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
               if (timeSlot!=null) {
                   if (timeSlot.getStatus() == getString(R.string.timeslot_status_create)) {
                       timeSlot.setStatus(getString(R.string.timeslot_status_pending));
                   } else if (timeSlot.getStatus() == getString(R.string.timeslot_status_pending)) {
                       timeSlot.setStatus(getString(R.string.timeslot_status_create));
                   }
               }else{
                   Log.i("error", "onTimeSlotClick: " + "no timeslot found");
               }
           }

           @Override
           public void onTimeSlotDragStart(TimeSlotView timeSlotView) {

           }

           @Override
           public void onTimeSlotDragging(TimeSlotView timeSlotView, int i, int i1) {

           }

           @Override
           public void onTimeSlotDragDrop(TimeSlotView timeSlotView) {
               TimeSlot calendarTimeSlot = (TimeSlot) ((WeekView.TimeSlotStruct)timeSlotView.getTag()).object;
               TimeSlot timeSlot = TimeSlotUtil.getTimeSlot(event, calendarTimeSlot);
               if (timeSlot!=null) {
                   timeSlot.setStartTime(timeSlotView.getStartTimeM());
                   timeSlot.setEndTime(timeSlotView.getEndTimeM());
               }
           }
       });
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
        hourWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                pickHour[0] = Integer.parseInt(createHours().get(position));
                durationTime.setText(getTimeString(pickHour[0], pickMinute[0]));
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
        minuteWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                pickMinute[0] = Integer.parseInt(createMinutes().get(position));
                durationTime.setText(getTimeString(pickHour[0], pickMinute[0]));
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
                    event.setTimeslots(new ArrayList<TimeSlot>());
                }
                for (TimeSlot timeSlot: event.getTimeslots()){
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
//
    public void initData(){
        //        ******************************simulate data
        // simulate timeSlots
//        WeekView.TimeSlotStruct
        Map simulateTimeSlots = new HashMap<>();
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,29);
        calendar.set(Calendar.HOUR_OF_DAY,5);
        calendar.set(Calendar.MINUTE,30);
        simulateTimeSlots.put(calendar.getTime().getTime(),false);
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setTimeSlotUid(EventUtil.generateTimeSlotUid());
        timeSlot1.setStatus(getString(R.string.timeslot_status_create));
        timeSlot1.setEventUid(event.getEventUid());
        timeSlot1.setStartTime(calendar.getTimeInMillis());
        timeSlot1.setEndTime(calendar.getTimeInMillis() + 3600000);
        timeSlots.add(timeSlot1);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH,30);
        calendar1.set(Calendar.HOUR_OF_DAY,7);
        calendar1.set(Calendar.MINUTE,45);
        simulateTimeSlots.put(calendar1.getTime().getTime(),false);
        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setTimeSlotUid(EventUtil.generateTimeSlotUid());
        timeSlot2.setEventUid(event.getEventUid());
        timeSlot2.setStatus(getString(R.string.timeslot_status_create));
        timeSlot2.setStartTime(calendar1.getTimeInMillis());
        timeSlot2.setEndTime(calendar1.getTimeInMillis() + 3600000);
        timeSlots.add(timeSlot2);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 31);
        calendar2.set(Calendar.HOUR_OF_DAY,9);
        calendar2.set(Calendar.MINUTE,0);
        simulateTimeSlots.put(calendar2.getTimeInMillis(),false);
        TimeSlot timeSlot3 = new TimeSlot();
        timeSlot3.setStatus(getString(R.string.timeslot_status_create));
        timeSlot3.setTimeSlotUid(EventUtil.generateTimeSlotUid());
        timeSlot3.setEventUid(event.getEventUid());
        timeSlot3.setStartTime(calendar2.getTimeInMillis());
        timeSlot3.setEndTime(calendar2.getTimeInMillis()+3600000);
        timeSlots.add(timeSlot3);

        event.setTimeslots(timeSlots);

        timeslotWeekView.setEventClassName(Event.class);
        timeslotWeekView.setDayEventMap(EventManager.getInstance().getEventsMap());
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void toNewEventDetailBeforeSending(Bundle bundle) {
        ((EventCreateActivity)getActivity()).toNewEventDetailBeforeSending(this, bundle);
    }

    @Override
    public void toInviteePicker(String tag) {
        ((EventCreateActivity)getActivity()).backToInviteePicker(this,tag);
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext());
    }
}
