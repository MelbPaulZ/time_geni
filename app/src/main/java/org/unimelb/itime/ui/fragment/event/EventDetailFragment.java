package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventDetailBinding;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel.SubTimeslotViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.TimeSlotUtil;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment.TASK_VIEW;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailFragment extends BaseUiAuthFragment<EventDetailMvpView, EventPresenter<EventDetailMvpView>> implements EventDetailMvpView {
    private FragmentEventDetailBinding binding;
    private Event event;

    private EventDetailViewModel contentViewModel;
    private ToolbarViewModel<EventDetailMvpView> toolbarViewModel;

    private List<SubTimeslotViewModel> timeslotVMList;
    private Map<String, List<EventUtil.StatusKeyStruct>> replyData = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public EventPresenter<EventDetailMvpView> createPresenter() {
        return new EventPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new EventDetailViewModel(getPresenter());
        contentViewModel.setEvent(event);
        initTimeslotVMList();
        initToolbar();

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initTimeslotVMList(){
        // it needs to be puted on the begining, otherwise won't initialize
        contentViewModel.setInviteeList(EventUtil.getInviteeWithStatus(event.getInvitee(),"accepted","rejected"));
        if(timeslotVMList != null){
            // if other fragment set timeslotWrapper to this fragment
            contentViewModel.setWrapperTimeSlotList(timeslotVMList);
            return;
        }
        timeslotVMList = new ArrayList<>();
        TimeSlotUtil.sortTimeslot(event.getTimeslot());
        for (Timeslot timeslot: event.getTimeslot()){
            WrapperTimeSlot wrapper = new WrapperTimeSlot(timeslot);
            if (EventUtil.isUserHostOfEvent(getContext(), event)) {
                wrapper.setSelected(timeslot.getIsConfirmed() == 1);
            }else{
                wrapper.setSelected(timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED));
            }
            SubTimeslotViewModel subTimeslotViewModel = new SubTimeslotViewModel(this);
            subTimeslotViewModel.setWrapper(wrapper);
            subTimeslotViewModel.setHostEvent(EventUtil.isUserHostOfEvent(getContext(), event));
            subTimeslotViewModel.setReplyData(replyData);
            this.timeslotVMList.add(subTimeslotViewModel);
        }
        contentViewModel.setWrapperTimeSlotList(timeslotVMList);
    }

    private void initToolbar(){
        toolbarViewModel =
                new ToolbarViewModel<EventDetailMvpView>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        String title = EventUtil.isUserHostOfEvent(getContext(), event)?
                getString(R.string.event_details) : getString(R.string.new_invitation);
        toolbarViewModel.setTitleStr(title);
        if (EventUtil.isUserHostOfEvent(getContext(), event)) {
            toolbarViewModel.setRightTitleStr(getString(R.string.edit));
        }
    }


    public void setData(Event event) {
        this.event = event;
        replyData = EventUtil.getAdapterData(event);
    }

    public void setData(Event event, List<WrapperTimeSlot> wrapperList){
        this.event = event;
        if(wrapperList == null){
            return;
        }
        timeslotVMList = new ArrayList<>();
        replyData = EventUtil.getAdapterData(event);
        for(WrapperTimeSlot t: wrapperList){
            SubTimeslotViewModel vm = new SubTimeslotViewModel(this);
            vm.setWrapper(t);
            vm.setHostEvent(EventUtil.isUserHostOfEvent(getContext(), event));
            vm.setReplyData(replyData);
            vm.setIconSelected(t.isSelected());
            timeslotVMList.add(vm);
        }
    }

    private void toCalendar(int resultCode) {
        Intent intent = new Intent();

        getActivity().setResult(resultCode, intent);
        getActivity().finish();
    }


    @Override
    public void viewInCalendar() {
        EventTimeSlotViewFragment timeSlotViewFragment = new EventTimeSlotViewFragment();
        timeSlotViewFragment.setFragment_task(TASK_VIEW);
        Event cpyEvent = EventUtil.copyEvent(event);
        List<WrapperTimeSlot> wrapperTimeSlots = new ArrayList<>();
        for (SubTimeslotViewModel vm: timeslotVMList){
            vm.getWrapper().setSelected(vm.isIconSelected());
            wrapperTimeSlots.add(vm.getWrapper());
        }
        if (event.getStatus().equals(Event.STATUS_CONFIRMED)){
            // if the event is confirmed, then no timeslot showing
            timeSlotViewFragment.setData(cpyEvent);
            timeSlotViewFragment.setDisplayTimeslot(false);
        }else {
            timeSlotViewFragment.setData(cpyEvent, wrapperTimeSlots);
        }
        getBaseActivity().openFragment(timeSlotViewFragment);

    }

    @Override
    public void viewInviteeResponse(Timeslot timeSlot) {

        InviteeTimeslotFragment inviteeTimeslotFragment = new InviteeTimeslotFragment();
        inviteeTimeslotFragment.setData(this.event, replyData.get(timeSlot.getTimeslotUid()), timeSlot);
        getBaseActivity().openFragment(inviteeTimeslotFragment);
    }

    @Override
    public void gotoGridView() {
        EventPhotoGridFragment gridFragment = new EventPhotoGridFragment();
        gridFragment.setEvent(event);
        getBaseActivity().openFragment(gridFragment);
    }

    @Override
    public void onTimeslotClick(WrapperTimeSlot wrapper) {
        // this wrapper has been changed, if host, then has already two selected wrappers
        if (EventUtil.isUserHostOfEvent(getContext(), event)){
            if (wrapper.isSelected() && TimeSlotUtil.numberSelectedWrapper(timeslotVMList) >1){
                unselectWrappers(wrapper);
            }
        }
        contentViewModel.setWrapperTimeSlotList(timeslotVMList);

    }

    /**
     * this method unselect other timeslots, if the host select more than one timeslots
     * @param wrapper
     */
    private void unselectWrappers(WrapperTimeSlot wrapper){
        for (SubTimeslotViewModel viewModel: timeslotVMList){
            WrapperTimeSlot wrapperTimeSlot = viewModel.getWrapper();
            if (!wrapperTimeSlot.equals(wrapper)){
                wrapperTimeSlot.setSelected(false);
                viewModel.setIconSelected(false);
            }
        }
    }

    @Override
    public void toResponse() {
        EventResponseFragment eventResponseFragment = new EventResponseFragment();
        eventResponseFragment.setData(event);
        getBaseActivity().openFragment(eventResponseFragment);
    }

    @Override
    public void createEventFromThisTemplate(Event event) {
        Intent intent = new Intent(getActivity(), EventCreateActivity.class);
        intent.putExtra("event", (Serializable) event);
        startActivity(intent);
    }


    @Override
    public void onTaskStart(int task) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
        hideProgressDialog();
        if (taskId == EventPresenter.TASK_TIMESLOT_ACCEPT){
            toCalendar(Activity.RESULT_OK);
        }else if (taskId == EventPresenter.TASK_EVENT_CONFIRM){
            toCalendar(Activity.RESULT_OK);
        }else if (taskId == EventPresenter.TASK_TIMESLOT_REJECT){
            toCalendar(Activity.RESULT_OK);
        }else if (taskId == EventPresenter.TASK_BACK){
            toCalendar(Activity.RESULT_CANCELED);
        }else if (taskId == EventPresenter.TASK_EVENT_ACCEPT){
            toCalendar(Activity.RESULT_OK);
        }else if (taskId == EventPresenter.TASK_EVENT_REJECT){
            toCalendar(Activity.RESULT_OK);
        }
    }

    @Override
    public void onTaskError(int taskId) {
        hideProgressDialog();
    }


    @Override
    public void onBack() {
        toCalendar(EventPresenter.TASK_BACK);
    }

    @Override
    public void onNext() {
        if (EventUtil.isUserHostOfEvent(getContext(), event)) {
            EventEditFragment eventEditFragment = new EventEditFragment();
            Event cpyEvent = EventUtil.copyEvent(event);
            eventEditFragment.setEvent(cpyEvent);
            getBaseActivity().openFragment(eventEditFragment);
        }
    }
}
