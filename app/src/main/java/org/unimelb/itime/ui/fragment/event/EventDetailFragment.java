package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.FragmentEventDetailBinding;
import org.unimelb.itime.ui.mvpview.EventDetailMvpView;
import org.unimelb.itime.ui.presenter.EventCommonPresenter;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;
import org.unimelb.itime.ui.viewmodel.EventDetailViewModel.SubTimeslotViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment.TASK_VIEW;

/**
 * Created by Paul on 4/09/2016.
 */
public class EventDetailFragment extends BaseUiAuthFragment<EventDetailMvpView, EventCommonPresenter<EventDetailMvpView>> implements EventDetailMvpView {
    private FragmentEventDetailBinding binding;
    private Event event;

    private EventDetailViewModel contentViewModel;
    private ToolbarViewModel<EventDetailMvpView> toolbarViewModel;

    private List<SubTimeslotViewModel> timeslotVMList = null;
    private Map<String, List<EventUtil.StatusKeyStruct>> replyData = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        replyData = EventUtil.getAdapterData(event);

        contentViewModel = new EventDetailViewModel(getPresenter());
        contentViewModel.setEvent(event);
        contentViewModel.setReplyData(replyData);
        initTimeslotVMList();
        initToolbar();

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initTimeslotVMList(){
        if(timeslotVMList != null){
            // if other fragment set timeslotWrapper to this fragment
            return;
        }
        timeslotVMList = new ArrayList<>();
        for (Timeslot timeslot: event.getTimeslot()){
            WrapperTimeSlot wrapper = new WrapperTimeSlot(timeslot);
            if (EventUtil.isUserHostOfEvent(getContext(), event)) {
                wrapper.setSelected(timeslot.getIsConfirmed() == 1);
            }else{
                wrapper.setSelected(timeslot.getStatus().equals(Timeslot.STATUS_ACCEPTED));
            }
            SubTimeslotViewModel subTimeslotViewModel = new SubTimeslotViewModel();
            subTimeslotViewModel.setWrapper(wrapper);
            this.timeslotVMList.add(subTimeslotViewModel);
        }
        contentViewModel.setWrapperTimeSlotList(timeslotVMList);

    }

    private void initToolbar(){
        toolbarViewModel =
                new ToolbarViewModel<EventDetailMvpView>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.back));
        String title = EventUtil.isUserHostOfEvent(getContext(), event)?
                getString(R.string.event_details) : getString(R.string.new_invitation);
        toolbarViewModel.setTitleStr(title);
        toolbarViewModel.setRightTitleStr(getString(R.string.edit));
    }


    public void setData(Event event){
        this.event = event;
    }

    public void setData(Event event, List<WrapperTimeSlot> wrapperList){
        this.event = event;
        if(wrapperList == null){
            return;
        }
        timeslotVMList = new ArrayList<>();
        for(WrapperTimeSlot t: wrapperList){
            SubTimeslotViewModel vm = new SubTimeslotViewModel();
            vm.setWrapper(t);
            timeslotVMList.add(vm);
        }
    }



    @Override
    public EventCommonPresenter<EventDetailMvpView> createPresenter() {
        return new EventCommonPresenter<>(getContext());
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
            wrapperTimeSlots.add(vm.getWrapper());
        }
        timeSlotViewFragment.setData(cpyEvent, wrapperTimeSlots);
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
    public void onBack() {
        toCalendar(EventCommonPresenter.TASK_BACK);
    }

    @Override
    public void onNext() {
        EventEditFragment eventEditFragment = new EventEditFragment();
        eventEditFragment.setEvent(this.event);
        getBaseActivity().openFragment(eventEditFragment);
//        EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
//        Event cpyEvent = EventManager.getInstance(getContext()).copyCurrentEvent(event);
//        for (Timeslot timeslot: cpyEvent.getTimeslot()){
//            timeslot.setStatus(Timeslot.STATUS_PENDING);
//        }
//        eventEditFragment.setData(cpyEvent);
//
//        EventManager.getInstance(getContext()).setCurrentEvent(event);
//
//        openFragment(this, eventEditFragment);
    }
}
