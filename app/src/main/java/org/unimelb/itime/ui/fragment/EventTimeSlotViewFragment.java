package org.unimelb.itime.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;

import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.timeslotview.WeekTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventTimeSlotViewFragment extends MvpFragment<EventCreateNewTimeSlotMvpView,EventCreateTimeSlotPresenter>
        implements EventCreateNewTimeSlotMvpView{


    FragmentEventCreateTimeslotViewBinding binding;
    EventCreateTimeslotViewModel eventCreateTimeslotViewModel;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView hourWheelView,minuteWheelView;

    public EventTimeSlotViewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view, container, false);
        this.inflater = inflater;
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventCreateTimeslotViewModel = new EventCreateTimeslotViewModel(getPresenter());
        binding.setTimeslotVM(eventCreateTimeslotViewModel);
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_relative_layout);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCreateTimeslotViewModel.setIsChangeDuration(true);
                initWheelPickers();
            }
        });
        initData();
    }

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

        PopupWindow popupWindow = new PopupWindow(root,ViewGroup.LayoutParams.MATCH_PARENT,850);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationPopup);
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.bottom_bar), Gravity.BOTTOM,0, -600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                eventCreateTimeslotViewModel.setDurationTimeString(getTimeString(pickHour[0],pickMinute[0]));
                eventCreateTimeslotViewModel.setIsChangeDuration(true);
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
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                list.add(String.valueOf(i));
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> createMinutes() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                list.add("" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    public void initData(){
        //        ******************************simulate data
        WeekTimeSlotView weekTimeSlotView = (WeekTimeSlotView) binding.getRoot().findViewById(R.id.week_time_slot_view);
        // simulate timeSlots
        Map<Long,Boolean> simulateTimeSlots = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,29);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);
        simulateTimeSlots.put(calendar.getTime().getTime(),false);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH,30);
        calendar1.set(Calendar.HOUR_OF_DAY,8);
        calendar1.set(Calendar.MINUTE,45);
        simulateTimeSlots.put(calendar1.getTime().getTime(),true);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 30);
        calendar2.set(Calendar.HOUR_OF_DAY,4);
        calendar2.set(Calendar.MINUTE,0);
        simulateTimeSlots.put(calendar2.getTimeInMillis(),false);

        weekTimeSlotView.setTimeSlots(simulateTimeSlots,60);

        // simulate Events
//        Event event = new Event();
////        Event event = new Event();
//        event.setTitle("itime meeting");
//        event.setStatus(Event.Status.COMFIRM); // 5== pending, 6== confirm
//        event.setEventType(1); //0 == private, 1== group, 2== public
//        Calendar calendar1 =Calendar.getInstance();
//        calendar1.set(Calendar.DAY_OF_MONTH,30);
//        calendar1.set(Calendar.HOUR_OF_DAY,4);
//        calendar1.set(Calendar.MINUTE,15);
//        calendar1.set(Calendar.SECOND,0);
//        event.setStartTime(calendar1.getTimeInMillis());
//        event.setEndTime(calendar1.getTimeInMillis() + 3600000*2);
//
//        WeekView weekView = (WeekView) binding.getRoot().findViewById(R.id.week_view);
//        weekView.setEvent(event);
//
//        weekTimeSlotView.setEvent(event);
//
    }



    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext(), inflater);
    }

    @Override
    public void toNewEventDetailBeforeSending() {
        ((EventCreateActivity)getActivity()).toNewEventDetailBeforeSending(this);
    }

}
