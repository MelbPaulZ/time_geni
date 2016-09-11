package org.unimelb.itime.ui.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.FragmentMainCalendarBinding;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.DayViewBodyController;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;
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
    ArrayList<? extends ITimeEventInterface> eventArrayList = new ArrayList<>();
    ArrayList<ITimeEventInterface> iTimeEventInterfacesArrayList = (ArrayList<ITimeEventInterface>) eventArrayList;;

    public void addNewEvent(Event event){
        iTimeEventInterfacesArrayList.add(event);

        DBManager.getInstance(getContext()).insertEvent(event);
        EventManager.getInstance().addEvent(event);
        binding.monthDayView.reloadCurrentBodyEvents();
        binding.weekView.setEvent(iTimeEventInterfacesArrayList);

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

        binding.monthDayView.setOnCreateNewEvent(new DayViewBodyController.OnCreateNewEvent() {
            @Override
            public void createNewEvent(MyCalendar myCalendar) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(myCalendar.getYear(), myCalendar.getMonth(), myCalendar.getDay(), myCalendar.getHour(), myCalendar.getMinute());
                ((MainActivity)getActivity()).startEventCreateActivity(calendar);

            }
        });
        initSpinner();
        init();
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
                if (i == 0){
                    changeView(View.VISIBLE, View.GONE, View.GONE);
                }else if (i == 1){
                    changeView(View.GONE, View.VISIBLE, View.GONE);
                }else if ( i == 2 ){
                    changeView(View.GONE, View.GONE, View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void changeView(int monthDayView, int weekView, int agendaView){
        binding.monthDayView.setVisibility(monthDayView);
        binding.weekView.setVisibility(weekView);
        binding.monthAgendaView.setVisibility(agendaView);
    }

    private void init(){
        Event event = new Event();
        event.setTitle("Host event");
        event.setStatus(5); // 5== pending, 6== confirm
        event.setEventType(1); //0 == private, 1== group, 2== public
        Calendar calendar =Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,12);
        calendar.set(Calendar.HOUR_OF_DAY,4);
        calendar.set(Calendar.MINUTE,15);
        calendar.set(Calendar.SECOND,0);
        event.setStartTime(calendar.getTimeInMillis());
        event.setEndTime(calendar.getTimeInMillis() + 3600000 * 2);

        // set attendee
        ArrayList<Invitee> attendeeArrayList = new ArrayList<>();
        attendeeArrayList.add(new Invitee("1001",UserUtil.getInstance().getUserUid(),"Me","My Name"));
        attendeeArrayList.add(new Invitee("1001",null,"AGE", "2") );
        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "9"));
        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "10"));
        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "19"));
        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "29"));
        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "39"));
        event.setInvitee(attendeeArrayList);


        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setStartTime(calendar.getTimeInMillis());
        timeSlot1.setEndTime(calendar.getTimeInMillis() + 3600000 * 2);
        timeSlot1.setStatus(getString(R.string.timeslot_status_pending));
        timeSlots.add(timeSlot1);

        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setStartTime(calendar.getTimeInMillis() + 3600000 * 6);
        timeSlot2.setEndTime(calendar.getTimeInMillis() + 3600000 * 8);
        timeSlot2.setStatus(getString(R.string.timeslot_status_pending));
        timeSlots.add(timeSlot2);

        TimeSlot timeSlot3 = new TimeSlot();
        timeSlot3.setStartTime(calendar.getTimeInMillis() + 3600000 * 24);
        timeSlot3.setEndTime(calendar.getTimeInMillis() + 3600000 * 26);
        timeSlot3.setStatus(getString(R.string.timeslot_status_pending));
        timeSlots.add(timeSlot3);

        event.setLocation("Melbourne");
        event.setUrl("www.google.com");
        event.setNote("Bring your own laptop.");
        event.setTimeslots(timeSlots);
        event.setUserUid(UserUtil.getInstance().getUserUid());


        Event event2 = new Event();
        event2.setTitle("Invitation event");
        event2.setEventUid(EventUtil.generateUid());
        event2.setStatus(5); // 5== pending, 6== confirm

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH)+2);
        calendar2.set(Calendar.HOUR_OF_DAY,2);
        event2.setStartTime(calendar2.getTimeInMillis());
        event2.setEndTime(calendar2.getTimeInMillis() + 3600000);
        event2.setUserUid("3"); // my user id is 1

        ArrayList<Invitee> attendeeArrayList2 = new ArrayList<>();
        attendeeArrayList2.add(new Invitee("1002","3","Host","Host name"));
        attendeeArrayList2.add(new Invitee("1002",null,"AGE", "2") );
        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "9"));
        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "10"));
        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "19"));
        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "29"));
        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "39"));
        event2.setInvitee(attendeeArrayList2);

        ArrayList<TimeSlot> timeSlots2 = new ArrayList<>();
        TimeSlot timeSlot21 = new TimeSlot();
        timeSlot21.setStartTime(calendar2.getTimeInMillis() + 3600000 * 2);
        timeSlot21.setEndTime(calendar2.getTimeInMillis() + 3600000 * 3);
        timeSlot21.setStatus(getString(R.string.timeslot_status_pending));
        timeSlot21.setEventUid("1002");
        timeSlots2.add(timeSlot21);
        // need to set accept number later

        TimeSlot timeslot22 = new TimeSlot();
        timeslot22.setStartTime(calendar2.getTimeInMillis() + 3600000 * 6);
        timeslot22.setStartTime(calendar2.getTimeInMillis() + 3600000 * 7);
        timeslot22.setStatus(getString(R.string.timeslot_status_pending));
        timeslot22.setEventUid("1002");
        timeSlots2.add(timeslot22);

        TimeSlot timeslot23 = new TimeSlot();
        timeslot23.setStartTime(calendar2.getTimeInMillis() + 3600000 * 24);
        timeslot23.setEndTime(calendar2.getTimeInMillis() + 3600000 * 25);
        timeslot23.setStatus(getString(R.string.timeslot_status_pending));
        timeslot23.setEventUid("1002");
        timeSlots2.add(timeslot23);
        event2.setTimeslots(timeSlots2);

