package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.databinding.FragmentSettingNotificationsBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingNotificationFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
implements SettingCommonMvpView{

    private FragmentSettingNotificationsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_notifications, container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setSettingVM(viewModel);
    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }



    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel.TASK_TO_SETTING){
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.action_settings));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.notifications));
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
