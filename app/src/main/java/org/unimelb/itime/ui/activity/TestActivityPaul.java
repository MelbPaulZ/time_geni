package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.EventAttendeeTimeslotResponseFragment;
import org.unimelb.itime.ui.fragment.EventCreateDetailBeforeSendingFragment;
import org.unimelb.itime.ui.fragment.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.mvpview.EventCreateNewMvpView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 23/08/2016.
 */
public class TestActivityPaul extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paul);

//        EventEditFragment eventEditFragment = new EventEditFragment();
//        EventDetailForInviteeFragment eventDetailFragment = new EventDetailForInviteeFragment();
//        EventReceiveDetailFragment eventReceiveDetailFragment = new EventReceiveDetailFragment();
//        EventAttendeeTimeslotResponseFragment eventAttendeeTimeslotResponseFragment = new EventAttendeeTimeslotResponseFragment();
//        getFragmentManager().beginTransaction().add(R.id.test_paul_fragment,eventAttendeeTimeslotResponseFragment).commit();

//        simulate data
//        Event event = new Event();
//        event.setTitle("Dinner");
//        event.setLocationAddress("Melbourne");
//        event.setRepeatTypeId(0);
//        ArrayList<String> names = new ArrayList<>();
//        names.add("Paul");
//        names.add("Tom");
//        event.setAttendees(names);
//        event.setLocationAddress("Melbourne");
//        event.setNote("with teammates");
//        event.setUrl("www.unimelb.edu.au");
//
//        ArrayList<Long> timeSlots = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        long timeLong = calendar.getTimeInMillis();
//        timeSlots.add(timeLong);
//        timeSlots.add(timeLong + 3600000*3);
//        timeSlots.add(timeLong + 3600000*24);
//        event.setProposedTimeSlots(timeSlots);
//
//        EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment = new EventCreateDetailBeforeSendingFragment();
//        eventCreateDetailBeforeSendingFragment.setEvent(event);
//

//        EventCreateNewFragment eventCreateNewFragment = new EventCreateNewFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, eventCreateNewFragment).commit();

        EventTimeSlotViewFragment eventTimeSlotViewFragment = new EventTimeSlotViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, eventTimeSlotViewFragment).commit();



    }



}
