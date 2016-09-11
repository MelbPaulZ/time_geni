package org.unimelb.itime.ui.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.ui.viewmodel.MainCalendarViewModel;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.vendor.dayview.DayViewBodyController;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

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

        binding.monthDayView.setOnLoadEvents(new DayViewBodyController.OnLoadEvents() {
            @Override
            public List<ITimeEventInterface> loadEvents(long beginOfDayM) {
                if (EventManager.getInstance().getEventsMap().containsKey(beginOfDayM)){
                    return EventManager.getInstance().getEventsMap().get(beginOfDayM);
                }
                return null;
            }
        });

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
//        Event event = new Event();
//        event.setTitle("Host event");
//        event.setStatus(5); // 5== pending, 6== confirm
//        event.setEventType(1); //0 == private, 1== group, 2== public
//        Calendar calendar =Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH,12);
//        calendar.set(Calendar.HOUR_OF_DAY,4);
//        calendar.set(Calendar.MINUTE,15);
//        calendar.set(Calendar.SECOND,0);
//        event.setStartTime(calendar.getTimeInMillis());
//        event.setEndTime(calendar.getTimeInMillis() + 3600000 * 2);
//
//        // set attendee
////        ArrayList<Invitee> attendeeArrayList = new ArrayList<>();
//        List<Invitee> inviteeList = new ArrayList<>();
//
//        List<Contact> contacts = initContact();
//        Invitee invitee1 = new Invitee();
//        invitee1.setEventUid("1");
//        invitee1.setContact(contacts.get(0));
//        invitee1.setInviteeUid(contacts.get(0).getContactUid());
//        inviteeList.add(invitee1);
//
//        Invitee invitee2 = new Invitee();
//        invitee2.setEventUid("2");
//        invitee2.setContact(contacts.get(1));
//        invitee2.setInviteeUid(contacts.get(1).getContactUid());
//        inviteeList.add(invitee2);
////        attendeeArrayList.add(new Invitee("1001",UserUtil.getInstance().getUserUid(),new Contact(UserUtil.getUserUid(), null, new Invitee())));
////        attendeeArrayList.add(new Invitee("1001",null,"AGE", "2") );
////        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "9"));
////        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "10"));
////        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "19"));
////        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "29"));
////        attendeeArrayList.add(new Invitee("1001","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "39"));
//        event.setInvitee(inviteeList);
//
//
//        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
//        TimeSlot timeSlot1 = new TimeSlot();
//        timeSlot1.setStartTime(calendar.getTimeInMillis());
//        timeSlot1.setEndTime(calendar.getTimeInMillis() + 3600000 * 2);
//        timeSlot1.setStatus(getString(R.string.timeslot_status_pending));
//        timeSlots.add(timeSlot1);
//
//        TimeSlot timeSlot2 = new TimeSlot();
//        timeSlot2.setStartTime(calendar.getTimeInMillis() + 3600000 * 6);
//        timeSlot2.setEndTime(calendar.getTimeInMillis() + 3600000 * 8);
//        timeSlot2.setStatus(getString(R.string.timeslot_status_pending));
//        timeSlots.add(timeSlot2);
//
//        TimeSlot timeSlot3 = new TimeSlot();
//        timeSlot3.setStartTime(calendar.getTimeInMillis() + 3600000 * 24);
//        timeSlot3.setEndTime(calendar.getTimeInMillis() + 3600000 * 26);
//        timeSlot3.setStatus(getString(R.string.timeslot_status_pending));
//        timeSlots.add(timeSlot3);
//
//        event.setLocation("Melbourne");
//        event.setUrl("www.google.com");
//        event.setNote("Bring your own laptop.");
//        event.setTimeslots(timeSlots);
//        event.setEventUid(EventUtil.generateUid());
//        event.setHostUserUid(UserUtil.getInstance().getUserUid());
//
//
//        Event event2 = new Event();
//        event2.setTitle("Invitation event");
//        event2.setEventUid(EventUtil.generateUid());
//        event2.setStatus(5); // 5== pending, 6== confirm
//
//        Calendar calendar2 = Calendar.getInstance();
//        calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH)+2);
//        calendar2.set(Calendar.HOUR_OF_DAY,2);
//        event2.setStartTime(calendar2.getTimeInMillis());
//        event2.setEndTime(calendar2.getTimeInMillis() + 3600000);
//        event2.setHostUserUid("3"); // my user id is 1
//
//        ArrayList<Invitee> attendeeArrayList2 = new ArrayList<>();
//        attendeeArrayList2.add(new Invitee("1002","3","Host","Host name"));
//        attendeeArrayList2.add(new Invitee("1002",null,"AGE", "2") );
//        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "9"));
//        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "10"));
//        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "19"));
//        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "29"));
//        attendeeArrayList2.add(new Invitee("1002","http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg","周二珂", "39"));
//        event2.setInvitee(attendeeArrayList2);
//
//        ArrayList<TimeSlot> timeSlots2 = new ArrayList<>();
//        TimeSlot timeSlot21 = new TimeSlot();
//        timeSlot21.setStartTime(calendar2.getTimeInMillis() + 3600000 * 2);
//        timeSlot21.setEndTime(calendar2.getTimeInMillis() + 3600000 * 3);
//        timeSlot21.setStatus(getString(R.string.timeslot_status_pending));
//        timeSlot21.setEventUid("1002");
//        timeSlots2.add(timeSlot21);
//        // need to set accept number later
//
//        TimeSlot timeslot22 = new TimeSlot();
//        timeslot22.setStartTime(calendar2.getTimeInMillis() + 3600000 * 6);
//        timeslot22.setStartTime(calendar2.getTimeInMillis() + 3600000 * 7);
//        timeslot22.setStatus(getString(R.string.timeslot_status_pending));
//        timeslot22.setEventUid("1002");
//        timeSlots2.add(timeslot22);
//
//        TimeSlot timeslot23 = new TimeSlot();
//        timeslot23.setStartTime(calendar2.getTimeInMillis() + 3600000 * 24);
//        timeslot23.setEndTime(calendar2.getTimeInMillis() + 3600000 * 25);
//        timeslot23.setStatus(getString(R.string.timeslot_status_pending));
//        timeslot23.setEventUid("1002");
//        timeSlots2.add(timeslot23);
//        event2.setTimeslots(timeSlots2);
//
//
//        iTimeEventInterfacesArrayList.add(event);
//        iTimeEventInterfacesArrayList.add(event2);
//
//        DBManager.getInstance(getContext()).insertEventList((ArrayList<Event>)(ArrayList<? extends ITimeEventInterface>)iTimeEventInterfacesArrayList);
////        iTimeEventInterfacesArrayList.add(event3);
////        iTimeEventInterfacesArrayList.add(event4);
//        binding.weekView.setEvent(iTimeEventInterfacesArrayList);


