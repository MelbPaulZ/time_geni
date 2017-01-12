package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

import java.util.List;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarFragment extends BaseUiAuthFragment<TaskBasedMvpView<List<Calendar>>, CalendarPresenter<TaskBasedMvpView<List<Calendar>>>> implements TaskBasedMvpView<List<Calendar>>, ItimeCommonMvpView{

    private FragmentSettingCalendarBinding binding;
    private CalendarViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_preference, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public CalendarPresenter<TaskBasedMvpView<List<Calendar>>> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new CalendarViewModel(getPresenter());

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
    public void onTaskSuccess(int taskId, List<Calendar> data) {

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
}
