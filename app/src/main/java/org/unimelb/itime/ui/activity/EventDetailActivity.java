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

    private Event event;
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
//        event = DBManager.getInstance(getApplicationContext()).getEvent(eventUid);
        event = DBManager.getInstance(getApplicationContext()).find(Event.class, "eventUid", eventUid).get(0);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.event_detail_fragment;
    }




}
