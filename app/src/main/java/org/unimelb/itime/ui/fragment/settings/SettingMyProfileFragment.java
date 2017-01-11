package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import me.fesky.library.widget.ios.ActionSheetDialog;

import static org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity.CHOOSE_FROM_LIBRARY;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> implements SettingCommonMvpView{

    private FragmentSettingMyProfileBinding binding;

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
        binding.setSettingVM(viewModel);
    }



    private void gotoPhotoPicker(){
        Intent intent = new Intent(getActivity(), ProfilePhotoPickerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        reloadSetting(); // after photo picker activity, reload setting data from setting manager
    }

    private void saveSetting(){
        SettingManager.getInstance(getContext()).setSetting(getSetting());
    }

    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel.TASK_VIEW_AVATAR){
            gotoPhotoPicker();
        }else if (task == MainSettingsViewModel.TASK_TO_SETTING){
            saveSetting();
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }else if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE_NAME){
            openFragment(this, (SettingMyProfileNameFragment)getFragmentManager().findFragmentByTag(SettingMyProfileNameFragment.class.getSimpleName()), getSetting());
        }else if (task == MainSettingsViewModel.TASK_TO_QR_CODE){
            openFragment(this, (MyQRCodeFragment)getFragmentManager().findFragmentByTag(MyQRCodeFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_GENDER){
            openFragment(this, (SettingProfileGenderFragment)getFragmentManager().findFragmentByTag(SettingProfileGenderFragment.class.getSimpleName()));
        }else if (task == MainSettingsViewModel.TASK_TO_RESET_PASSWORD){
            openFragment(this, (SettingProfileResetPasswordFragment)getFragmentManager().findFragmentByTag(SettingProfileResetPasswordFragment.class.getSimpleName()));
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.action_settings));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.setting_my_profile));
    }

    @Override
    public void setRightTitleStringToVM() {

    }

    @Override
    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolBarViewModel() {
        return new ToolbarViewModel<>(this);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }
}
