package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.InviteeFragment;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailForSoloFragment;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailHostFragment;
import org.unimelb.itime.ui.fragment.eventdetail.EventDetailHostTimeSlotFragment;
import org.unimelb.itime.ui.fragment.eventdetail.EventEditFragment;
import org.unimelb.itime.ui.fragment.InviteeTimeslotFragment;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    private EventDetailHostFragment eventDetailHostFragment;
    private EventDetailForSoloFragment eventDetailForSoloFragment;
    private InviteeTimeslotFragment inviteeTimeslotFragment;
    private EventEditFragment eventEditFragment;
    private EventDetailHostTimeSlotFragment eventDetailHostTimeSlotFragment;
    private EventLocationPickerFragment locationPickerFragment;
    private InviteeFragment inviteeFragment;
    private Event event;

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int ACTIVITY_PHOTOPICKER = 2;

    public static final String request = "request";
    public static final int UPDATE_EVENT = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        event = (Event) getIntent().getSerializableExtra(getString(R.string.event));

        if (event.getHostUserUid().equals(UserUtil.getInstance().getUserUid())) {
//            if (true){ // for test only
            if (event.hasAttendee() && event.getInvitee().size()>0) {
                // group event, and is host
                if (event.getInvitee().size() == 2) {
                    Toast.makeText(getBaseContext(), "I am host", Toast.LENGTH_SHORT).show();
                    eventDetailHostFragment = new EventDetailHostFragment();
                    eventDetailHostFragment.setEvent(event);
                    getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostFragment).commit();
                } else {
                    Toast.makeText(getBaseContext(), "this is more people event", Toast.LENGTH_SHORT).show();
                    eventDetailHostFragment = new EventDetailHostFragment();
                    eventDetailHostFragment.setEvent(event);
                    getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostFragment).commit();
                }
            } else {
                // solo event
                Toast.makeText(getBaseContext(), "this is solo event", Toast.LENGTH_SHORT);
                eventDetailForSoloFragment = new EventDetailForSoloFragment();
                eventDetailForSoloFragment.setEvent(event);
                getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailForSoloFragment).commit();
            }
        } else {
            Toast.makeText(getBaseContext(), "this is attendee", Toast.LENGTH_SHORT).show();
            eventDetailHostFragment = new EventDetailHostFragment();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostFragment).commit();
//
        }


    }

    public void gotoWeekViewCalendar() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(request, UPDATE_EVENT);
        startActivity(intent);
    }

    public void confirmAndGotoWeekViewCalendar(Event event, boolean[] suggestTimeSlotConfirmArray) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.time_slots_confirm_array), suggestTimeSlotConfirmArray);
        intent.putExtra(getResources().getString(R.string.event), event);
        startActivity(intent);
    }

    public void gotoEventDetail(InviteeTimeslotFragment fragment) {
        if (eventDetailHostFragment != null && eventDetailHostFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        }
    }

    public void toAttendeeView(long time) {
        if (inviteeTimeslotFragment != null && inviteeTimeslotFragment.isAdded()) {
            inviteeTimeslotFragment.setTime(time);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().show(inviteeTimeslotFragment).commit();
        } else {
            inviteeTimeslotFragment = new InviteeTimeslotFragment();
            inviteeTimeslotFragment.setTime(time);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, inviteeTimeslotFragment).commit();
        }
    }

    public void toEditEvent(Fragment fragment, Event event) {
        if (eventEditFragment != null && eventEditFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        } else {
            eventEditFragment = new EventEditFragment();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventEditFragment).commit();
        }
    }


    public void toEventDetail(Event event) {
        if (eventDetailHostFragment != null && eventDetailHostFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        } else {
            eventDetailHostFragment = new EventDetailHostFragment();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        }
    }

    public void toTimeSlotView(String tag, Event event) {
        if (eventDetailHostTimeSlotFragment != null && eventDetailHostTimeSlotFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            eventDetailHostTimeSlotFragment.setEvent(event);
            eventDetailHostTimeSlotFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().show(eventDetailHostTimeSlotFragment).commit();
        } else {
            eventDetailHostTimeSlotFragment = new EventDetailHostTimeSlotFragment();
            eventDetailHostTimeSlotFragment.setEvent(event);
            eventDetailHostTimeSlotFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostTimeSlotFragment).commit();
        }
    }

    public void toHostEventDetail(Event event, Fragment fragment) {
        if (eventDetailHostFragment != null && eventDetailHostFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        } else {
            eventDetailHostFragment = new EventDetailHostFragment();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostFragment).commit();
        }
    }

    // from timeslot push cancle button
    public void fromTimeSlotToHostEdit(Event event,Fragment fragment){
        if (eventEditFragment != null && eventEditFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        }else{
            eventEditFragment = new EventEditFragment();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventEditFragment).commit();
        }
    }

    public void toLocationPicker(String tag, Fragment fragment) {
        if (locationPickerFragment != null && locationPickerFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            locationPickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().show(locationPickerFragment).commit();
        } else {
            locationPickerFragment = new EventLocationPickerFragment();
            locationPickerFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, locationPickerFragment).commit();
        }
    }


    public void fromHostEditToTimeSlotView(String tag, Event event,Fragment fragment){
        if (eventDetailHostTimeSlotFragment != null && eventDetailHostTimeSlotFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            eventDetailHostTimeSlotFragment.setEvent(event);
            eventDetailHostTimeSlotFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().show(eventDetailHostTimeSlotFragment).commit();
        }else{
            eventDetailHostTimeSlotFragment = new EventDetailHostTimeSlotFragment();
            eventDetailHostTimeSlotFragment.setEvent(event);
            eventDetailHostTimeSlotFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostTimeSlotFragment).commit();
        }
    }

    public void toInviteePicker(String tag, Fragment fragment){
        if (inviteeFragment != null && inviteeFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            inviteeFragment.setEvent(event);
            inviteeFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().show(inviteeFragment).commit();
        }else{
            inviteeFragment = new InviteeFragment();
            inviteeFragment.setEvent(event);
            inviteeFragment.setTag(tag);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, inviteeFragment).commit();
        }
    }

    public void fromInviteeToEditEvent(){
        if (eventEditFragment != null && eventEditFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(inviteeFragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        }else{
            // should never call this
            eventEditFragment = new EventEditFragment();
            getSupportFragmentManager().beginTransaction().hide(inviteeFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventEditFragment).commit();
        }
    }

    // from location picker to edit event page
    public void toEditEvent(Fragment fragment) {
        if (eventEditFragment != null && eventEditFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        } else {
            eventEditFragment = new EventEditFragment();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        }
    }

    // from host edit solo page to solo detail page
    public void toDetailSoloEvent(Event event){
        if (eventDetailForSoloFragment != null && eventDetailForSoloFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            eventDetailForSoloFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventDetailForSoloFragment).commit();
        }else{
            eventDetailForSoloFragment = new EventDetailForSoloFragment();
            eventDetailForSoloFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailForSoloFragment).commit();
        }
    }


    public void checkPermission(String tag){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    toPhotoPicker();
                }else {
                    Toast.makeText(getBaseContext(), "retry",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void toPhotoPicker(){
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        int maxNum = 3;
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA,true);
        startActivityForResult(intent, ACTIVITY_PHOTOPICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // this is the recall for photo urls
        switch (requestCode){
            case ACTIVITY_PHOTOPICKER: {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                    eventEditFragment.setPhotos(result);
                }
            }
        }
    }

    // this is for invitee confirm
    public void confirmAndGotoWeekViewCalendar(Event event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
