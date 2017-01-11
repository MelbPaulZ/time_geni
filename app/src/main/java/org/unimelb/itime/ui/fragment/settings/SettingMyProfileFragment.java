package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingMyProfileBinding;
import org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;

/**
 * Created by Paul on 26/12/2016.
 */

//public class SettingMyProfileFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> implements SettingCommonMvpView{
public class SettingMyProfileFragment extends BaseUiAuthFragment<UserMvpView, UserPresenter<UserMvpView>> implements UserMvpView{

    private FragmentSettingMyProfileBinding binding;

    private UserProfileViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile, container, false);
        return binding.getRoot();
    }


    @Override
    public UserPresenter<UserMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new UserProfileViewModel(this);
        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.action_settings));
        toolbarViewModel.setTitleStr(getString(R.string.setting_my_profile));
        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void toEditPhotoPage() {
        Intent intent = new Intent(getActivity(), ProfilePhotoPickerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void toEditNamePage() {

    }

    @Override
    public void toEditEmailPage() {

    }

    @Override
    public void toEditPasswordPage() {

    }

    @Override
    public void toEditMyQrCodePage() {

    }

    @Override
    public void toEditGenderPage() {

    }

    @Override
    public void toEditRegionPage() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {

    }

    @Override
    public void onTaskError(int taskId) {

    }


//    private void gotoPhotoPicker(){
//        Intent intent = new Intent(getActivity(), ProfilePhotoPickerActivity.class);
//        startActivityForResult(intent, 1);
//    }
//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        reloadSetting(); // after photo picker activity, reload setting data from setting manager
    }

//
//    private void saveSetting(){
//        SettingManager.getInstance(getContext()).setSetting(getSetting());
//    }

//    @Override
//    public void onViewChange(int task, boolean isSave) {

//        SettingMyProfileNameFragment fragment = new SettingMyProfileNameFragment();
//        getFragmentManager().beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
//                .replace(R.id.setting_activity_framelayout, fragment)
//                .commit();

//        if (task == MainSettingsViewModel.TASK_VIEW_AVATAR){
//            gotoPhotoPicker();
//        }else if (task == MainSettingsViewModel.TASK_TO_SETTING){
//
//        }else if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE_NAME){
//            openFragment(this, (SettingMyProfileNameFragment)getFragmentManager().findFragmentByTag(SettingMyProfileNameFragment.class.getSimpleName()), getSetting());
//        }else if (task == MainSettingsViewModel.TASK_TO_QR_CODE){
//            openFragment(this, (MyQRCodeFragment)getFragmentManager().findFragmentByTag(MyQRCodeFragment.class.getSimpleName()));
//        }else if (task == MainSettingsViewModel.TASK_TO_GENDER){
//            openFragment(this, (SettingProfileGenderFragment)getFragmentManager().findFragmentByTag(SettingProfileGenderFragment.class.getSimpleName()));
//        }else if (task == MainSettingsViewModel.TASK_TO_RESET_PASSWORD){
//            openFragment(this, (SettingProfileResetPasswordFragment)getFragmentManager().findFragmentByTag(SettingProfileResetPasswordFragment.class.getSimpleName()));
//        }
//    }

//    @Override
//    public void setLeftTitleStringToVM() {
//        toolbarViewModel.setLeftTitleStr(getString(R.string.action_settings));
//    }

//    @Override
//    public void setTitleStringToVM() {
//        toolbarViewModel.setTitleStr(getString(R.string.setting_my_profile));
//    }
//
//    @Override
//    public void setRightTitleStringToVM() {
//
//    }
//
//    @Override
//    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolBarViewModel() {
//        return new ToolbarViewModel<>(this);
//    }
//
    @Override
    public void onBack() {
//        saveSetting();
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {

    }
}
