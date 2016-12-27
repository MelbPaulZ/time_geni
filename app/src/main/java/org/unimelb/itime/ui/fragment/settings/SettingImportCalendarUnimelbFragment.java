package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentSettingImportUnimelbBinding;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * Created by Paul on 27/12/2016.
 */



public class SettingImportCalendarUnimelbFragment extends BaseUiFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>> {

    private FragmentSettingImportUnimelbBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_import_unimelb, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainSettingsViewModel viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);

    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }
}
