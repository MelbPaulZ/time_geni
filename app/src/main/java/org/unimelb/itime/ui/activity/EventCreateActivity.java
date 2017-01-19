package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.util.rulefactory.InviteeUtil;

import java.util.ArrayList;

import static android.R.attr.fragment;
import static android.R.attr.start;


public class EventCreateActivity extends EmptyActivity implements PlaceSelectionListener {
    private String TAG = "EventCreateActivity";
    private FragmentManager fragmentManager;
    private Event event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        long startTime = getIntent().getLongExtra("start_time",0);
        fragmentManager = getSupportFragmentManager();
        if (startTime!=0) {
            event = initNewEvent(startTime);
        }else{
            event = (Event) getIntent().getSerializableExtra("event");
        }
        EventEditFragment fragment = new EventEditFragment();

        fragment.setEvent(event);

        // if has default contact, this only happens when invitee a contact to event
        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        if (contact!=null){
            event.addInvitee(InviteeUtil.getInstance().contactToInvitee(contact, event));
        }
        EventManager.getInstance(getApplicationContext()).setCurrentEvent(event);
        fragmentManager.beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    private Event initNewEvent(long startTime){
        // initial default values for new event
        Event event = new Event();
        event.setEventUid(AppUtil.generateUuid());
        event.setHostUserUid(UserUtil.getInstance(getApplicationContext()).getUserUid());
        long endTime = startTime + 3600 * 1000;
        event.setStartTime(startTime);
        event.setEndTime(endTime);

        return event;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.create_event_fragment;
    }


    @Subscribe
    public void gotoUrl(MessageUrl messageUrl) {
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
