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

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingCalendarPreferenceBinding;
import org.unimelb.itime.ui.mvpview.CalendarPreferenceMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.viewmodel.CalendarPreferenceViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarPreferenceFragment extends BaseUiAuthFragment<CalendarPreferenceMvpView, MvpBasePresenter<CalendarPreferenceMvpView>> implements CalendarPreferenceMvpView {

    private FragmentSettingCalendarPreferenceBinding binding;
    private CalendarPreferenceViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_preference, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public MvpBasePresenter<CalendarPreferenceMvpView> createPresenter() {
        return new MvpBasePresenter<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new CalendarPreferenceViewModel(getPresenter());
        contentViewModel.setUser(UserUtil.getInstance(getContext()).getUser());
        contentViewModel.setSetting(UserUtil.getInstance(getContext()).getSetting());

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.setting_calendar_pref));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Calendar data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

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
        getBaseActivity().openFragment(new SettingCalendarDisplayFragment());
    }

    @Override
    public void toAlertTimePage() {
        getBaseActivity().openFragment(new SettingStDefaultAlertFragment());
    }

    @Override
    public void toImportPage() {
        getBaseActivity().openFragment(new SettingCalendarImportFragment());
    }
}
