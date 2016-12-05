package org.unimelb.itime.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.databinding.FragmentMainSettingsBinding;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.mvpview.MainSettingsMvpView;
import org.unimelb.itime.ui.presenter.MainSettingsPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainSettingsFragment extends MvpFragment<MainSettingsMvpView, MainSettingsPresenter> implements MainSettingsMvpView{
    private MainSettingsViewModel settingVM;
    private FragmentMainSettingsBinding binding;

    public MainSettingsFragment() {
    }

    @Override
    public MainSettingsPresenter createPresenter() {
        return new MainSettingsPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        settingVM = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(settingVM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_settings, container, false);
        return binding.getRoot();
    }

    @Override
    public void logOut() {
        Toast.makeText(getContext(), "Log Out", Toast.LENGTH_SHORT).show();

        Intent serviceI = new Intent(getContext(), RemoteService.class);
        getActivity().stopService(serviceI);

        Intent i = new Intent(getContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
    }
}
