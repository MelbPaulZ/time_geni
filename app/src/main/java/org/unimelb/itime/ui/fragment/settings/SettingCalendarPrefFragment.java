package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.CalendarPrefMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarPrefViewModel;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.List;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarPrefFragment extends BaseUiAuthFragment<CalendarPrefMvpView, CommonPresenter<CalendarPrefMvpView>> implements CalendarPrefMvpView{

    private FragmentSettingCalendarBinding binding;
    private CalendarPrefViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_preference, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public CommonPresenter<CalendarPrefMvpView> createPresenter() {
        return new CommonPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new CalendarPrefViewModel(getPresenter());

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.action_settings));
        toolbarViewModel.setTitleStr(getString(R.string.setting_calendar_pref));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, User data) {

    }

    @Override
    public void onTaskError(int taskId) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBack() {
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onNext() {

    }

    @Override
    public void toCalendarPage() {

    }

    @Override
    public void toAlertTimePage() {

    }

    @Override
    public void toImportPage() {

    }
}
