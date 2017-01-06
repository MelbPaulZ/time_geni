package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingMyProfileNameBinding;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileNameFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
        implements SettingCommonMvpView{

    private FragmentSettingMyProfileNameBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile_name, container, false);
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
    public void onEnter() {
        super.onEnter();
    }

    @Override
    public void onViewChange(int task, boolean isSave) {
        SettingMyProfileFragment settingMyProfileFragment = (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName());
        if (isSave) {
            closeFragment(this, settingMyProfileFragment, SettingManager.getInstance(getContext()).copySetting(getSetting()));
        }else{
            closeFragment(this, settingMyProfileFragment);
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        viewModel.setLeftTitleStr(getString(R.string.setting_my_profile));
    }

    @Override
    public void setTitleStringToVM() {
        viewModel.setTitleStr(getString(R.string.name));
    }

    @Override
    public void setRightTitleStringToVM() {
        viewModel.setRightTitleStr(getString(R.string.setting_save));
    }
}
