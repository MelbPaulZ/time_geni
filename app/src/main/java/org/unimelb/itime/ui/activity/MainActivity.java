package org.unimelb.itime.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.ui.fragment.contact.ContactHomePageFragment;
import org.unimelb.itime.ui.fragment.MainCalendarFragment;
import org.unimelb.itime.ui.fragment.MainInboxFragment;
import org.unimelb.itime.ui.fragment.settings.SettingIndexFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileNameFragment;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.presenter.MainTabBarPresenter;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity<MainTabBarView, MainTabBarPresenter> implements MainTabBarView{

    private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MvpFragment[] tagFragments;

    private ActivityMainBinding binding;
    private MainTabBarViewModel tabBarViewModel;

    private final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_AND_CAMERA = 1001;
    private EventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        eventManager = EventManager.getInstance(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        tabBarViewModel = new MainTabBarViewModel(getPresenter());
        binding.setTabBarVM(tabBarViewModel);
        tabBarViewModel.setUnReadNum(0+""); // give a default value
        checkPermission();
//        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA , Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_AND_CAMERA);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_AND_CAMERA:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    init();
                }else {
                    Toast.makeText(getApplicationContext(), "needs permissions to continue", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @NonNull
    @Override
    public MainTabBarPresenter createPresenter() {
        return new MainTabBarPresenter(this);
    }

    private void init(){
        tagFragments = new MvpFragment[4];
        tagFragments[0] = new MainCalendarFragment();
        tagFragments[1] = new ContactHomePageFragment();
        tagFragments[2] = new MainInboxFragment();
        tagFragments[3] = new SettingIndexFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[0]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[1]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[2], MainInboxFragment.class.getSimpleName());
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[3], SettingIndexFragment.class.getSimpleName());

        fragmentTransaction.commit();
        showFragmentById(0);
        refreshTabStatus(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getInboxMessage(MessageInboxMessage messageInboxMessage){
        List<Message> messageList = DBManager.getInstance(getApplicationContext()).getAllMessages();
        int unReadNum = 0;
        for (Message message : messageList){
            if (!message.isRead()){
                unReadNum+=1;
            }
        }
        tabBarViewModel.setUnReadNum(unReadNum+"");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setNewFriendRequestCount(MessageNewFriendRequest msg){
        tabBarViewModel.setUnReadFriendRequest(msg.count);
    }


    @Override
    public void showFragmentById(int pageId) {
        fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < tagFragments.length; i++){
            if (pageId == i){
                fragmentTransaction.show(tagFragments[i]);
            }else{
                fragmentTransaction.hide(tagFragments[i]);
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void refreshTabStatus(int pageId){

    }


    public void startEventCreateActivity(Calendar startTime){
        Intent intent = new Intent(this, EventCreateActivity.class);
        intent.putExtra(BaseActivity.TASK, BaseActivity.TASK_SELF_CREATE_EVENT);
        intent.putExtra("start_time", startTime.getTimeInMillis());
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        startActivityForResult(intent, EventUtil.ACTIVITY_CREATE_EVENT,bundleAnimation);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventUtil.ACTIVITY_CREATE_EVENT ){
            if (resultCode == Activity.RESULT_OK) {
                ((MainCalendarFragment) tagFragments[0]).reloadEvent();
                ((MainCalendarFragment) tagFragments[0]).scrollToWithOffset(eventManager.getCurrentEvent().getStartTime());
            }
        }else if (requestCode == EventUtil.ACTIVITY_EDIT_EVENT ){
            if (resultCode == Activity.RESULT_OK) {
                ((MainCalendarFragment) tagFragments[0]).reloadEvent();
                ((MainCalendarFragment) tagFragments[0]).scrollToWithOffset(eventManager.getCurrentEvent().getStartTime());
            }
        }
    }


    @Override
    protected void onDestroy() {
//        stopService(new Intent(this, RemoteService.class));
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
