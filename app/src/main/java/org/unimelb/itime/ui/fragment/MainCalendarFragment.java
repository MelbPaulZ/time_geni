package org.unimelb.itime.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.FragmentMainCalendarBinding;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends MvpFragment<MainCalendarMvpView, MainCalendarPresenter> implements MainCalendarMvpView {

    private final static String TAG = "MainCalendarFragment";

    FragmentMainCalendarBinding binding;
    MainCalendarViewModel mainCalendarViewModel;

    // put dayview and weekview in this page, set vi
    public MainCalendarFragment() {

    }

    @Override
    public MainCalendarPresenter createPresenter() {
        return new MainCalendarPresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_calendar, container, false);

        // simulate event
        // simulate Events here


//            ArrayList<? extends ITimeEventInterface> interface1;
//            ArrayList<Event> eventArrayList = new ArrayList<>();
//            interface1 = eventArrayList;

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainCalendarViewModel = new MainCalendarViewModel(getPresenter());
        binding.setCalenarVM(mainCalendarViewModel);
        init();
    }

    private void init(){
        Event event = new Event();
        event.setTitle("itime meeting");
        event.setStatus(5); // 5== pending, 6== confirm
        event.setEventType(1); //0 == private, 1== group, 2== public
        Calendar calendar =Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,29);
        calendar.set(Calendar.HOUR_OF_DAY,4);
        calendar.set(Calendar.MINUTE,15);
        calendar.set(Calendar.SECOND,0);
        event.setStartTime(calendar.getTimeInMillis());
        event.setEndTime(calendar.getTimeInMillis() + 3600000 * 2);

        // set attendee
        ArrayList<String> attendeeArrayList = new ArrayList<>();
        attendeeArrayList.add("Paul");
        attendeeArrayList.add("David");
        attendeeArrayList.add("Tim");
        event.setAttendees(attendeeArrayList);

        ArrayList<Long> suggestTimeArrayList = new ArrayList<>();
        suggestTimeArrayList.add(calendar.getTimeInMillis());
        suggestTimeArrayList.add(calendar.getTimeInMillis() + 3600000 * 4);
        suggestTimeArrayList.add(calendar.getTimeInMillis() + 3600000 * 8);
        event.setProposedTimeSlots(suggestTimeArrayList);



        WeekView weekView = (WeekView) binding.getRoot().findViewById(R.id.week_view);
        weekView.addEvent(event);
        Log.i("calendar",calendar.getTime().toString());
    }


    @Override
    public void startCreateEventActivity() {
        ((MainActivity) getActivity()).startEventCreateActivity();
    }

    @Override
    public void startEditEventActivity(ITimeEventInterface iTimeEventInterface) {
        ((MainActivity)getActivity()).startEventEditActivity(iTimeEventInterface);
    }
}
