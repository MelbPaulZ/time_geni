package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingMyProfileBinding;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity;
import org.unimelb.itime.ui.fragment.contact.MyQRCodeFragment;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

import me.fesky.library.widget.ios.ActionSheetDialog;

import static org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity.CHOOSE_FROM_LIBRARY;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> implements SettingCommonMvpView{

    private FragmentSettingMyProfileBinding binding;
    private MainSettingsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);
    }



    @Override
    public void onStart() {
        super.onStart();
        viewModel.setSetting(SettingManager.getInstance(getContext()).getSetting());
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * popup dialog for change avatar
     */
    private void popupDialog(){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Take Photo", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                ImagePicker.getInstance().takePicture(getActivity(),ImagePicker.REQUEST_CODE_TAKE);
//                                                Intent intent = new Intent(ProfilePhotoPickerActivity.this, ImageGridActivity.class);
//                                                startActivityForResult(intent, TAKE_PHOTO);
                            }
                        })
                .addSheetItem("Choose from Photos", ActionSheetDialog.SheetItemColor.Black,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                                startActivityForResult(intent, CHOOSE_FROM_LIBRARY);
                            }
                        });
                actionSheetDialog.show();
    }

    private void gotoPhotoPicker(){
        Intent intent = new Intent(getActivity(), ProfilePhotoPickerActivity.class);
        startActivity(intent);
    }


    @Override
    public void onViewChange(int task) {
        if (task == MainSettingsViewModel.TASK_VIEW_AVATAR){
            gotoPhotoPicker();
        }else if (task == MainSettingsViewModel.TASK_TO_SETTING){
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//            closeFragment(this, (SettingIndexFragment)getFragmentManager().findFragmentByTag(SettingIndexFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE_NAME){
            openFragment(this, (SettingMyProfileNameFragment)getFragmentManager().findFragmentByTag(SettingMyProfileNameFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_QR_CODE){
            openFragment(this, (MyQRCodeFragment)getFragmentManager().findFragmentByTag(MyQRCodeFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_GENDER){
            openFragment(this, (SettingProfileGenderFragment)getFragmentManager().findFragmentByTag(SettingProfileGenderFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_RESET_PASSWORD){
            openFragment(this, (SettingProfileResetPasswordFragment)getFragmentManager().findFragmentByTag(SettingProfileResetPasswordFragment.class.getSimpleName()));
        }
    }

    @Override
    public void onViewChange(int task, boolean isSave) {

    }

}
