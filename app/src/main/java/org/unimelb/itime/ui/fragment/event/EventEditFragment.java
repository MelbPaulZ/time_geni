package org.unimelb.itime.ui.fragment.event;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.LocationPickerFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends BaseUiAuthFragment<EventEditMvpView, EventPresenter<EventEditMvpView>> implements EventEditMvpView{
    /**
     * the key for pass bundle for arguments
     */
    private final static String TAG = "EditFragment";

    public final static int TASK_CREATE = 0;
    public final static int TASK_EDIT = 1;

    public final static int REQ_LOCATION = 1000;
    public final static int REQ_INVITEE = 1001;
    public final static int REQ_TIMESLOT = 1002;


    private FragmentEventEditDetailBinding binding;
    private Event event = null;
    private EventManager eventManager;

    private EventEditViewModel eventEditViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private int task = TASK_CREATE;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit_detail, container, false);
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(getActivity() instanceof EventCreateActivity){
            task = TASK_CREATE;
        }else if (getActivity() instanceof EventDetailActivity){
            task = TASK_EDIT;
        }

        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEvent(event);
        initToolbar();

        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initToolbar(){
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.cancel));
        if (task == TASK_CREATE) {
            toolbarViewModel.setTitleStr(getString(R.string.new_event));
            toolbarViewModel.setRightTitleStr(getString(R.string.send));

        }else if (task == TASK_EDIT) {
            toolbarViewModel.setTitleStr(getString(R.string.edit_event));
            toolbarViewModel.setRightTitleStr(getString(R.string.done));
        }
    }

    public void setEvent(Event event){
        this.event = event;
    }

    @Override
    public EventPresenter<EventEditMvpView> createPresenter() {
        return new EventPresenter<>(getContext());
    }

    public void setPhotos(ArrayList<String> photos){
        eventEditViewModel.setPhotos(photos);
    }

    @Override
    public void toEventDetailPage() {
        EventDetailFragment fragment = new EventDetailFragment();
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toLocationPage() {
        LocationPickerFragment fragment = new LocationPickerFragment();
        fragment.setTargetFragment(this, REQ_LOCATION);
        Bundle data = new Bundle();
        data.putString(LocationPickerFragment.DATA_LOCATION, event.getLocation());
        getBaseActivity().openFragment(fragment, data);
    }

    @Override
    public void toTimeslotViewPage() {
        // // TODO: 12/1/17  changet to

        EventTimeSlotViewFragment timeSlotViewFragment = new EventTimeSlotViewFragment();
        timeSlotViewFragment.setTargetFragment(this, REQ_TIMESLOT);
        timeSlotViewFragment.setEvent(event);
        getBaseActivity().openFragment(timeSlotViewFragment);
//        EventDetailTimeSlotFragment timeSlotFragment = new EventDetailTimeSlotFragment();
//        Event cpyEvent = eventManager.copyCurrentEvent(event);
//        Invitee me = EventUtil.getSelfInInvitees(getContext(), cpyEvent);
//        // if the user is host, then reset all his timeslot as create
//        if (me!=null) {
//            for (SlotResponse slotResponse : me.getSlotResponses()) {
//                slotResponse.setStatus(Timeslot.STATUS_CREATING);
//            }
//        }
//        timeSlotFragment.setEvent(cpyEvent);
//        getBaseActivity().openFragment(timeSlotFragment);
    }


    @Override
    public void toInviteePickerPage() {
        InviteeFragment inviteeFragment = new InviteeFragment();
        inviteeFragment.setTargetFragment(this, REQ_INVITEE);
        inviteeFragment.setEvent(event);
        getBaseActivity().openFragment(inviteeFragment);

    }


    @Override
    public void toPhotoPickerPage() {
        ((EventDetailActivity)getActivity()).checkPermission();
    }


    @Override
    public void onTaskStart(int task) {
        AppUtil.showProgressBar(getActivity(),"Updating","Please wait...");
    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
//        switch (taskId){
//            case EventCommonPresenter.TASK_EVENT_INSERT:
//                break;
//            case EventCommonPresenter.TASK_EVENT_UPDATE:{
//                toCalendar();
//                break;
//            }case EventCommonPresenter.TASK_EVENT_DELETE:
//                toCalendar();
//                break;
//        }
    }

    @Override
    public void onTaskError(int taskId) {

    }

    private void toCalendar(){
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onBack() {
        eventEditViewModel.onBack();
    }

    @Override
    public void onNext() {
        eventEditViewModel.onBack();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + requestCode + "/" + requestCode);
        if(requestCode == REQ_LOCATION && resultCode == LocationPickerFragment.RET_LOCATION_SUCCESS){
            String location = data.getStringExtra("location");
            this.event.setLocation(location);
        }
    }
}
