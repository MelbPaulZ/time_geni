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
import org.unimelb.itime.messageevent.MessageUrl;
import org.unimelb.itime.ui.fragment.event.EventCreateDetailBeforeSendingFragment;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;

import java.util.ArrayList;


public class EventCreateActivity extends BaseActivity implements PlaceSelectionListener {
    private String TAG = "EventCreateActivity";
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private final int ACTIVITY_PHOTOPICKER = 2;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        fragmentManager = getSupportFragmentManager();
        EventEditFragment fragment = new EventEditFragment();
        fragmentManager.beginTransaction()
                .replace(getFragmentContainerId(), fragment)
                .commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.create_event_fragment;
    }

    public void checkPermission(String tag){
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
//                    if (tag==getString(R.string.tag_create_event)){
//                        EventCreateNewFragment eventCreateNewFragment = (EventCreateNewFragment) getSupportFragmentManager().findFragmentByTag(EventCreateNewFragment.class.getSimpleName());
//                        eventCreateNewFragment.setPhotos(result);
//                    }else if (tag== getString(R.string.tag_create_event_before_sending)){
                        EventCreateDetailBeforeSendingFragment eventCreateDetailBeforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getSupportFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
                        eventCreateDetailBeforeSendingFragment.setPhotos(result);
//                    }
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
