package org.unimelb.itime.ui.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentMainCalendarBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.messageevent.MessageMonthYear;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.fragment.calendars.CalendarAgendaFragment;
import org.unimelb.itime.ui.fragment.calendars.CalendarMonthDayFragment;
import org.unimelb.itime.ui.fragment.calendars.CalendarWeekFragment;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends MvpFragment<MainCalendarMvpView, MainCalendarPresenter> implements MainCalendarMvpView {

    private final static String TAG = "MainCalendarFragment";
    private CalendarMonthDayFragment monthDayFragment;
    private CalendarAgendaFragment agendaFragment;
    private CalendarWeekFragment weekFragment;
    private FragmentMainCalendarBinding binding;
    private MainCalendarViewModel mainCalendarViewModel;


    public void reloadEvent(){
        if (monthDayFragment!=null && monthDayFragment.isAdded()){
            monthDayFragment.calendarNotifyDataSetChanged();
        }
        if (agendaFragment!=null && agendaFragment.isAdded()){
            agendaFragment.calendarNotifyDataSetChanged();
        }
        if (weekFragment!=null && weekFragment.isAdded()){
            weekFragment.calendarNotifyDataSetChanged();
        }
    }

    @Override
    public MainCalendarPresenter createPresenter() {
        return new MainCalendarPresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_calendar, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainCalendarViewModel = new MainCalendarViewModel(getPresenter());
        binding.setCalenarVM(mainCalendarViewModel);
        initSpinner();
        initCalendars();
        initBackToday();
    }

    public void initBackToday(){
        ImageView backToday = (ImageView) binding.getRoot().findViewById(R.id.back_to_today);
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monthDayFragment.isAdded()){
                    monthDayFragment.backToday();
                }else if (weekFragment.isAdded()){
                    weekFragment.backToday();
                }else if (agendaFragment.isAdded()){
                    agendaFragment.backToday();
                }
            }
        });
    }

    public void initSpinner(){
        ArrayList<String> viewOptionsArrayList = new ArrayList<>();
        viewOptionsArrayList.add(getString(R.string.month_day_view));
        viewOptionsArrayList.add(getString(R.string.week_view));
        viewOptionsArrayList.add(getString(R.string.agenda_view));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, viewOptionsArrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.threeLines.setAdapter(arrayAdapter);
        binding.threeLines.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeView(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void changeView(int index){
        switch (index){
            case 0:
                showCalendar(monthDayFragment);
                break;
            case 1:
                showCalendar(weekFragment);
                break;
            case 2:
                showCalendar(agendaFragment);
                break;
            default:
                showCalendar(monthDayFragment);
        }

    }

    public void showCalendar(Fragment fragment){
        if (!fragment.isAdded()){
            getFragmentManager().beginTransaction().replace(R.id.calendar_framelayout, fragment).commit();
        }
    }


    public void initCalendars(){
        monthDayFragment = new CalendarMonthDayFragment();
        weekFragment = new CalendarWeekFragment();
        agendaFragment = new CalendarAgendaFragment();
        getFragmentManager().beginTransaction().add(R.id.calendar_framelayout, monthDayFragment).commit();
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
    public void startEditEventActivity(ITimeEventInterface iTimeEventInterface) {
        EventUtil.startEditEventActivity(getContext(), getActivity(), iTimeEventInterface);
    }
//
//    @Subscribe
//    public void refreshCalendar(MessageEvent msg){
//        if (msg.task == MessageEvent.RELOAD_EVENT){
//            reloadEvent();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
