package org.unimelb.itime.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentViewCalendarBinding;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.calendars.ViewInCalendarMonthDayFragment;
import org.unimelb.itime.ui.fragment.event.EventDetailGroupFragment;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.List;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class ViewMainCalendarFragment extends BaseUiFragment<MainCalendarMvpView, MainCalendarPresenter> implements MainCalendarMvpView {

    private final static String TAG = "MainCalendarFragment";
    private ViewInCalendarMonthDayFragment monthDayFragment;
    private FragmentViewCalendarBinding binding;
    private MainCalendarViewModel mainCalendarViewModel;

    @Override
    public MainCalendarPresenter createPresenter() {
        return new MainCalendarPresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_calendar, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainCalendarViewModel = new MainCalendarViewModel(getPresenter());
        binding.setCalenarVM(mainCalendarViewModel);
        initCalendars();
        showCalendar(this.monthDayFragment);
    }


    public void showCalendar(Fragment fragment){
        if (!fragment.isAdded()){
            getFragmentManager().beginTransaction().replace(R.id.calendar_framelayout, fragment).commit();
        }
    }


    public void initCalendars(){
        monthDayFragment = new ViewInCalendarMonthDayFragment();
        getFragmentManager().beginTransaction().add(R.id.calendar_framelayout, monthDayFragment).commit();
    }

    public ViewInCalendarMonthDayFragment getMonthDayFrag(){
        return this.monthDayFragment;
    }

    @Subscribe
    public void setYearMonthHeader(MessageMonthYear messageMonthYear){
        String month = EventUtil.getMonth(getContext(),messageMonthYear.month);
        String year = messageMonthYear.year + "";
        mainCalendarViewModel.setToolbarTitle( month + " " + year);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void startCreateEventActivity() {
        ((MainActivity) getActivity()).startEventCreateActivity();
    }

    @Override
    public void backToGroupEvent() {
        EventDetailGroupFragment eventDetailGroupFragment = (EventDetailGroupFragment) getFragmentManager().findFragmentByTag(EventDetailGroupFragment.class.getSimpleName());
        switchFragment(this,eventDetailGroupFragment);
    }

    @Override
    public void startEditEventActivity(ITimeEventInterface iTimeEventInterface) {
        EventUtil.startEditEventActivity(getContext(), getActivity(), iTimeEventInterface);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    public void onTaskError(Throwable e) {

    }

    @Override
    public void onTaskComplete(List<Event> dataList) {

    }

    @Override
    public void onTaskComplete(Event data) {

    }
}
