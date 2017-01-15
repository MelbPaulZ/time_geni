package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingNotificationsBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView_delete;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.ui.viewmodel.SettingViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.UserProfileViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 27/12/2016.
 */

public class SettingNotificationFragment extends BaseUiAuthFragment<TaskBasedMvpView<Setting>, SettingPresenter<TaskBasedMvpView<Setting>>> implements TaskBasedMvpView<Object>, ItimeCommonMvpView{

    private FragmentSettingNotificationsBinding binding;
    private SettingViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_notifications, container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        contentViewModel = new SettingViewModel(getPresenter());
        contentViewModel.setUser(UserUtil.getInstance(getContext()).getUser());
        contentViewModel.setSetting(UserUtil.getInstance(getContext()).getSetting());

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.notifications));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter<>(getContext());
    }

    @Override
    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    @Override
    public void onBack() {
        getBaseActivity().finish();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskError(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }
}
