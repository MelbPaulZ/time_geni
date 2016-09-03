package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.EventDetailForInviteeFragment;

public class EventDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Event event= (Event) getIntent().getSerializableExtra(getString(R.string.event));

        EventDetailForInviteeFragment eventDetailFragment = new EventDetailForInviteeFragment();
        eventDetailFragment.setEvent(event);
        getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment,eventDetailFragment).commit();

    }

    public void gotoWeekViewCalendar(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void confirmAndGotoWeekViewCalendar(Event event, boolean[] suggestTimeSlotConfirmArray){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getResources().getString(R.string.time_slots_confirm_array),suggestTimeSlotConfirmArray);
        intent.putExtra(getResources().getString(R.string.event),event);
        startActivity(intent);
    }
}
