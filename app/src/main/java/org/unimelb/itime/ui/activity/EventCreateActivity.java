package org.unimelb.itime.ui.activity;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.eventcreate.EventCreateDetailBeforeSendingFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.fragment.eventcreate.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventDatePickerFragment;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.EventTimePickerFragment;
import org.unimelb.itime.ui.fragment.eventcreate.EventTimeSlotViewFragment;

import java.util.Calendar;

import butterknife.Unbinder;

//public class EventCreateActivity extends AppCompatActivity implements
//        EventDatePickerFragment.EventDatePickerCommunicator, EventTimePickerFragment.EventTimePickerCommunicator{

public class EventCreateActivity extends AppCompatActivity implements PlaceSelectionListener {
    private Unbinder butterKnifeUnbinder;
    private EventCreateNewFragment eventCreateNewFragment;
    private EventTimePickerFragment eventTimePickerFragment;
    private EventDatePickerFragment eventDatePickerFragment;
    private EventLocationPickerFragment eventLocationPickerFragment;
    private InviteeFragment inviteeFragment;
    private EventTimeSlotViewFragment eventTimeSlotViewFragment;
    private EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        eventCreateNewFragment = new EventCreateNewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateNewFragment).commit();

        if (getIntent().hasExtra(getString(R.string.new_event))) {
            Long startTime = getIntent().getExtras().getLong(getString(R.string.new_event));
            eventCreateNewFragment.setStartTime(startTime);
            eventCreateNewFragment.setEndTime(startTime + 3600000); // default 1 hour event
        }else{
            eventCreateNewFragment.setStartTime(Calendar.getInstance().getTimeInMillis());
            eventCreateNewFragment.setEndTime(Calendar.getInstance().getTimeInMillis() + 3600000);
        }

        EventBus.getDefault().register(this);
    }



    public void toTimePicker(EventDatePickerFragment fragment, String tag) {
        if (eventTimePickerFragment == null || !(eventTimePickerFragment.isAdded())) {
            eventTimePickerFragment = new EventTimePickerFragment();
            eventTimePickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventTimePickerFragment).commit();
        } else {
            eventTimePickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventTimePickerFragment).commit();
        }
    }

    public void toDatePicker(android.support.v4.app.Fragment fragment, String tag) {
        if (eventDatePickerFragment == null || !(eventDatePickerFragment.isAdded())) {
            eventDatePickerFragment = new EventDatePickerFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDatePickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventDatePickerFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDatePickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().show(eventDatePickerFragment).commit();
        }
    }


    public void toCreateEventNewFragment(android.support.v4.app.Fragment fragment){
        if (eventCreateNewFragment == null || !(eventCreateNewFragment.isAdded())) {
            eventCreateNewFragment = new EventCreateNewFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateNewFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventCreateNewFragment).commit();
        }
    }

    public void toWeekViewCalendar(EventCreateNewFragment fragment) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toLocationPicker(android.support.v4.app.Fragment fragment, String tag) {
        if (eventLocationPickerFragment == null || !(eventLocationPickerFragment.isAdded())) {
            eventLocationPickerFragment = new EventLocationPickerFragment();
            eventLocationPickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventLocationPickerFragment).commit();
        } else {
            eventLocationPickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventLocationPickerFragment).commit();
        }
    }


//    public void toLocationPicker(EventCreateDetailBeforeSendingFragment fragment, String tag)

    public void toAttendeePicker(EventCreateNewFragment fragment, Bundle bundle) {
        if (inviteeFragment == null || !(inviteeFragment.isAdded())) {
            inviteeFragment = new InviteeFragment();
            inviteeFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment, inviteeFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(inviteeFragment).commit();
        }
    }

    public void toTimeSlotView(Fragment fragment, Bundle bundle) {
        if (eventTimeSlotViewFragment == null || !(eventTimeSlotViewFragment.isAdded())) {
            eventTimeSlotViewFragment = new EventTimeSlotViewFragment();
            eventTimeSlotViewFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventTimeSlotViewFragment).commit();
        } else {
            getFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventTimeSlotViewFragment).commit();
        }
    }

    public void toNewEventDetailBeforeSending(android.support.v4.app.Fragment fragment, Bundle bundle) {
        if (eventCreateDetailBeforeSendingFragment == null || !(eventCreateDetailBeforeSendingFragment.isAdded())) {
            eventCreateDetailBeforeSendingFragment = new EventCreateDetailBeforeSendingFragment();
            eventCreateDetailBeforeSendingFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateDetailBeforeSendingFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventCreateDetailBeforeSendingFragment).commit();
        }
    }

    public void toNewEventDetailBeforeSending(android.support.v4.app.Fragment fragment) {
        if (eventCreateDetailBeforeSendingFragment == null || !(eventCreateDetailBeforeSendingFragment.isAdded())) {
            eventCreateDetailBeforeSendingFragment = new EventCreateDetailBeforeSendingFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, eventCreateDetailBeforeSendingFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventCreateDetailBeforeSendingFragment).commit();
        }
    }


    public void goBackToTimeSlot(EventCreateDetailBeforeSendingFragment fragment){
        if (eventTimeSlotViewFragment == null || !(eventTimeSlotViewFragment.isAdded())){
            eventTimeSlotViewFragment = new EventTimeSlotViewFragment();
            eventTimeSlotViewFragment.setTag(getString(R.string.tag_create_event_before_sending));
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment,eventTimeSlotViewFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventTimeSlotViewFragment.setTag(getString(R.string.tag_create_event_before_sending));
            getSupportFragmentManager().beginTransaction().show(eventTimeSlotViewFragment).commit();
        }
    }

    public void createSoloEvent(Event event){
        Intent intent = new Intent(this,MainActivity.class);
        event.setHost(true);
        intent.putExtra(getString(R.string.new_event),event);
        startActivity(intent);
    }


    public void sendEvent(Event event){
        Intent intent = new Intent(this, MainActivity.class);
        event.setHost(true);
        intent.putExtra(getString(R.string.new_event),event);
        startActivity(intent);
    }

    public void backToInviteePicker(EventTimeSlotViewFragment fragment, String tag){
        if (inviteeFragment == null || !(inviteeFragment.isAdded())){
            inviteeFragment = new InviteeFragment();
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().add(R.id.create_event_fragment,inviteeFragment).commit();
        }else{
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getFragmentManager().beginTransaction().show(inviteeFragment).commit();
        }
    }


    @Subscribe
    public void gotoUrl(MessageUrl messageUrl) {
        Log.i("get subscribe", messageUrl.url);
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
