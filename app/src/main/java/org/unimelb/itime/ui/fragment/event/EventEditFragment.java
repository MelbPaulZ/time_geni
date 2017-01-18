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
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentEventEditDetailBinding;
import org.unimelb.itime.managers.EventManager;
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
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.ArrayList;
import java.util.List;

import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_EVENT_INSERT;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_EVENT_UPDATE;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_SYN_IMAGE;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_UPLOAD_IMAGE;

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
    private List<WrapperTimeSlot> wrapperTimeSlotList;
    private EventManager eventManager;

    private EventEditViewModel eventEditViewModel;
//    private EventPresenter presenter;
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
        if(getActivity() instanceof EventCreateActivity){
            task = TASK_CREATE;
        }else if (getActivity() instanceof EventDetailActivity){
            task = TASK_EDIT;
        }

        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEvent(event);
        eventEditViewModel.setFragment_task(task);
        initToolbar();

        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initToolbar(){
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
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
        // this is for photo choose back, then refresh page
        if (eventEditViewModel!=null){
            eventEditViewModel.setEvent(event);
        }
    }

    @Override
    public EventPresenter<EventEditMvpView> createPresenter() {
        presenter = new EventPresenter<>(getContext());
        return presenter;
    }

    public void setPhotos(ArrayList<String> photos){
        event.setPhoto(EventUtil.fromStringToPhotoUrlList(getContext(), photos));
        // this is for letting viewmodel refresh data
        if (eventEditViewModel!=null){
            eventEditViewModel.setEvent(event);
        }
    }

    @Override
    public void toEventDetailPage() {
        // TODO: 14/1/17 clean pop backstack when reshowing edit event page
        getFragmentManager().popBackStack();
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
        timeSlotViewFragment.setFragment_task(EventTimeSlotViewFragment.TASK_EDIT);
        timeSlotViewFragment.setTargetFragment(this, REQ_TIMESLOT);
        Event cpyEvent = EventUtil.copyEvent(event);
        timeSlotViewFragment.setData(cpyEvent, null);
        getBaseActivity().openFragment(timeSlotViewFragment);
    }


    @Override
    public void toInviteePickerPage() {
        InviteeFragment inviteeFragment = new InviteeFragment();
        inviteeFragment.setTargetFragment(this, REQ_INVITEE);
        Event cpyEvent = EventUtil.copyEvent(event);
        inviteeFragment.setEvent(cpyEvent);
        getBaseActivity().openFragment(inviteeFragment);

    }


    @Override
    public void toPhotoPickerPage() {
        if (getActivity() instanceof EventDetailActivity) {
            ((EventDetailActivity) getActivity()).checkPermission();
        }else if (getActivity() instanceof EventCreateActivity){
            ((EventCreateActivity) getBaseActivity()).checkPermission();
        }
    }

    @Override
    public void toCustomPage() {
        EventCustomRepeatFragment eventCustomRepeatFragment = new EventCustomRepeatFragment();

        eventCustomRepeatFragment.setEvent(event);
        getBaseActivity().openFragment(eventCustomRepeatFragment);
    }


    @Override
    public void onTaskStart(int task) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, List<Event> data) {
        hideProgressDialog();
        switch (taskId){
            case TASK_UPLOAD_IMAGE:{
                for (Event event:data
                     ) {
                    for (PhotoUrl photoUrl:event.getPhoto()
                         ) {
                        presenter.updatePhotoToServer(event, photoUrl.getPhotoUid(), photoUrl.getUrl());
                    }
                }
                break;
            }
            case TASK_SYN_IMAGE:{
                toCalendar();
                break;
            }

            case TASK_EVENT_INSERT: {
                toCalendar();
                break;
            }
            case TASK_EVENT_UPDATE:{
                toCalendar();
                break;
            }
            default:{
                toCalendar();
            }
        }
    }

    @Override
    public void onTaskError(int taskId) {
        hideProgressDialog();
        switch (taskId) {
            case TASK_UPLOAD_IMAGE: {
                Toast.makeText(getContext(), "Upload Image Failed, LeanCloud ERROR", Toast.LENGTH_LONG).show();
                break;
            }
            case TASK_SYN_IMAGE: {
                Toast.makeText(getContext(), "Upload Image Failed, ITime-Server ERROR", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    private void toCalendar(){
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onBack() {
        if(task == TASK_CREATE){
            Intent intent = new Intent();
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        }else if (task == TASK_EDIT){
            toEventDetailPage();
        }
    }



    @Override
    public void onNext() {
        if (event.hasAttendee()
                && EventUtil.hasOtherInviteeExceptSelf(getContext(), event)
                && event.getTimeslot().size()==0){
            // has other invitees but no timeslots
            toTimeslotViewPage();
        }else if (task == TASK_CREATE){
            eventEditViewModel.toCreateEvent();
        }else{
            eventEditViewModel.editEvent();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + requestCode + "/" + requestCode);
        if(requestCode == REQ_LOCATION && resultCode == LocationPickerFragment.RET_LOCATION_SUCCESS){
            String location = data.getStringExtra("location");
            this.event.setLocation(location);
        }
    }
}
