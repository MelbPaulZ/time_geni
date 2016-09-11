package org.unimelb.itime.ui.activity;

import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.ui.fragment.eventcreate.EventCreateDetailBeforeSendingFragment;

/**
 * Created by Paul on 23/08/2016.
 */
public class TestActivityPaul extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paul);

        EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment = new EventCreateDetailBeforeSendingFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, eventCreateDetailBeforeSendingFragment).commit();
//        eventCreateDetailBeforeSendingFragment.setEvent(event);
//

//        EventCreateNewFragment eventCreateNewFragment = new EventCreateNewFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, eventCreateNewFragment).commit();

//        EventTimeSlotViewFragment eventTimeSlotViewFragment = new EventTimeSlotViewFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, eventTimeSlotViewFragment).commit();
//        InviteeTimeslotFragment inviteeTimeslotFragment = new InviteeTimeslotFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, inviteeTimeslotFragment).commit();
//        EventDetailHostFragment eventDetailHostFragment = new EventDetailHostFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment,eventDetailHostFragment).commit();



    }



}
