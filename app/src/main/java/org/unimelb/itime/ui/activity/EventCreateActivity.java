package org.unimelb.itime.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventDatePickerFragment;
import org.unimelb.itime.ui.fragment.EventTimePickerFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EventCreateActivity extends AppCompatActivity implements
        EventDatePickerFragment.EventDatePickerCommunicator, EventTimePickerFragment.EventTimePickerCommunicator{

    private Unbinder butterKnifeUnbinder;
    private EventCreateNewFragment eventCreateNewFragment;
    private EventTimePickerFragment eventTimePickerFragment;
    private EventDatePickerFragment eventDatePickerFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        eventCreateNewFragment = new EventCreateNewFragment();
        getFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventCreateNewFragment).commit();
    }


    public void toTimePicker(Fragment fragment){
        if (eventTimePickerFragment == null || !(eventTimePickerFragment.isAdded())){
            eventTimePickerFragment = new EventTimePickerFragment();
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventTimePickerFragment).commit();
        }else{
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(eventTimePickerFragment).commit();
        }
    }

    public void toDatePicker(Fragment fragment){
        if (eventDatePickerFragment == null || !(eventDatePickerFragment.isAdded())){
            eventDatePickerFragment = new EventDatePickerFragment();
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventDatePickerFragment).commit();
        }else{
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(eventDatePickerFragment).commit();
        }
    }

    public void toCreateEventNewFragment(Fragment fragment){
        if (eventCreateNewFragment == null || !(eventCreateNewFragment.isAdded())){
            eventCreateNewFragment = new EventCreateNewFragment();
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateNewFragment).commit();
        }else{
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(eventCreateNewFragment).commit();
        }
    }


    @Override
    public void changeDate(int year, int month, int day) {
        if (eventTimePickerFragment == null || !(eventTimePickerFragment.isAdded())){
            eventTimePickerFragment = new EventTimePickerFragment();
        }
        eventTimePickerFragment.changeDate(year,month,day);
    }

    @Override
    public void changeDateAndTime(int year, int month, int day, int hour, int minute) {
        eventCreateNewFragment.changeDateAndTime(year,month,day,hour,minute);
    }
}
