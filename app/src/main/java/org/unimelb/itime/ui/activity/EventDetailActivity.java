package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.event.EventDetailFragment;
import org.unimelb.itime.util.EventUtil;

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
//        Event cpyEvent = EventUtil.copyEvent(event);
        fragment.setData(event);
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
        long beginOfDay = EventUtil.getDayBeginMilliseconds(startTime);


        //find org event
        event = EventManager.getInstance(getBaseContext()).findEventByUid(eventUid);
        //if repeated, find the correspond child
        if (event != null && event.getRecurrence().length != 0){
            event = EventManager.getInstance(getBaseContext()).findRepeatedByUUUID(beginOfDay,eventUid);
        }
        //if deleted, find in db
        if (event == null){
            event = DBManager.getInstance(getApplicationContext()).find(Event.class, "eventUid", eventUid).get(0);
            event = event.clone();
            long duration = event.getDurationMilliseconds();
            event.setStartTime(startTime);
            event.setEndTime(startTime + duration);
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.event_detail_fragment;
    }




}
