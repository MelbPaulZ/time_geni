package org.unimelb.itime.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TimePicker;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventDatePickerFragment;
import org.unimelb.itime.ui.fragment.EventTimePickerFragment;
import org.unimelb.itime.ui.fragment.EventWeekViewFragment;
import org.unimelb.itime.vendor.weekview.WeekView;
import org.unimelb.itime.vendor.weekview.WeekViewHeader;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//public class EventCreateActivity extends AppCompatActivity implements
//        EventDatePickerFragment.EventDatePickerCommunicator, EventTimePickerFragment.EventTimePickerCommunicator{

    public class EventCreateActivity extends AppCompatActivity{
    private Unbinder butterKnifeUnbinder;
    private EventCreateNewFragment eventCreateNewFragment;
    private EventTimePickerFragment eventTimePickerFragment;
    private EventDatePickerFragment eventDatePickerFragment;
    private EventWeekViewFragment eventWeekViewFragment;

    private PlaceAutocompleteFragment autocompleteFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
//
        eventCreateNewFragment = new EventCreateNewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment,  eventCreateNewFragment).commit();

        EventBus.getDefault().register(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);

//        eventWeekViewFragment = new EventWeekViewFragment();
//        getFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventWeekViewFragment).commit();

    }


    public void toTimePicker(EventDatePickerFragment fragment){
        if (eventTimePickerFragment == null || !(eventTimePickerFragment.isAdded())){
            eventTimePickerFragment = new EventTimePickerFragment();
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventTimePickerFragment).commit();
        }else{
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(eventTimePickerFragment).commit();
        }
    }

    public void toDatePicker(EventCreateNewFragment fragment){
        if (eventDatePickerFragment == null || !(eventDatePickerFragment.isAdded())){
            eventDatePickerFragment = new EventDatePickerFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventDatePickerFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(eventDatePickerFragment).commit();
        }
    }
//
    public void toCreateEventNewFragment(Fragment fragment){
        if (eventCreateNewFragment == null || !(eventCreateNewFragment.isAdded())){
            eventCreateNewFragment = new EventCreateNewFragment();
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateNewFragment).commit();
        }else{
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventCreateNewFragment).commit();
        }
    }

        public void toWeekViewCalendar(EventCreateNewFragment fragment){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    @Subscribe
    public void gotoUrl(MessageUrl messageUrl){
        Log.i("get subscribe",messageUrl.url);
        Uri uri;
        if (messageUrl.url.startsWith("http")) {
            uri = Uri.parse(messageUrl.url);
        } else {
            uri = Uri.parse("http://" + messageUrl.url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

        @Override
        protected void onStop() {
            EventBus.getDefault().unregister(this);
            super.onStop();
        }

}
