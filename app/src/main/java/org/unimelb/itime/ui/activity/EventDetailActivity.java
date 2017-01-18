package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.event.EventDetailFragment;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;
import org.unimelb.itime.util.EventUtil;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends EmptyActivity {

    private List<BaseUiFragment> fragmentList = new ArrayList<>();
    private Event event;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int ACTIVITY_PHOTOPICKER = 2;
    private FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        EventDetailFragment fragment = new EventDetailFragment();
        initEvent();
        EventManager.getInstance(getApplicationContext()).setCurrentEvent(event);
        Event cpyEvent = EventUtil.copyEvent(event);
        fragment.setData(cpyEvent);
        fragmentManager.beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    private void initEvent(){
        Intent intent = getIntent();
        String eventUid = intent.getStringExtra("event_uid");
        if (eventUid == null || eventUid.length()==0){
            throw new RuntimeException("event detail activity dont have parameters eventUid");
        }
        long startTime = intent.getLongExtra("start_time",0);
        event = DBManager.getInstance(getApplicationContext()).getEvent(eventUid);
//        event = DBManager.getInstance(getApplicationContext()).find(Event.class, "eventUid", eventUid).get(0);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.event_detail_fragment;
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
                    EventEditFragment eventEditFragment = (EventEditFragment) getSupportFragmentManager().findFragmentById(getFragmentContainerId());
                    eventEditFragment.setPhotos(result);
                    openFragment(eventEditFragment);
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
