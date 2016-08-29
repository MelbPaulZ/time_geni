package org.unimelb.itime.ui.activity;

import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.ui.fragment.EventAttendeeTimeslotResponseFragment;

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
        EventAttendeeTimeslotResponseFragment eventAttendeeTimeslotResponseFragment = new EventAttendeeTimeslotResponseFragment();
        getFragmentManager().beginTransaction().add(R.id.test_paul_fragment,eventAttendeeTimeslotResponseFragment).commit();

    }

}
