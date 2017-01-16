package org.unimelb.itime.ui.fragment.event;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TimeslotBaseMvpView;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.dayview.TimeSlotController;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Paul on 27/08/2016.
 * timeslot: weekview
 * in created event flow
 */
public class EventTimeSlotViewFragment extends BaseUiAuthFragment<TimeslotBaseMvpView, TimeslotPresenter<TimeslotBaseMvpView>> implements TimeslotBaseMvpView{

    public static final int REQ_TIMESLOT = 1000;

    private FragmentEventCreateTimeslotViewBinding binding;
    private Event event;
    private RelativeLayout durationRelativeLayout;
    private LayoutInflater inflater;
    private WheelView timeWheelView;
    private WeekView timeslotWeekView;
    private int timePosition = 3;

    private long weekStartTime = -1;
    private EventCreateTimeslotViewModel viewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    private List<WrapperTimeSlot> timeslotWrapperList = null;

    public static final int TASK_EDIT = 1;
    public static final int TASK_VIEW = 2;
    private int fragment_task = -1;
    private boolean displayTimeslot= true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_create_timeslot_view, container, false);
        return binding.getRoot();
    }

    @Override
    public TimeslotPresenter<TimeslotBaseMvpView> createPresenter() {
        return new TimeslotPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timeslotWeekView = (WeekView) binding.getRoot().findViewById(R.id.timeslot_week_view);
        viewModel = new EventCreateTimeslotViewModel(getPresenter());
        viewModel.setTask(fragment_task);
        viewModel.setEvent(event);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));

        binding.setTimeslotVM(viewModel);
        binding.setToolbarVM(toolbarViewModel);
        inflater = LayoutInflater.from(getContext());

        initData();
        initListeners();

        if(weekStartTime > 0){
            timeslotWeekView.scrollToWithOffset(weekStartTime);
        }

        if (event.getTimeslot().size()>0) {
            Timeslot timeslot = event.getTimeslot().get(0);
            long duration = timeslot.getEndTime() - timeslot.getStartTime();
            timePosition = getTimePosition(duration);
        }

        viewModel.setDurationTimeString(EventUtil.getDurationTimes().get(timePosition));
        timeslotWeekView.updateTimeSlotsDuration(EventUtil.getDurationInMintues(timePosition) * 60 * 1000, false);
        timeslotWeekView.reloadTimeSlots(false); // for page refresh

        this.showAnimation();
    }


    /**
     * for create a new event
     * @param event
     */
    public void setData(Event event){
        this.event = event;
    }

    /**
     * for edit event
     * @param event
     * @param wrapperList
     */
    public void setData(Event event, List<WrapperTimeSlot> wrapperList){
        this.event = event;
        this.timeslotWrapperList = wrapperList;
    }


    private void initData(){
        if (displayTimeslot) {
            if (timeslotWrapperList == null && event.hasTimeslots()) {
                // when create event, and jump page from invitee page
                timeslotWrapperList = new ArrayList<>();
                for (Timeslot timeSlot : event.getTimeslot()) {
                    WrapperTimeSlot wrapper = new WrapperTimeSlot(timeSlot);
                    if (fragment_task == TASK_EDIT) {
                        // create timeslots
                        wrapper.setSelected(true);
                    } else if (fragment_task == TASK_VIEW) {
                        // just view timeslots
                        if (timeSlot.getStatus().equals(Timeslot.STATUS_ACCEPTED)) {
                            wrapper.setSelected(true);
                        } else {
                            wrapper.setSelected(false);
                        }
                    }
                    timeslotWeekView.addTimeSlot(wrapper);
                    timeslotWrapperList.add(wrapper);
                }


            } else if (timeslotWrapperList != null) {
                // jump page when already has timeslots
                for (WrapperTimeSlot wrapper : timeslotWrapperList) {
                    timeslotWeekView.addTimeSlot(wrapper);
                }
            }

            if (fragment_task == TASK_EDIT) {
                if (event != null || event.getTimeslot() == null || event.getTimeslot().size() == 0) {
                    Calendar calendar = Calendar.getInstance();
                    presenter.getTimeSlots(event, calendar.getTimeInMillis());
                }
                timeslotWeekView.reloadTimeSlots(false);
            }
        }
    }

    private int getTimePosition(long duration){
        int minute = (int) (duration/1000/60);
        int[] arr = {15, 30, 45, 60, 120, 180, 240, 300, 360, 720, 1440};
        for(int i = 0; i < arr.length; i++){
            if(minute <= arr[i]){
                return i;
            }
        }
        return 0;
    }

    private void initListeners() {
        if (fragment_task == TASK_EDIT){
            editInitListener();
        }else if (fragment_task == TASK_VIEW){
            viewInitListener();
        }
    }

    private void editInitListener(){
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_part);
        durationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initWheelPickers();
            }
        });
        timeslotWeekView.setEventClassName(Event.class);
        //Enable function of creating time block
        timeslotWeekView.enableTimeSlot();
        timeslotWeekView.setDayEventMap(EventManager.getInstance(getContext()).getEventsPackage());
        timeslotWeekView.setOnTimeSlotOuterListener(new TimeSlotController.OnTimeSlotListener() {
            @Override
            public void onTimeSlotCreate(DraggableTimeSlotView timeSlotView) {
                // popup timeslot create page
                EventTimeSlotCreateFragment fragment = new EventTimeSlotCreateFragment();
                Timeslot timeslot = new Timeslot();
                timeslot.setStartTime(timeSlotView.getNewStartTime());
                timeslot.setEndTime(timeSlotView.getNewEndTime());
                fragment.setTimeslot(timeslot);

                //todo change the code
                fragment.setTargetFragment(EventTimeSlotViewFragment.this, REQ_TIMESLOT);
                getBaseActivity().openFragment(fragment);
            }

            @Override
            public void onTimeSlotClick(DraggableTimeSlotView view) {
                WrapperTimeSlot wrapper = view.getWrapper();
                if (getSelectedTimeslotNum() > 7) {
                    if (wrapper.isSelected()) {
                        view.setIsSelected(false);
                    } else {
                        Toast.makeText(getContext(), "cannot select, maximum 7 timeslots selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    view.setIsSelected(!wrapper.isSelected());
                }
            }

            @Override
            public void onTimeSlotDragStart(DraggableTimeSlotView timeSlotView) {

            }

            @Override
            public void onTimeSlotDragging(DraggableTimeSlotView timeSlotView, int i, int i1) {

            }

            @Override
            public void onTimeSlotDragDrop(DraggableTimeSlotView timeSlotView, long startTime, long endTime) {
                Timeslot timeslot = (Timeslot) timeSlotView.getTimeslot();
                timeslot.setStartTime(startTime);
                timeslot.setEndTime(endTime);
                timeslotWeekView.reloadTimeSlots(false);
            }

        });
    }

    private void viewInitListener(){
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_part);
        durationRelativeLayout.setVisibility(View.GONE);
        timeslotWeekView.setEventClassName(Event.class);
        //Enable function of creating time block
        timeslotWeekView.setDayEventMap(EventManager.getInstance(getContext()).getEventsPackage());
        timeslotWeekView.setOnTimeSlotOuterListener(new TimeSlotController.OnTimeSlotListener() {
            @Override
            public void onTimeSlotCreate(DraggableTimeSlotView draggableTimeSlotView) {

            }

            @Override
            public void onTimeSlotClick(DraggableTimeSlotView draggableTimeSlotView) {
                WrapperTimeSlot wrapper = draggableTimeSlotView.getWrapper();
                if (EventUtil.isUserHostOfEvent(getContext(), event)){
                    // when user is host, can only select one timeslot
                    if (wrapper.isSelected()){
                        wrapper.setSelected(false);
                    }else{
                        // wrapper is unselected, need to check number of timeslots has selected
                        if (getSelectedTimeslotNum()>=1){
                            Toast.makeText(getContext(), "cannot select, please unselect one timeslot", Toast.LENGTH_SHORT).show();
                        }else{
                            wrapper.setSelected(true);
                        }
                    }
                }else{
                    wrapper.setSelected(!wrapper.isSelected());
                }
                timeslotWeekView.reloadTimeSlots(false);
            }

            @Override
            public void onTimeSlotDragStart(DraggableTimeSlotView draggableTimeSlotView) {

            }

            @Override
            public void onTimeSlotDragging(DraggableTimeSlotView draggableTimeSlotView, int i, int i1) {

            }

            @Override
            public void onTimeSlotDragDrop(DraggableTimeSlotView draggableTimeSlotView, long l, long l1) {

            }
        });
    }

    private int getSelectedTimeslotNum(){
        int num = 0;
        for(WrapperTimeSlot wrapper : this.timeslotWrapperList){
            if(wrapper.isSelected()){
                num ++;
            }
        }
        return num;
    }

    private void initWheelPickers() {
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
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.duration_part), Gravity.BOTTOM, 0, -600);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewModel.setDurationTimeString(EventUtil.getDurationTimes().get(timePosition));
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

    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<Timeslot> data) {
        if (!event.hasTimeslots()) {
            event.setTimeslot(new ArrayList<Timeslot>());
        }
        for (Timeslot timeSlot : data) {
            if (!EventManager.getInstance(getContext()).isTimeslotExistInEvent(event, timeSlot)) {
                // have to do this
                timeSlot.setEventUid(event.getEventUid());
                timeSlot.setStatus(Timeslot.STATUS_CREATING);
                WrapperTimeSlot wrapper = new WrapperTimeSlot(timeSlot);
                timeslotWeekView.addTimeSlot(wrapper);
                timeslotWrapperList.add(wrapper);
            }
        }
        viewModel.setEvent(event);
        timeslotWeekView.reloadTimeSlots(false);
    }

    @Override
    public void onTaskError(int taskId) {

    }

    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        if (fragment_task == TASK_EDIT) {
            List<Timeslot> list = new ArrayList<>();
            for (WrapperTimeSlot wrapper : this.timeslotWrapperList) {
                if (wrapper.isSelected()) {
                    wrapper.getTimeSlot().setStatus(Timeslot.STATUS_PENDING);
                    list.add((Timeslot) wrapper.getTimeSlot());
                }
            }
            TimeSlotUtil.sortTimeslot(list);
            event.setTimeslot(list);
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            EventEditFragment fragment = new EventEditFragment();
            fragment.setEvent(event);
            getBaseActivity().openFragment(fragment, null, false);
        }else if (fragment_task == TASK_VIEW){

            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            // TODO: 14/1/17 update invitee response
            EventDetailFragment eventDetailFragment = new EventDetailFragment();
            eventDetailFragment.setData(event ,timeslotWrapperList);
            getBaseActivity().backFragment(eventDetailFragment);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_TIMESLOT){
            weekStartTime = data.getLongExtra("weekStartTime", 0);
        }

        if (requestCode == REQ_TIMESLOT && resultCode == EventTimeSlotCreateFragment.RET_TIMESLOT) {
            long startTime = data.getLongExtra("startTime", 0);
            long endTime = data.getLongExtra("endTime", 0);
            Timeslot timeslot = new Timeslot();
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);
            timeslot.setEventUid(event.getEventUid());
            timeslot.setTimeslotUid(AppUtil.generateUuid());
            event.getTimeslot().add(timeslot);
            WrapperTimeSlot wrapper = new WrapperTimeSlot(timeslot);
            wrapper.setSelected(true);
            timeslotWrapperList.add(wrapper);
        }
    }

    public void setFragment_task(int fragment_task) {
        this.fragment_task = fragment_task;
    }

    public void setDisplayTimeslot(boolean displayTimeslot) {
        this.displayTimeslot = displayTimeslot;
    }

    private void showAnimation(){
        if (displayTimeslot){
            timeslotWeekView.showTimeslotAnim(event.getTimeslot());
        }else {
            timeslotWeekView.showEventAnim(event);
        }
    }


}
