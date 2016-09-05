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
    WeekView weekView;
    ArrayList<? extends ITimeEventInterface> eventArrayList = new ArrayList<>();
    ArrayList<ITimeEventInterface> iTimeEventInterfacesArrayList = (ArrayList<ITimeEventInterface>) eventArrayList;;

    // put dayview and weekview in this page, set vi

    public void addNewEvent(Event event){
        iTimeEventInterfacesArrayList.add(event);
        weekView.setEvent(iTimeEventInterfacesArrayList);
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
        init();
    }

    private void init(){
        Event event = new Event();
        event.setTitle("host event");
        event.setStatus(5); // 5== pending, 6== confirm
        event.setEventType(1); //0 == private, 1== group, 2== public
        Calendar calendar =Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,5);
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

        event.setRepeatTypeId(1);
        event.setHost(true);

        // event2
        Event event2 = new Event();
        event2.setTitle("invite event");
        event2.setStatus(5);
        event2.setEventType(1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)+2, 2, 0);
        event2.setStartTime(calendar2.getTimeInMillis());
        event2.setEndTime(calendar2.getTimeInMillis() + 3600000*2);

        // set invitees
        ArrayList<String> event2Invitees = new ArrayList<>();
        event2Invitees.add("Jack");
        event2Invitees.add("Peter");
        event2Invitees.add("Zzzz");

        ArrayList<Long> suggestTimeArrayList2 = new ArrayList<>();
        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 3);
        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 8);
        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 24);
        event2.setProposedTimeSlots(suggestTimeArrayList2);
        event2.setDuration(120);

        event2.setRepeatTypeId(2);
        event2.setHost(false);


        Event event3 = new Event();
        event3.setTitle("solo event");
        event3.setStatus(6);
        event3.setEventType(0);

        event3.setStartTime(calendar2.getTimeInMillis()+3600000*25);
        event3.setEndTime(calendar2.getTimeInMillis() + 3600000*26);
        event3.setDuration(60);
        event3.setRepeatTypeId(0);
        event3.setHost(true);

        iTimeEventInterfacesArrayList.add(event);
        iTimeEventInterfacesArrayList.add(event2);
        iTimeEventInterfacesArrayList.add(event3);
        weekView = (WeekView) binding.getRoot().findViewById(R.id.week_view);
        weekView.setEvent(iTimeEventInterfacesArrayList);
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
