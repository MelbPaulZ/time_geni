package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingDefaultAlertBinding;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingDefaultAlertFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
implements SettingCommonMvpView{

    private FragmentSettingDefaultAlertBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_default_alert, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        MainSettingsViewModel viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);
    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }


    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel.TASK_TO_CALENDAR_PREFERENCE){
            closeFragment(this, (SettingCalendarPreferenceFragment)getFragmentManager().findFragmentByTag(SettingCalendarPreferenceFragment.class.getSimpleName()));
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        viewModel.setLeftTitleStr(getString(R.string.back));
    }

    @Override
    public void setTitleStringToVM() {
        viewModel.setTitleStr(getString(R.string.setting_default_alert));
    }

    @Override
    public void setRightTitleStringToVM() {

    }
}