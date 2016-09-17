package org.unimelb.itime.ui.fragment;


import android.content.Intent;
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
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.vendor.dayview.DayViewBody;
import org.unimelb.itime.vendor.eventview.DayDraggableEventView;
import org.unimelb.itime.vendor.eventview.WeekDraggableEventView;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.weekview.WeekView;
import org.unimelb.itime.vendor.weekview.WeekViewBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends MvpFragment<MainCalendarMvpView, MainCalendarPresenter> implements MainCalendarMvpView {

    private final static String TAG = "MainCalendarFragment";
    private final int ACTIVITY_EDITEVENT = 1;

    FragmentMainCalendarBinding binding;
    MainCalendarViewModel mainCalendarViewModel;
    ArrayList<? extends ITimeEventInterface> eventArrayList = new ArrayList<>();
    ArrayList<ITimeEventInterface> iTimeEventInterfacesArrayList = (ArrayList<ITimeEventInterface>) eventArrayList;;

    public void addNewEvent(Event event){
        DBManager.getInstance(getContext()).insertEvent(event);
        //try
        if (event.hasAttendee()) {
            DBManager.getInstance(getContext()).insertInviteeList(event.getInvitee());
        }
        if (event.hasTimeslots()) {
            for (int i = 0; i < event.getTimeslots().size(); i++) {
                // need to be changed for various number of timeslots
                DBManager.getInstance(getContext()).insertTimeSlot(event.getTimeslots().get(i));
            }
        }
        // here should insert invitee and timeslots

        List<Event> eventList = DBManager.getInstance(getContext()).getAllEvents();
        EventManager.getInstance().getEventsMap().clear();
        for (Event ev: eventList) {
            ev.getTimeslots();
            List<Invitee> inviteeList =  ev.getInvitee();
            for(Invitee iv: inviteeList){
                iv.getContact();
            }
            EventManager.getInstance().addEvent(ev);
        }


        binding.monthDayView.setDayEventMap(EventManager.getInstance().getEventsMap());
        binding.monthDayView.reloadEvents();
        binding.weekView.setEventMap(EventManager.getInstance().getEventsMap());

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
        init();
        binding.weekView.setOnWeekBodyOutterListener(new WeekViewBody.OnWeekBodyListener() {
            @Override
            public void onEventCreate(WeekDraggableEventView weekDraggableEventView) {

            }

            @Override
            public void onEventClick(WeekDraggableEventView weekDraggableEventView) {
                startEditEventActivity(weekDraggableEventView.getEvent());
            }

            @Override
            public void onEventDragStart(WeekDraggableEventView weekDraggableEventView) {

            }

            @Override
            public void onEventDragging(WeekDraggableEventView weekDraggableEventView, int i, int i1) {

            }

            @Override
            public void onEventDragDrop(WeekDraggableEventView weekDraggableEventView) {

            }
        });

        binding.monthDayView.setDayEventMap(EventManager.getInstance().getEventsMap());
        binding.monthDayView.setEventClassName(Event.class);
        binding.monthDayView.setOnBodyOuterListener(new DayViewBody.OnBodyListener() {

            @Override
            public void onEventCreate(DayDraggableEventView dayDraggableEventView) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dayDraggableEventView.getStartTimeM());
                Log.i("starttime", String.valueOf(calendar.getTime()));
                ((MainActivity)getActivity()).startEventCreateActivity(calendar);
            }

            @Override
            public void onEventClick(DayDraggableEventView dayDraggableEventView) {
                startEditEventActivity(dayDraggableEventView.getEvent());
            }

            @Override
            public void onEventDragStart(DayDraggableEventView dayDraggableEventView) {

            }

            @Override
            public void onEventDragging(DayDraggableEventView dayDraggableEventView, int i, int i1) {

            }

            @Override
            public void onEventDragDrop(DayDraggableEventView dayDraggableEventView) {
                EventManager.getInstance().updateEvent((Event) dayDraggableEventView.getEvent(),
                        dayDraggableEventView.getStartTimeM(), dayDraggableEventView.getEndTimeM());
                ((Event)dayDraggableEventView.getEvent()).update();

                binding.monthDayView.reloadEvents();

            }

        });

        binding.monthAgendaView.setDayEventMap(EventManager.getInstance().getEventsMap());
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

        long start = System.currentTimeMillis();
        EventManager.getInstance().getEventsMap().clear();
        List<Event> list = DBManager.getInstance(getContext()).getAllEvents();
        for (Event ev: list) {
            ev.getTimeslots();
            List<Invitee> inviteeList =  ev.getInvitee();
            for(Invitee iv: inviteeList){
                iv.getContact();
            }
            EventManager.getInstance().addEvent(ev);
        }
        long end = System.currentTimeMillis();
        long delay = (end - start) / 1000;

        binding.weekView.setEventMap(EventManager.getInstance().getEventsMap());
    }

    private void initDB(){
        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        List<Contact> contacts = initContact();

        int[] type = {0,1,2};
        int[] status = {0,1};
        long interval = 3600 * 1000;
        int alldayCount = 0;
        for (int i = 1; i < 100; i++) {

            long startTime = calendar.getTimeInMillis();
            long endTime = startTime + interval * (i%30);
            long duration = (endTime - startTime);

            Event event = new Event();
            event.setEventUid("" + i);
            event.setTitle("" + i);
            event.setEventType(i%type.length);
            event.setStatus(i%status.length);
            event.setLocation("here");
            event.setStartTime(startTime);
            if (i%2 == 0) {
                event.setHostUserUid("1"); // "1" refers to I am host
            }else{
                event.setHostUserUid("2"); // "2" refers to invitee
            }

            List<Invitee> inviteeList = new ArrayList<>();

            Invitee invitee1 = new Invitee();
            invitee1.setEventUid("" + i);
            invitee1.setContact(contacts.get(0));
            invitee1.setInviteeUid(contacts.get(0).getContactUid());
            inviteeList.add(invitee1);

            Invitee invitee2 = new Invitee();
            invitee2.setEventUid("" + i);
            invitee2.setContact(contacts.get(1));
            invitee2.setInviteeUid(contacts.get(1).getContactUid());
            inviteeList.add(invitee2);

            DBManager.getInstance(getContext()).insertInviteeList(inviteeList);
            event.setInvitee(inviteeList);

            long realEnd = endTime;
            long temp = duration;
            while (temp > 3 * 60 * 60 * 1000 ){
                temp = temp/2;
                realEnd -= temp;
            }

            event.setEndTime(realEnd);

            /* for time slot*/

            List<TimeSlot> timeslotList = new ArrayList<>();
            for(int k = 0; k < 3; k++){
                TimeSlot slot = new TimeSlot();
                slot.setTimeSlotUid((long)(Math.random() * 1000000));
                slot.setEventUid("" + i);
                slot.setStartTime(calendar.getTimeInMillis() + k * 3600 * 1000);
                slot.setEndTime(calendar.getTimeInMillis() + (1 + k) *3600*1000);
                slot.setStatus(getString(R.string.timeslot_status_pending));
                timeslotList.add(slot);
                DBManager.getInstance(getContext()).insertTimeSlot(slot);
            }
            event.setTimeslots(timeslotList);
            events.add(event);
            calendar.setTimeInMillis(endTime);

        }

        DBManager.getInstance(getContext()).insertEventList(events);
    }

    private List<Contact> initContact(){
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Contact contact = new Contact("contact:"+i, "http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg", "name " + i);
            contacts.add(contact);
            DBManager.getInstance(getContext()).insertContact(contact);
        }

        return contacts;
    }


    @Override
    public void startCreateEventActivity() {
        ((MainActivity) getActivity()).startEventCreateActivity();
    }

    @Override
    public void startEditEventActivity(ITimeEventInterface iTimeEventInterface) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        Event event = (Event) iTimeEventInterface;
        intent.putExtra(getString(R.string.event), event);
        startActivityForResult(intent, ACTIVITY_EDITEVENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_EDITEVENT){
            Log.i("here here","main calendar fragment");
        }

    }
}
