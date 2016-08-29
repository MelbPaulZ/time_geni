package org.unimelb.itime.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TimePicker;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.EventAttendeeFragment;
import org.unimelb.itime.ui.fragment.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventDatePickerFragment;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.EventTimePickerFragment;
import org.unimelb.itime.ui.fragment.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.fragment.EventWeekViewFragment;
import org.unimelb.itime.ui.fragment.MainContactsFragment;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;
import org.unimelb.itime.vendor.weekview.WeekView;
import org.unimelb.itime.vendor.weekview.WeekViewHeader;

import butterknife.ButterKnife;
import butterknife.Unbinder;

//public class EventCreateActivity extends AppCompatActivity implements
//        EventDatePickerFragment.EventDatePickerCommunicator, EventTimePickerFragment.EventTimePickerCommunicator{

    public class EventCreateActivity extends AppCompatActivity implements PlaceSelectionListener {
    private Unbinder butterKnifeUnbinder;
    private EventCreateNewFragment eventCreateNewFragment;
    private EventTimePickerFragment eventTimePickerFragment;
    private EventDatePickerFragment eventDatePickerFragment;
    private EventWeekViewFragment eventWeekViewFragment;
        private EventLocationPickerFragment eventLocationPickerFragment;
        private EventAttendeeFragment eventAttendeeFragment;
        private EventTimeSlotViewFragment eventTimeSlotViewFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
//
        eventCreateNewFragment = new EventCreateNewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment,  eventCreateNewFragment).commit();

        EventBus.getDefault().register(this);

//        eventWeekViewFragment = new EventWeekViewFragment();
//        getFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventWeekViewFragment).commit();

    }

        @Override
        protected void onSaveInstanceState(Bundle outState) {

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

    public void toDatePicker(EventCreateNewFragment fragment, EventCreateNewVIewModel.PickDateFromType pickDateFromType){
        if (eventDatePickerFragment == null || !(eventDatePickerFragment.isAdded())){
            eventDatePickerFragment = new EventDatePickerFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDatePickerFragment.setPickDateFromType(pickDateFromType);
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventDatePickerFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDatePickerFragment.setPickDateFromType(pickDateFromType);
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

        public void toLocationPicker(EventCreateNewFragment fragment){
            if (eventLocationPickerFragment == null || !(eventLocationPickerFragment.isAdded())){
                eventLocationPickerFragment = new EventLocationPickerFragment();
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                getFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventLocationPickerFragment).commit();
            }else{
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                getFragmentManager().beginTransaction().show(eventLocationPickerFragment).commit();
            }
        }

        public void toAttendeePicker(EventCreateNewFragment fragment){
            if (eventAttendeeFragment == null || !(eventAttendeeFragment.isAdded())){
                eventAttendeeFragment = new EventAttendeeFragment();
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                getFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventAttendeeFragment).commit();
            }else{
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                getFragmentManager().beginTransaction().show(eventAttendeeFragment).commit();
            }
        }

        public void toTimeSlotView(Fragment fragment){
            if (eventTimeSlotViewFragment == null || !(eventTimeSlotViewFragment.isAdded())){
                eventTimeSlotViewFragment = new EventTimeSlotViewFragment();
                getFragmentManager().beginTransaction().hide(fragment).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventTimeSlotViewFragment).commit();
            }else{
                getFragmentManager().beginTransaction().hide(fragment).commit();
                getSupportFragmentManager().beginTransaction().show(eventTimeSlotViewFragment).commit();
            }
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

        @Override
        public void onPlaceSelected(Place place) {

        }

        @Override
        public void onError(Status status) {

        }
    }