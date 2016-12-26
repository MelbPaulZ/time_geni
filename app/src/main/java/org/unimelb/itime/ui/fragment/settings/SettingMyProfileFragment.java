package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingMyProfileBinding;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileFragment extends SettingCommonFragment {

    private FragmentSettingMyProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainSettingsViewModel viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);

    }


}
