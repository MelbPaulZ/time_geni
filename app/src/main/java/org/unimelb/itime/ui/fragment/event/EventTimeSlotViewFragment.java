package org.unimelb.itime.ui.fragment.event;

import android.content.Intent;
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
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.TimeSlotController;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;

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
    private int timePosition;

    private EventCreateTimeslotViewModel viewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

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
        viewModel.setEvent(event);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.back));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
        binding.setTimeslotVM(viewModel);
        binding.setToolbarVM(toolbarViewModel);
        inflater = LayoutInflater.from(getContext());

        initData();
    }

    public void setEvent(Event event){
        this.event = event;
    }

    private void initData(){
        initListeners();
        resetCalendar(); // try

        if (event != null || event.getTimeslot() == null || event.getTimeslot().size() == 0) {
            Calendar calendar = Calendar.getInstance();
            presenter.getTimeSlots(event, calendar.getTimeInMillis());
        }
        timeslotWeekView.reloadTimeSlots(false);
    }

    public void resetCalendar() {
        timeslotWeekView.resetTimeSlots();
        if (event.hasTimeslots()) {
            for (Timeslot timeSlot : event.getTimeslot()) {
                timeslotWeekView.addTimeSlot(timeSlot,getTimeSlotIsSelected(timeSlot));
            }
        }
        timeslotWeekView.reloadTimeSlots(false);
//        scrollToFstTimeSlot(event);
    }

    private void scrollToFstTimeSlot(Event event){
        List<Timeslot> slots = event.getTimeslot();
        if (slots.size() > 0){
            timeslotWeekView.scrollToWithOffset(slots.get(0).getStartTime());
        }
    }

    private void changeTimeSlots(DraggableTimeSlotView timeSlotView) {
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

    public void createTimeSlot(DraggableTimeSlotView timeSlotView) {
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
        durationRelativeLayout = (RelativeLayout) getActivity().findViewById(R.id.duration_part);
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
            public void onTimeSlotClick(DraggableTimeSlotView timeSlotView) {
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
            public void onTimeSlotDragStart(DraggableTimeSlotView timeSlotView) {

            }

            @Override
            public void onTimeSlotDragging(DraggableTimeSlotView timeSlotView, int i, int i1) {

            }

            @Override
            public void onTimeSlotDragDrop(DraggableTimeSlotView timeSlotView, long startTime, long endTime) {
                timeslotDrop(timeSlotView, startTime, endTime);
            }

        });
    }

    private void timeslotDrop(DraggableTimeSlotView timeSlotView, long startTime, long endTime) {
        // update timeslot info
        Timeslot timeslot = (Timeslot) timeSlotView.getTimeslot();
        timeslot.setStartTime(startTime);
        timeslot.setEndTime(endTime);
        timeslotWeekView.reloadTimeSlots(false);

    }

    private boolean getTimeSlotIsSelected(Timeslot timeslot){
        if (timeslot.getStatus().equals(Timeslot.STATUS_CREATING)){
            return false;
        }else if (timeslot.getStatus().equals(Timeslot.STATUS_PENDING)){
            return true;
        }

        return false;
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
        popupWindow.showAtLocation(binding.getRoot().findViewById(R.id.duration_part), Gravity.BOTTOM, 0, -600);
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


    @Override
    public void onTaskStart(int task) {

    }

    @Override
    public void onTaskSuccess(int taskId, List<Timeslot> data) {
        if (!event.hasTimeslots()) {
            event.setTimeslot(new ArrayList<Timeslot>());
        }
        for (Timeslot timeSlot : data) {
            if (EventManager.getInstance(getContext()).isTimeslotExistInEvent(event, timeSlot)) {
                // already exist, then do nothing
            } else {
                // have to do this
                timeSlot.setEventUid(event.getEventUid());
                timeSlot.setStatus(Timeslot.STATUS_CREATING);
                event.getTimeslot().add(timeSlot);
                timeslotWeekView.addTimeSlot(timeSlot);
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
        TimeSlotUtil.sortTimeslot(event.getTimeslot());
        EventEditFragment fragment = new EventEditFragment();
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_TIMESLOT && resultCode == EventTimeSlotCreateFragment.RET_TIMESLOT) {
            long startTime = data.getLongExtra("startTime", 0);
            long endTime = data.getLongExtra("endTime", 0);
            Timeslot timeslot = new Timeslot();
            timeslot.setStartTime(startTime);
            timeslot.setEndTime(endTime);
            timeslot.setEventUid(event.getEventUid());
            timeslot.setTimeslotUid(AppUtil.generateUuid());
            event.getTimeslot().add(timeslot);
        }
    }
}
