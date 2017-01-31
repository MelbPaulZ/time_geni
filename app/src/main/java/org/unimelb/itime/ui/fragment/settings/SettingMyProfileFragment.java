package org.unimelb.itime.ui.fragment.settings;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    public static final int REQ_GENDER = 1000;
    public static final int REQ_REGION = 1001;

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
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
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
        Fragment fragment = new SettingProfileGenderFragment();
        fragment.setTargetFragment(this, REQ_GENDER);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void toEditRegionPage() {
        Fragment fragment = new SettingRegionFragment();
        fragment.setTargetFragment(this, REQ_REGION);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

    @Override
    public void onBack() {
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_GENDER && resultCode == Activity.RESULT_OK){
            refreshDataInViewModel();
        }

        if (requestCode == REQ_REGION && resultCode == Activity.RESULT_OK){
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void refreshDataInViewModel(){
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);
    }
}
