package org.unimelb.itime.ui.fragment.event;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventCreateTimeslotViewBinding;
import org.unimelb.itime.databinding.TimeslotViewResponseBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TimeslotBaseMvpView;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.ui.viewmodel.EventCreateTimeslotViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.TimeslotViewResponseViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.SizeUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.dayview.TimeSlotController;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;
import org.unimelb.itime.vendor.weekview.WeekView;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


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

    //
    private long showingTime = -1;
    private EventCreateTimeslotViewModel viewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    private List<WrapperTimeSlot> timeslotWrapperList = null;

    public static final int TASK_EDIT = 1;
    public static final int TASK_VIEW = 2;
    private int fragment_task = -1;
    private boolean displayTimeslot= true;

    private Map<String, List<EventUtil.StatusKeyStruct>> replyData = null;

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

        // set up title strings
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        if (!event.getStatus().equals(Event.STATUS_CONFIRMED)) {
            toolbarViewModel.setRightTitleStr(getString(R.string.done));
        }else{
            toolbarViewModel.setRightClickable(false);
        }
        // use event start time to set the current title string date
        // TODO: 20/1/17 the time is inaccurate, sometimes when the event is on a 1:00 of Sunday, it will display last week
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK,calendar.getFirstDayOfWeek());
        toolbarViewModel.setTitleStr(getTitleString(calendar.getTimeInMillis()));

        binding.setTimeslotVM(viewModel);
        binding.setToolbarVM(toolbarViewModel);
        inflater = LayoutInflater.from(getContext());

        initData();
        initListeners();

        // scroll to the showing time
        if(showingTime > 0){
            timeslotWeekView.scrollToWithOffset(showingTime);
        }

        if (event.getTimeslot().size()>0) {
            Timeslot timeslot = event.getTimeslot().get(0);
            long duration = timeslot.getEndTime() - timeslot.getStartTime();
            timePosition = getTimePosition(duration);
        }

        viewModel.setDurationTimeString(EventUtil.getDurationTimes().get(timePosition));
        timeslotWeekView.updateTimeSlotsDuration(EventUtil.getDurationInMintues(timePosition) * 60 * 1000, false);
        timeslotWeekView.reloadTimeSlots(false); // for page refreshEventManager

        this.showAnimation();
    }


    /**
     * for create a new event
     * @param event
     */
    public void setData(Event event){
        this.event = event;
        showingTime = event.getStartTime();
        replyData = EventUtil.getAdapterData(event);
    }

    /**
     * for edit event
     * @param event
     * @param wrapperList
     */
    public void setData(Event event, List<WrapperTimeSlot> wrapperList){
        this.event = event;
        this.timeslotWrapperList = wrapperList;
        showingTime = event.getStartTime();
        replyData = EventUtil.getAdapterData(event);
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
                if (event != null || !event.hasTimeslots() || event.getTimeslot().size() == 0) {
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
                final WrapperTimeSlot wrapper = draggableTimeSlotView.getWrapper();
                int inviteeVisibility = event.getInviteeVisibility();
                if (EventUtil.isUserHostOfEvent(getContext(), event)) {
                    // when user is host, can only select one timeslot
                    if (getSelectedTimeslotNum() >= 1 && !wrapper.isSelected()) {
                        Toast.makeText(getContext(), "cannot select, please unselect one timeslot", Toast.LENGTH_SHORT).show();
                    } else {
                        // wrapper.setSelected(false);
                        popupTimeslotInfo(wrapper);
                    }
                } else {
                    if (inviteeVisibility == 0) {
                        wrapper.setSelected(!wrapper.isSelected());
                    } else {
                        popupTimeslotInfo(wrapper);
                    }
                }
                timeslotWeekView.reloadTimeSlots(false);

                int breakpoint;
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
        timeslotWeekView.removeAllOptListener();
    }

    /**
     * Created by Xiaojie on 27/01/2017.
     */
    private void popupTimeslotInfo(final WrapperTimeSlot wrapper) {
        /*
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Please enter a password");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wrapper.setSelected(!wrapper.isSelected());
                timeslotWeekView.reloadTimeSlots(false);

            }
        });
        dialog.setNegativeButton("CANCEL", null);
        dialog.show();
        */
        ITimeTimeSlotInterface timeslot = wrapper.getTimeSlot();
        String timeslotUid = timeslot.getTimeslotUid();
        long startTime = timeslot.getStartTime();
        long endTime = timeslot.getEndTime();

        List<EventUtil.StatusKeyStruct> responses = replyData.get(timeslotUid);
        EventUtil.StatusKeyStruct responseAccepted = responses.get(0);
        List<String> inviteesAccepted = new ArrayList<String>();
        for (Invitee invitee:responseAccepted.getInviteeList())
            inviteesAccepted.add(invitee.getAliasName());
        EventUtil.StatusKeyStruct responseRejected = responses.get(1);
        List<String> inviteesRejected = new ArrayList<String>();
        for (Invitee invitee:responseRejected.getInviteeList())
            inviteesRejected.add(invitee.getAliasName());
        EventUtil.StatusKeyStruct responseNoResponse = responses.get(2);
        List<String> inviteeNoResponse = new ArrayList<String>();
        for (Invitee invitee:responseNoResponse.getInviteeList())
            inviteeNoResponse.add(invitee.getAliasName());

        Context context = getContext();
        List<String> times = EventUtil.getTimeslotViewResponseFromLong(context, startTime, endTime);

        // TimeslotViewResponseBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.timeslot_view_response, null, false);
        TimeslotViewResponseBinding binding = TimeslotViewResponseBinding.inflate(LayoutInflater.from(context));
        TimeslotViewResponseViewModel viewModel = new TimeslotViewResponseViewModel();
        binding.setViewModel(viewModel);
        viewModel.setInviteesAccepted(inviteesAccepted);
        viewModel.setInviteesRejected(inviteesRejected);
        viewModel.setInviteesNoResponse(inviteeNoResponse);
        viewModel.setTimeRange(times.get(0));
        viewModel.setDayOfWeek(times.get(1));
        if (inviteesAccepted.size() == 0)
            viewModel.setShowAccepted(false);
        if (inviteesRejected.size() == 0)
            viewModel.setShowRejected(false);
        if (inviteeNoResponse.size() == 0)
            viewModel.setShowNoResponse(false);
        if (wrapper.isSelected()) {
            viewModel.setRightButtonText("Unselect");
        } else {
            viewModel.setRightButtonText("Select");
        }

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View rootView = binding.getRoot();
//        LinearLayout.LayoutParams rootViewParams = new LinearLayout.LayoutParams(
//                SizeUtil.dp2px(context, 400), SizeUtil.dp2px(context, 400));
//        rootView.setLayoutParams(rootViewParams);
//        rootView.measure(SizeUtil.dp2px(context, 400), SizeUtil.dp2px(context, 400));
        dialog.setContentView(rootView);

        Button buttonSelect = (Button) dialog.findViewById(R.id.timeslot_view_response_select);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrapper.setSelected(!wrapper.isSelected());
                timeslotWeekView.reloadTimeSlots(false);
                dialog.dismiss();
            }
        });
        Button buttonCancel = (Button) dialog.findViewById(R.id.timeslot_view_response_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(SizeUtil.dp2px(context, 270.0), SizeUtil.dp2px(context, 367.5));
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

                for (WrapperTimeSlot wrapper: timeslotWrapperList){
                    wrapper.getTimeSlot().setEndTime(wrapper.getTimeSlot().getStartTime() + EventUtil.getDurationInMintues(timePosition) * 60 * 1000);
                }

                timeslotWeekView.reloadTimeSlots(false); // for page refreshEventManager
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
            if (!isTimeslotExistInWrapperList(timeslotWrapperList, timeSlot)) {
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

    private boolean isTimeslotExistInWrapperList(List<WrapperTimeSlot> wrapperTimeSlots, Timeslot timeslot){
        if (wrapperTimeSlots!=null && wrapperTimeSlots.size()>0) {
            for (WrapperTimeSlot wrapper : wrapperTimeSlots){
                long tsSecond= wrapper.getTimeSlot().getStartTime()/(1000*60*15);
                long recSecond = timeslot.getStartTime()/(1000*60*15);
                if(tsSecond == recSecond){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onBack() {
        event.setHighLighted(false);
        getFragmentManager().popBackStack();
    }

    @Override
    public void onNext() {
        event.setHighLighted(false);
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
            showingTime = data.getLongExtra("showingTime", 0);
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
            event.setHighLighted(true);
            timeslotWeekView.showEventAnim(event);
        }
    }

    /**
     * this is for tool bar showing current week date
     * @param weekStartTime
     */
    @Override
    public void getWeekStartTime(long weekStartTime) {
        toolbarViewModel.setTitleStr(getTitleString(weekStartTime));
    }

    private String getTitleString(long weekStartTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(weekStartTime);
        SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
        String title = df.format(calendar.getTime());
        return title;
    }
}
