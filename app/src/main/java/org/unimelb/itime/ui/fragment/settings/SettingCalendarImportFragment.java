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
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.FragmentSettingImportCalendarBinding;
import org.unimelb.itime.ui.mvpview.CalendarImportMvpView;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarPreferenceViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarImportFragment extends BaseUiAuthFragment<CalendarImportMvpView, CalendarPresenter<CalendarImportMvpView>> implements CalendarImportMvpView{

    private FragmentSettingImportCalendarBinding binding;
    private CalendarPreferenceViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_import_calendar, container, false);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public CalendarPresenter<CalendarImportMvpView> createPresenter() {
        return new CalendarPresenter<>(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contentViewModel = new CalendarPreferenceViewModel(getPresenter());
        User user = UserUtil.getInstance(getContext()).getUser();
        contentViewModel.setUser(user);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.setting_import_calendar));

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
    public void onTaskError(int taskId) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBack() {
        getBaseActivity().backFragment(new SettingCalendarPreferenceFragment());
    }

    @Override
    public void onNext() {

    }

    @Override
    public void toGoogleCal() {

    }

    @Override
    public void toUnimebCal() {

    }
}