//        DBManager.getInstance(getContext()).clearDB();
        initDB();

//        List<Invitee> list =  DBManager.getInstance(getContext()).getAllInvitee();


        List<Event> list = DBManager.getInstance(getContext()).getAllEvents();
        for (Event ev: list) {
            ev.getTimeslots();
            List<Invitee> inviteeList =  ev.getInvitee();
            for(Invitee iv: inviteeList){
                iv.getContact();
            }
        }
//        List<Invitee> v1 = list.get(0).getInvitee();
        binding.weekView.setEvent(new ArrayList<ITimeEventInterface>(list));
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
                slot.setStartTime(calendar.getTimeInMillis() + 2 * k * 3600 * 1000);
                slot.setEndTime(calendar.getTimeInMillis() + 3 * k *3600*1000);
                slot.setStatus(getString(R.string.timeslot_status_pending));
                timeslotList.add(slot);
            }
            event.setTimeslots(timeslotList);
            events.add(event);

//            if (duration >= 24 * 3600 * 1000 && alldayCount < 3){
//                String title = "All day";
//                for (int j = 0; j < 4; j++) {
//                    Event event_clone = new Event();
//                    event_clone.setTitle(title);
//                    event_clone.setEventType(0);
//                    event_clone.setStatus(0);
//                    event_clone.setStartTime(startTime);
//                    event_clone.setEndTime(endTime);
//                    event_clone.setLocation("here");
////                    event_clone.setInviteesUrls("");
//                    title = title + " all day";
//                }
//                alldayCount = 0;
//            }

            calendar.setTimeInMillis(endTime);

        }

        DBManager.getInstance(getContext()).insertEventList(events);
    }

    private List<Contact> initContact(){
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Contact contact = new Contact(""+i, "http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg", "name " + i);
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
        ((MainActivity)getActivity()).startEventEditActivity(iTimeEventInterface);
    }
}
