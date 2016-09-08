package org.unimelb.itime.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
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
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.helper.FragmentTagListener;
import org.unimelb.itime.messageevent.MessageAttendeeEvent;
import org.unimelb.itime.messageevent.MessageEventEvent;
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
        implements EventCreateNewTimeSlotMvpView,FragmentTagListener{


    FragmentEventCreateTimeslotViewBinding binding;
    EventCreateTimeslotViewModel eventCreateTimeslotViewModel;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView hourWheelView,minuteWheelView;
    WeekTimeSlotView weekTimeSlotView;
    Map<Long,Boolean> simulateTimeSlots;
    private Event newEvent;
    private String tag;

    public void setEvent(Event event){
        this.newEvent = event;
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
        Bundle bundle = getArguments();
        this.newEvent = (Event) bundle.getSerializable(getString(R.string.new_event));

        initData();
        eventCreateTimeslotViewModel = new EventCreateTimeslotViewModel(getPresenter(),this.newEvent);
        eventCreateTimeslotViewModel.setTag(this.tag);
        binding.setTimeslotVM(eventCreateTimeslotViewModel);
        initListeners();
    }

    public void initListeners(){
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_relative_layout);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventCreateTimeslotViewModel.setIsChangeDuration(true);
                initWheelPickers();
            }
        });

        TextView timeSlotDoneBtn = (TextView) binding.getRoot().findViewById(R.id.timeslot_view_done_btn);
        timeSlotDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.new_event),newEvent);
                toNewEventDetailBeforeSending(bundle);
            }
        });
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


        final PopupWindow popupWindow = new PopupWindow(root,ViewGroup.LayoutParams.MATCH_PARENT,850);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.AnimationPopup);
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.bottom_bar), Gravity.BOTTOM,0, -600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                eventCreateTimeslotViewModel.setDurationTimeString(getTimeString(pickHour[0],pickMinute[0]));
                eventCreateTimeslotViewModel.setIsChangeDuration(true);
                weekTimeSlotView.setTimeSlots(simulateTimeSlots, pickHour[0] * 60 + pickMinute[0]);
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

    public void initData(){
        //        ******************************simulate data
        weekTimeSlotView = (WeekTimeSlotView) binding.getRoot().findViewById(R.id.week_time_slot_view);
        // simulate timeSlots
        simulateTimeSlots = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,6);
        calendar.set(Calendar.HOUR_OF_DAY,7);
        calendar.set(Calendar.MINUTE,30);
        simulateTimeSlots.put(calendar.getTime().getTime(),false);


        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH,7);
        calendar1.set(Calendar.HOUR_OF_DAY,8);
        calendar1.set(Calendar.MINUTE,45);
        simulateTimeSlots.put(calendar1.getTime().getTime(),true);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, 9);
        calendar2.set(Calendar.HOUR_OF_DAY,4);
        calendar2.set(Calendar.MINUTE,0);
        simulateTimeSlots.put(calendar2.getTimeInMillis(),false);

        weekTimeSlotView.setTimeSlots(simulateTimeSlots,60);

//        newEvent = new Event();
        newEvent.setProposedTimeSlots(new ArrayList(simulateTimeSlots.keySet()));
        newEvent.setDuration(60);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }



    @Override
    public EventCreateTimeSlotPresenter createPresenter() {
        return new EventCreateTimeSlotPresenter(getContext(), inflater);
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
}
