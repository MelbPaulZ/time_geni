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
import org.unimelb.itime.databinding.FragmentSettingMyProfileNameBinding;
import org.unimelb.itime.ui.mvpview.SettingMyProfileMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingMyProfileNameFragment extends SettingBaseFragment<SettingMyProfileMvpView, SettingCommonPresenter<SettingMyProfileMvpView>>
        implements SettingMyProfileMvpView{

    private FragmentSettingMyProfileNameBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_my_profile_name, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainSettingsViewModel viewModel = new MainSettingsViewModel(getPresenter());
        binding.setSettingVM(viewModel);
    }

    @Override
    public SettingCommonPresenter<SettingMyProfileMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Override
    public void changeAdatar() {

    }


    @Override
    public void onViewChange(int task) {
        if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE){
            closeFragment(this, (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName()));
        }
    }

    @Override
    public void onViewChange(int task, boolean isSave) {
        if (isSave) {
            if (task == MainSettingsViewModel.TASK_TO_MY_PROFILE){
                SettingMyProfileFragment settingMyProfileFragment = (SettingMyProfileFragment)getFragmentManager().findFragmentByTag(SettingMyProfileFragment.class.getSimpleName());
                closeFragment(this, settingMyProfileFragment, getSetting());
            }
        }
    }

}
