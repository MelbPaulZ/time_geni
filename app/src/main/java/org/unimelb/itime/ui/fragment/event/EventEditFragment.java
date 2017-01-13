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

import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.messageevent.MessageLocation;
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
    public static String DATA_EVENT = "event";
    private static final String TAG = "EditFragment";
    private FragmentEventEditDetailBinding binding;
    private Event event = null;
    private EventManager eventManager;

    private EventEditViewModel eventEditViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_edit_detail, container, false);
        return binding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        Bundle bundle = getArguments();
        if(bundle != null && bundle.get(DATA_EVENT) != null){
            this.event = (Event) bundle.get(DATA_EVENT);
        }
        if(event == null){
            event = eventManager.copyCurrentEvent(EventManager.getInstance(getContext()).getCurrentEvent());
        }

        eventEditViewModel.setEvent(event);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.cancel));
        toolbarViewModel.setTitleStr(getString(R.string.edit_event));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));

        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
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
        fragment.setTargetFragment(this, 1000);
        Bundle data = new Bundle();
        data.putString(LocationPickerFragment.DATA_LOCATION, event.getLocation());
        data.putParcelable(DATA_EVENT, event);
        getBaseActivity().openFragment(fragment, data);
    }

    @Override
    public void toTimeslotViewPage(Event event) {
        // // TODO: 12/1/17  changet to bundle
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
    public void toInviteePickerPage(Event event) {
        InviteeFragment inviteeFragment = new InviteeFragment();
        inviteeFragment.setTargetFragment(this, 1000);
        inviteeFragment.setEvent(eventManager.copyCurrentEvent(event));
        getBaseActivity().openFragment(inviteeFragment);

    }


    @Override
    public void toPhotoPickerPage() {
        ((EventDetailActivity)getActivity()).checkPermission();
    }

    @Subscribe
    public void getLocation(MessageLocation messageLocation){
        if (messageLocation.tag.equals(EventEditFragment.class.getSimpleName())){
            event.setLocation(messageLocation.locationString);
            eventEditViewModel.setEvent(event);
        }
    }

    @Subscribe
    public void getInvitees(MessageInvitees messageInvitees){
        if (messageInvitees.tag == getString(R.string.tag_host_event_edit)){
            event.setInvitee(messageInvitees.invitees);
            eventEditViewModel.setEvent(event);
        }
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
        if(resultCode == 1){
            String location = data.getStringExtra("location");
            this.event.setLocation(location);
        }
    }
}
