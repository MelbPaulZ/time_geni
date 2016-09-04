package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.fragment.EventDetailForInviteeFragment;
import org.unimelb.itime.ui.fragment.EventDetailForSoloFragment;
import org.unimelb.itime.ui.fragment.EventDetailHostFragment;
import org.unimelb.itime.ui.fragment.EventEditFragment;
import org.unimelb.itime.ui.fragment.InviteeTimeslotFragment;

public class EventDetailActivity extends AppCompatActivity {

    private EventDetailHostFragment eventDetailHostFragment;
    private EventDetailForInviteeFragment eventDetailForInviteeFragment;
    private EventDetailForSoloFragment eventDetailForSoloFragment;
    private InviteeTimeslotFragment inviteeTimeslotFragment;
    private EventEditFragment eventEditFragment;
    private Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        event= (Event) getIntent().getSerializableExtra(getString(R.string.event));

        if (event.isHost()){
            if (event.hasAttendee()){
                // group event, and is host
                if (event.getAttendees().size()==1){
                    Toast.makeText(getBaseContext(),"this is two people event",Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getBaseContext(),"this is more people event",Toast.LENGTH_SHORT);
                    eventDetailHostFragment = new EventDetailHostFragment();
                    eventDetailHostFragment.setEvent(event);
                    getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailHostFragment).commit();
                }
            }else{
                // solo event
                Toast.makeText(getBaseContext(),"this is solo event",Toast.LENGTH_SHORT);
                eventDetailForSoloFragment = new EventDetailForSoloFragment();
                eventDetailForSoloFragment.setEvent(event);
                getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, eventDetailForSoloFragment).commit();
            }
        }else{
            Toast.makeText(getBaseContext(),"this is attendee",Toast.LENGTH_SHORT);
            eventDetailForInviteeFragment = new EventDetailForInviteeFragment();
            eventDetailForInviteeFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment,eventDetailForInviteeFragment).commit();
        }



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

    public void gotoEventDetail(InviteeTimeslotFragment fragment){
        if (eventDetailHostFragment!=null && eventDetailHostFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        }
    }

    public void toAttendeeView(long time){
        if (inviteeTimeslotFragment != null && inviteeTimeslotFragment.isAdded()){
            inviteeTimeslotFragment.setTime(time);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().show(inviteeTimeslotFragment).commit();
        }else{
            inviteeTimeslotFragment = new InviteeTimeslotFragment();
            inviteeTimeslotFragment.setTime(time);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment, inviteeTimeslotFragment).commit();
        }
    }

    public void toEditEvent(Event event){
        if (eventEditFragment !=null && eventEditFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventEditFragment).commit();
        }else{
            eventEditFragment = new EventEditFragment();
            eventEditFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(eventDetailHostFragment).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.event_detail_fragment,eventEditFragment).commit();
        }
    }


    public void toEventDetail(Event event){
        if (eventDetailHostFragment!=null && eventDetailHostFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        }else{
            eventDetailHostFragment = new EventDetailHostFragment();
            eventDetailHostFragment.setEvent(event);
            getSupportFragmentManager().beginTransaction().hide(eventEditFragment).commit();
            getSupportFragmentManager().beginTransaction().show(eventDetailHostFragment).commit();
        }
    }
}
