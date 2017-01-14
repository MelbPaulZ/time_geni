package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.databinding.FragmentSettingCalendarCreateBinding;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.viewmodel.CalendarViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 26/12/2016.
 */

public class SettingCalendarCreateFragment extends BaseUiAuthFragment<TaskBasedMvpView<Calendar>, CalendarPresenter<TaskBasedMvpView<Calendar>>> implements TaskBasedMvpView<Calendar>,ItimeCommonMvpView {

    private FragmentSettingCalendarCreateBinding binding;

    private CalendarViewModel contentViewModel;
    private ToolbarViewModel<? extends ItimeCommonMvpView> toolbarViewModel;
    private CalendarPresenter presenter;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting_calendar_create, container, false);
        return binding.getRoot();
    }

    @Override
    public CalendarPresenter createPresenter() {
        return new CalendarPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = getPresenter();

        this.calendar = new Calendar();
        this.calendar.setCalendarUid(AppUtil.generateUuid());
        this.calendar.setUserUid(UserUtil.getInstance(getContext()).getUserUid());

        contentViewModel = new CalendarViewModel(presenter);
        contentViewModel.setCalendar(calendar);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setLeftTitleStr(getString(R.string.calendar_title));
        toolbarViewModel.setTitleStr(getString(R.string.add_calendar));
        toolbarViewModel.setRightTitleStr(getString(R.string.done));

        binding.setContentVM(contentViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onBack() {
        getBaseActivity().backFragment(new SettingCalendarDisplayFragment());
    }

    @Override
    public void onNext() {
        contentViewModel.onCreateDoneClick().onClick(null);
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
}
