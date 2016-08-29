package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.EventDetailFragment;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Event event= (Event) getIntent().getSerializableExtra("Event");

        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        getFragmentManager().beginTransaction().add(R.id.event_detail_fragment,eventDetailFragment).commit();



    }
}
