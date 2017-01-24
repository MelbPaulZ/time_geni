package org.unimelb.itime.ui.fragment.event;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import org.unimelb.itime.ui.activity.LocationPickerActivity;
import org.unimelb.itime.ui.activity.PhotoPickerActivity;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.mvpview.EventEditMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.EventPresenter;
import org.unimelb.itime.ui.viewmodel.EventEditViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.wrapper.WrapperTimeSlot;

import java.util.ArrayList;
import java.util.List;

import me.fesky.library.widget.ios.ActionSheetDialog;

import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_EVENT_INSERT;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_EVENT_UPDATE;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_SYN_IMAGE;
import static org.unimelb.itime.ui.presenter.EventPresenter.TASK_UPLOAD_IMAGE;

/**
 * Created by Paul on 28/08/2016.
 */
public class EventEditFragment extends BaseUiAuthFragment<EventEditMvpView, EventPresenter<EventEditMvpView>> implements EventEditMvpView {
    /**
     * the key for pass bundle for arguments
     */
    private final static String TAG = "EditFragment";

    public final static int TASK_CREATE = 0;
    public final static int TASK_EDIT = 1;

    public final static int REQ_LOCATION = 1000;
    public final static int REQ_INVITEE = 1001;
    public final static int REQ_TIMESLOT = 1002;
    public final static int REQ_CUSTOM_REPEAT = 1003;
    public final static int REQ_PHOTO = 1004;

    public final static int REQUEST_PHOTO_PERMISSION = 101;
    public final static int REQUEST_LOCATION_PERMISSION = 102;
    private List<String> permissionList;

    private FragmentEventEditDetailBinding binding;
    private Event event = null;
    private List<WrapperTimeSlot> wrapperTimeSlotList;
    private EventManager eventManager;
    private List<PhotoUrl> photoUrls;
    private EventPhotoFragment eventPhotoFragment;

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
        if (getActivity() instanceof EventCreateActivity) {
            task = TASK_CREATE;
        } else if (getActivity() instanceof EventDetailActivity) {
            task = TASK_EDIT;
        }

        eventManager = EventManager.getInstance(getContext());
        eventEditViewModel = new EventEditViewModel(getPresenter());
        eventEditViewModel.setEvent(event);
        eventEditViewModel.setFragmentTask(task);
        initToolbar();

        binding.setEventEditVM(eventEditViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        if (task == TASK_CREATE) {
            toolbarViewModel.setTitleStr(getString(R.string.new_event));
            toolbarViewModel.setRightTitleStr(getString(R.string.send));

        } else if (task == TASK_EDIT) {
            toolbarViewModel.setTitleStr(getString(R.string.edit_event));
            toolbarViewModel.setRightTitleStr(getString(R.string.done));
        }
    }

    public void setEvent(Event event) {
        this.event = event;
        // this is for photo choose back, then refreshEventManager page
        if (eventEditViewModel != null) {
            eventEditViewModel.setEvent(event);
        }
    }

    @Override
    public EventPresenter<EventEditMvpView> createPresenter() {
        presenter = new EventPresenter<>(getContext());
        return presenter;
    }


    @Override
    public void toEventDetailPage() {
        // TODO: 14/1/17 clean pop backstack when reshowing edit event page
        getFragmentManager().popBackStack();
    }

    @Override
    public void toLocationPage() {
        checkLocationPermission();
    }

    private void gotoLocationPicker() {
        Intent intent = new Intent(getActivity(), LocationPickerActivity.class);
        intent.putExtra("location", event.getLocation());
        startActivityForResult(intent, REQ_LOCATION);
    }

