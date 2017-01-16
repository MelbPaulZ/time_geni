package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSettingAboutBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView_delete;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel_delete;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingAboutFragment extends SettingBaseFragment<SettingCommonMvpView_delete, SettingCommonPresenter<SettingCommonMvpView_delete>>
implements SettingCommonMvpView_delete {

    private FragmentSettingAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_about, container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setSettingVM(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public SettingCommonPresenter<SettingCommonMvpView_delete> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }


    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel_delete.TASK_TO_SETTING){
            getActivity().finish();
            getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
    }

    @Override
    public void setLeftTitleStringToVM() {
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.about));
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
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {

    }
}