//        ArrayList<Long> endTimeSlotArrayList = new ArrayList<>(); // set the end time
//        endTimeSlotArrayList.add(calendar.getTimeInMillis() + 3600000 * 2);
//        endTimeSlotArrayList.add(calendar.getTimeInMillis() + 3600000 * 6);
//        endTimeSlotArrayList.add(calendar.getTimeInMillis() + 3600000 * 10);
//        event.setProposedEndTimeslots(endTimeSlotArrayList);
//
//        event.setRepeatTypeId(1);
//        event.setHost(true);
//
//        // event2
//        Event event2 = new Event();
//        event2.setTitle("invite event");
//        event2.setStatus(5);
//        event2.setEventType(1);
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(calendar2.get(Calendar.YEAR),calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)+2, 2, 0);
//        event2.setStartTime(calendar2.getTimeInMillis());
//        event2.setEndTime(calendar2.getTimeInMillis() + 3600000*2);
//
//        // set invitees
//        ArrayList<ITimeContactInterface> event2Invitees = new ArrayList<>();
//        event2Invitees.add(new Contact(null,"Tom", "25"));
//        event2Invitees.add(new Contact(null,"AGE", "26"));
//        event2Invitees.add(new Contact(null,"AGELOL", "27"));
//        event2.setAttendees(event2Invitees);
//
//        ArrayList<Long> suggestTimeArrayList2 = new ArrayList<>();
//        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 3);
//        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 8);
//        suggestTimeArrayList2.add(calendar2.getTimeInMillis() + 3600000 * 24);
//        event2.setProposedTimeSlots(suggestTimeArrayList2);
//        event2.setDuration(120);
//
//        event2.setRepeatTypeId(2);
//        event2.setHost(false);
//
//
//        Event event3 = new Event();
//        event3.setTitle("solo event");
//        event3.setStatus(6);
//        event3.setEventType(0);
//
//        event3.setStartTime(calendar2.getTimeInMillis()+3600000*25);
//        event3.setEndTime(calendar2.getTimeInMillis() + 3600000*26);
//        event3.setDuration(60);
//        event3.setRepeatTypeId(0);
//        event3.setHost(true);
//
//
//
////
//        Event event4 = new Event();
//        event4.setTitle("invite 2 people event");
//        event4.setStatus(5);
//        event4.setEventType(1);
//
//        Calendar calendar4 = Calendar.getInstance();
//        calendar4.set(calendar4.get(Calendar.YEAR),calendar4.get(Calendar.MONTH), calendar4.get(Calendar.DAY_OF_MONTH)-1, 2, 0);
//        event4.setStartTime(calendar4.getTimeInMillis());
//        event4.setEndTime(calendar4.getTimeInMillis() + 3600000*2);
//
//        // set invitees
//        ArrayList<ITimeContactInterface> event4Invitees = new ArrayList<>();
//        event4Invitees.add(new Contact(null,"SAGE", "12"));
//        event4Invitees.add(new Contact(null,"WAGE", "112"));
//
//
//        ArrayList<Long> suggestTimeArrayList4 = new ArrayList<>();
//        suggestTimeArrayList4.add(calendar4.getTimeInMillis() + 3600000 * 3);
//        suggestTimeArrayList4.add(calendar4.getTimeInMillis() + 3600000 * 8);
//        suggestTimeArrayList4.add(calendar4.getTimeInMillis() + 3600000 * 24);
//        event4.setProposedTimeSlots(suggestTimeArrayList4);
//        event4.setDuration(120);
//
//        event4.setRepeatTypeId(2);
//        event4.setHost(false);
//        event4.setAttendees(event4Invitees);
//
//
        iTimeEventInterfacesArrayList.add(event);
        iTimeEventInterfacesArrayList.add(event2);
//        iTimeEventInterfacesArrayList.add(event3);
//        iTimeEventInterfacesArrayList.add(event4);
        binding.weekView.setEvent(iTimeEventInterfacesArrayList);
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
