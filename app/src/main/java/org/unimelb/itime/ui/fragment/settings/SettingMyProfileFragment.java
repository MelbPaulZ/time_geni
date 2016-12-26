package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentSettingMyProfileBinding;
import org.unimelb.itime.ui.activity.ProfilePhotoPickerActivity;
import org.unimelb.itime.ui.mvpview.SettingMyProfileMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.SettingProfileViewModel;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileFragment extends BaseUiFragment<SettingMyProfileMvpView, SettingCommonPresenter<SettingMyProfileMvpView>> implements SettingMyProfileMvpView{

    private FragmentSettingMyProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public SettingCommonPresenter<SettingMyProfileMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SettingProfileViewModel viewModel = new SettingProfileViewModel(getPresenter());
        binding.setSettingVM(viewModel);

    }


    @Override
    public void changeAdatar() {
        Intent intent = new Intent(getActivity(), ProfilePhotoPickerActivity.class);
        startActivity(intent);
    }

    @Override
    public void logout() {

    }
}
