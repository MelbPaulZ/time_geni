package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingGenderBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingProfileGenderFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
implements SettingCommonMvpView{

    private FragmentSettingGenderBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_gender, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setSettingVM(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE){
            closeFragment(this, (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName()), getSetting());
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftTitleStr(getString(R.string.setting_my_profile));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.setting_gender));
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
        closeFragment(this, (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName()));
    }

    @Override
    public void onNext() {
        closeFragment(this, (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName()), getSetting());

    }
}