    @Override
    public void gotoGridView() {
        EventPhotoGridFragment gridFragment = new EventPhotoGridFragment();
        gridFragment.setEvent(event);
        getBaseActivity().openFragment(gridFragment);
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


    /**
     * only photo related permission granted, then can go to photo picker
     */
    private void startPhotoPicker() {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        int maxNum = 9;
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @Override
    public void toPhotoPickerPage() {
        if(event.getPhoto().isEmpty()) {
            openPhotoActionSheetDialog();
        }else{
            toPhotoGridPage();
        }
    }

    private void toPhotoGridPage(){
        if(eventPhotoFragment==null){
            eventPhotoFragment = new EventPhotoFragment();
        }
        eventPhotoFragment.setPhotos(event.getPhoto());
        getBaseActivity().openFragment(eventPhotoFragment);
    }

    private void openPhotoActionSheetDialog(){
        if (presenter.getView()!=null) {
            new ActionSheetDialog(getActivity())
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(getContext().getResources().getString(R.string.take_photo),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    //presenter.getView().openCamera();
                                }
                            })
                    .addSheetItem(presenter.getContext().getResources().getString(R.string.choose_from_photos),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    checkPhotoPickerPermissions();
                                }
                            }).show();
        }
    }


    @Override
    public void toCustomPage() {
        EventCustomRepeatFragment eventCustomRepeatFragment = new EventCustomRepeatFragment();
        eventCustomRepeatFragment.setTargetFragment(this, REQ_CUSTOM_REPEAT);
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
        switch (taskId) {
            case TASK_EVENT_INSERT: {
                toCalendar();
                break;
            }
            case TASK_EVENT_UPDATE: {
                toCalendar();
                break;
            }
            default: {
                toCalendar();
            }
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {
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

    private void toCalendar() {
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onBack() {
        if (task == TASK_CREATE) {
            Intent intent = new Intent();
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        } else if (task == TASK_EDIT) {
            toEventDetailPage();
        }
    }


    @Override
    public void onNext() {
        if (event.hasAttendee()
                && EventUtil.hasOtherInviteeExceptSelf(getContext(), event)
                && event.getTimeslot().size() == 0) {
            // has other invitees but no timeslots
            toTimeslotViewPage();
        } else if (task == TASK_CREATE) {
            eventEditViewModel.toCreateEvent();
        } else {
            eventEditViewModel.editEvent();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_LOCATION && resultCode == Activity.RESULT_OK) {
            String location = data.getStringExtra("location");
            this.event.setLocation(location);
            setEvent(event);
        }

        if (requestCode == REQ_CUSTOM_REPEAT && resultCode == EventCustomRepeatFragment.RET_CUSTOM_REPEAT) {
            Event event = (Event) data.getSerializableExtra("event");
            setEvent(event);
        }

        if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            List<PhotoUrl> photoUrls = EventUtil.fromStringToPhotoUrlList(getContext(), result);
            event.setPhoto(photoUrls);
            setEvent(event);
            toPhotoGridPage();
        }
    }


    /**
     * after request permission, this will be called back. If the corresponding permission is granted,
     * can continue do further
     * @param requestCode {REQUEST_LOCATION_PERMISSION, REQUEST_PHOTO_PERMISSION}
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if (allPermissionGranted(grantResults)) {
                    gotoLocationPicker();
                } else {
                    Toast.makeText(getContext(), "need location permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_PHOTO_PERMISSION: {
                if (allPermissionGranted(grantResults)) {
                    startPhotoPicker();
                } else {
                    Toast.makeText(getContext(), "need photo permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    /**
     * for photo picker, need to check storage permission and camera permission
     */
    private void checkPhotoPickerPermissions() {
        permissionList = new ArrayList<>();
        checkCameraPermission();
        checkStoragePermission();
        if (permissionList.size() > 0) {
            requestPermissions(
                    permissionList.toArray(new String[permissionList.size()]),
                    REQUEST_PHOTO_PERMISSION
            );
        } else {
            startPhotoPicker();
        }
    }


    /**
     * check camera permission, if not granted, add to check permission list
     */
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
    }

    /**
     * check storage permission, if not granted, then add to check permission list
     */
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    /**
     * if location permission granted, then go to location picker
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }else{
            gotoLocationPicker();
        }
    }

    /**
     *
     * @param grantResults all requirements
     * @return true if all granted, otherwise false
     */
    private boolean allPermissionGranted(int[] grantResults) {
        int size = grantResults.length;
        for (int i = 0; i < size; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
