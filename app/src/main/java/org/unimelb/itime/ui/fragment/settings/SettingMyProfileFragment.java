package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingMyProfileBinding;
import org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity;
import org.unimelb.itime.ui.fragment.contact.MyQRCodeFragment;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.UserPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 26/12/2016.
 */

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

    @NonNull
    @Override
    public UserPresenter<UserMvpView> createPresenter() {
        return new UserPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new UserProfileViewModel(getPresenter());
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);

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
        getBaseActivity().openFragment(new SettingMyProfileNameFragment());
    }

    @Override
    public void toEditEmailPage() {

    }

    @Override
    public void toEditPasswordPage() {
        getBaseActivity().openFragment(new SettingProfileResetPasswordFragment());
    }

    @Override
    public void toEditMyQrCodePage() {
        getBaseActivity().openFragment(new MyQRCodeFragment());
    }

    @Override
    public void toEditGenderPage() {
        getBaseActivity().openFragment(new SettingProfileGenderFragment());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBack() {
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {

    }
}
