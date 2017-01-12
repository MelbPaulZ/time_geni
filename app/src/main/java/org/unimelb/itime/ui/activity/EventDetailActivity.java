package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.ViewMainCalendarFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.fragment.event.EventDetailFragment;
import org.unimelb.itime.ui.fragment.event.EventDetailTimeSlotFragment;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;
import org.unimelb.itime.ui.fragment.event.EventPhotoGridFragment;
import org.unimelb.itime.ui.fragment.event.InviteeTimeslotFragment;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    private List<BaseUiFragment> fragmentList = new ArrayList<>();
    private Event event;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int ACTIVITY_PHOTOPICKER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        event = EventManager.getInstance(getApplicationContext()).getCurrentEvent();
        initFragments();
        getSupportFragmentManager().beginTransaction().show(fragmentList.get(0)).commit();

    }

    public void initFragments() {
        fragmentList.add(new EventDetailFragment());
//        fragmentList.add(new EventEditFragment());
        fragmentList.add(new EventDetailTimeSlotFragment());
//        fragmentList.add(new EventLocationPickerFragment());
        fragmentList.add(new InviteeFragment());
        fragmentList.add(new InviteeTimeslotFragment());
        fragmentList.add(new ViewMainCalendarFragment());
        fragmentList.add(new EventPhotoGridFragment());
        hideAllFragments();
    }

    public void hideAllFragments() {
        for (BaseUiFragment fragment : fragmentList) {
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, fragment, fragment.getClassName()).hide(fragment).commit();
        }
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    toPhotoPicker();
                } else {
                    Toast.makeText(getBaseContext(), "retry", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    public void toPhotoPicker() {
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        int maxNum = 3;
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        startActivityForResult(intent, ACTIVITY_PHOTOPICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // this is the recall for photo urls
        switch (requestCode) {
            case ACTIVITY_PHOTOPICKER: {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                    EventEditFragment eventEditFragment = (EventEditFragment) getSupportFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
                    eventEditFragment.setPhotos(result);
                }
            }
        }
    }

    // this is for invitee accept
    public void confirmAndGotoWeekViewCalendar(Event event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
