package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.event.EventCreateDetailBeforeSendingFragment;
import org.unimelb.itime.ui.fragment.contact.InviteeFragment;
import org.unimelb.itime.ui.fragment.event.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.EventLocationPickerFragment;
import org.unimelb.itime.ui.fragment.event.EventTimeSlotCreateFragment;
import org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment;

import java.util.ArrayList;
import java.util.List;


public class EventCreateActivity extends AppCompatActivity implements PlaceSelectionListener {
    private String TAG = "EventCreateActivity";

    private List<BaseUiFragment> fragmentList = new ArrayList<>();
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int ACTIVITY_PHOTOPICKER = 2;
    private String tag = ""; // this is for identifying which request is it


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        initFragments();
        Log.i(TAG, "onCreate: "+ System.currentTimeMillis());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: " + System.currentTimeMillis());
    }

    public void initFragments(){
        fragmentList.add(new EventCreateNewFragment());

        hideAllFragments();
        getSupportFragmentManager().beginTransaction().show(fragmentList.get(0)).commit();
        initRestFragments();
    }

    private void initRestFragments(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                fragmentList.add(new InviteeFragment());
                fragmentList.add(new EventTimeSlotViewFragment());
                fragmentList.add(new EventCreateDetailBeforeSendingFragment());
                fragmentList.add(new EventLocationPickerFragment());
                fragmentList.add(new EventTimeSlotCreateFragment());
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideAllFragments();
        }
    };

    public void hideAllFragments(){
        for (BaseUiFragment fragment: fragmentList){
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.create_event_fragment, fragment, fragment.getClassName()).hide(fragment).commit();
            }
        }

    }
    public void checkPermission(String tag){
        this.tag = tag;
       if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
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
//
//
//    // todo alert dialog show image
//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // this is the recall for photo urls
        switch (requestCode){
            case ACTIVITY_PHOTOPICKER: {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                    if (tag==getString(R.string.tag_create_event)){
                        EventCreateNewFragment eventCreateNewFragment = (EventCreateNewFragment) getSupportFragmentManager().findFragmentByTag(EventCreateNewFragment.class.getSimpleName());
                        eventCreateNewFragment.setPhotos(result);
                    }else if (tag== getString(R.string.tag_create_event_before_sending)){
                        EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getSupportFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
                        eventCreateDetailBeforeSendingFragment.setPhotos(result);
                    }
                }
            }
        }
    }
//
    public void toPhotoPicker(){
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        int maxNum = 3;
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        startActivityForResult(intent, ACTIVITY_PHOTOPICKER);
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
