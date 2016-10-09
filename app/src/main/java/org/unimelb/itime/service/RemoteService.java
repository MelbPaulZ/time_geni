package org.unimelb.itime.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.testdb.EventManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class RemoteService extends Service{
    private final static String TAG = "RemoteService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onStartCommand: " + "start remoteservice");
        Thread loadDBThread = new Thread(){
            @Override
            public void run(){

                createDB();
                try {
                    Thread.sleep(5000);
                    loadDB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new MessageEvent(MessageEvent.INIT_DB));
            }
        };
        loadDBThread.start();
    }

    private void loadDB(){

        DBManager.getInstance(getBaseContext()).getAllInvitee();
        long start = System.currentTimeMillis();
        EventManager.getInstance().getEventsMap().clear();
        List<Event> list = DBManager.getInstance(getBaseContext()).getAllEvents();
        int i = 0;
        for (Event ev: list) {
            ev.getTimeslots();
            List<Invitee> inviteeList =  ev.getInvitee();
            for(Invitee iv: inviteeList){
                iv.getContact();
            }

//            9-595
            EventManager.getInstance().addEvent(ev);
            i++;
            if(i > 100){
                break;
            }
        }
    }

    private void createDB(){
        DBManager.getInstance(getBaseContext()).clearDB();
        Calendar calendar = Calendar.getInstance();
        List<Event> events = new ArrayList<>();
        List<Contact> contacts = initContact();

        int[] type = {0,1,2};
        int[] status = {0,1};
        long interval = 3600 * 1000;
        int alldayCount = 0;
        for (int i = 1; i < 5; i++) {

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

            DBManager.getInstance(getBaseContext()).insertInviteeList(inviteeList);
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
                DBManager.getInstance(getBaseContext()).insertTimeSlot(slot);
            }
            event.setTimeslots(timeslotList);
            events.add(event);
            calendar.setTimeInMillis(endTime);

        }

        DBManager.getInstance(getBaseContext()).insertEventList(events);
    }

    private List<Contact> initContact(){
        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Contact contact = new Contact(""+i, "http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg", "name " + i);
            contacts.add(contact);
            DBManager.getInstance(getBaseContext()).insertContact(contact);
        }

        return contacts;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
