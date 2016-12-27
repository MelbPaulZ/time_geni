package org.unimelb.itime.ui.activity;

import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.ui.fragment.calendars.CalendarWeekFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileNameFragment;

/**
 * Created by Paul on 23/08/2016.
 */
public class TestActivityPaul extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paul);

//        CalendarWeekFragment calendarWeekFragment = new CalendarWeekFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, calendarWeekFragment).commit();
        SettingMyProfileFragment fragment = new SettingMyProfileFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, fragment).commit();
//        SettingMyProfileNameFragment fragment = new SettingMyProfileNameFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.test_paul_fragment, fragment).commit();
    }



}
