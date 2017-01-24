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
import org.unimelb.itime.databinding.FragmentPhotogridBinding;
import org.unimelb.itime.ui.activity.PhotoPickerActivity;
import org.unimelb.itime.ui.mvpview.event.EventPhotoGridMvpView;
import org.unimelb.itime.ui.presenter.event.EventPhotoPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.PhotoGridViewModel;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

import me.fesky.library.widget.ios.AlertDialog;

/**
 * Created by Qiushuo Huang on 2017/1/23.
 */

public class EventPhotoFragment extends BaseUiAuthFragment<EventPhotoGridMvpView, EventPhotoPresenter> implements EventPhotoGridMvpView {
    public final static int REQ_LOCATION = 1000;
    public final static int REQ_CUSTOM_REPEAT = 1003;
    public final static int REQ_PHOTO = 1004;

    private int maxNum = 9;
    public final static int REQUEST_PHOTO_PERMISSION = 101;
    private List<String> permissionList;

    private FragmentPhotogridBinding binding;
    private Event event;
    private List<PhotoUrl> tmpPhotos;
    private ToolbarViewModel toolbarViewModel;
    private PhotoGridViewModel viewModel;
    private EventBigPhotoFragment bigPhotoFragment;
    private boolean editable = true;

    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photogrid, container, false);
        return binding.getRoot();
    }

    @Override
    public EventPhotoPresenter createPresenter() {
        return new EventPhotoPresenter(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new PhotoGridViewModel(getPresenter());
        loadData();
        initToolbar();
        binding.setViewModel(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    @Override
    public void onStart(){
        super.onStart();
        loadData();
    }

    private void loadData(){
        viewModel.setPhotos(tmpPhotos);
        viewModel.setEditable(editable);
        viewModel.setMaxNum(maxNum);
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.photo));
        toolbarViewModel.setRightClickable(true);
        toolbarViewModel.setRightTitleStr(getString(R.string.done));
    }

    public void setEvent(Event event) {
        this.event = event;
        tmpPhotos = new ArrayList<>(event.getPhoto());
    }

    @Override
    public void onBack() {
        if(!event.getPhoto().equals(tmpPhotos)){
            openAlertDialog();
        }else{
            getBaseActivity().onBackPressed();
        }
    }

    private void openAlertDialog(){
        new AlertDialog(getActivity())
                .builder()
                .setTitle(getString(R.string.cancel_photos_alert_msg))
                .setPositiveButton(getString(R.string.leave), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getBaseActivity().onBackPressed();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }

    @Override
    public void onNext() {
        event.setPhoto(tmpPhotos);
        getBaseActivity().onBackPressed();
    }

    @Override
    public void openCamera() {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum - tmpPhotos.size());
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
        intent.putExtra(PhotoPickerActivity.CAMERA_ONLY, true);
        startActivityForResult(intent, REQ_PHOTO);
    }

    @Override
    public void openAlbum(){
        checkPhotoPickerPermissions();
    }

    @Override
    public void openBigPhoto(int position, List<PhotoUrl> photos){
        if(bigPhotoFragment==null){
            bigPhotoFragment = new EventBigPhotoFragment();
        }
        bigPhotoFragment.setPosition(position);
        bigPhotoFragment.setPhotos(photos);
        bigPhotoFragment.setEditable(editable);
        getBaseActivity().openFragment(bigPhotoFragment, null, true);
    }


    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
            List<PhotoUrl> photoUrls = EventUtil.fromStringToPhotoUrlList(getContext(), result);
            tmpPhotos.addAll(photoUrls);
            viewModel.setPhotos(tmpPhotos);
        }
    }

    /**
     * only photo related permission granted, then can go to photo picker
     */
    private void startPhotoPicker() {
        Intent intent = new Intent(getActivity(), PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum - tmpPhotos.size());
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
        startActivityForResult(intent, REQ_PHOTO);
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
