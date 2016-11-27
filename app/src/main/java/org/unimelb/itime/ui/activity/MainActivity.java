package org.unimelb.itime.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.MainCalendarFragment;
import org.unimelb.itime.ui.fragment.MainContactsFragment;
import org.unimelb.itime.ui.fragment.MainInboxFragment;
import org.unimelb.itime.ui.fragment.MainSettingsFragment;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.presenter.MainTabBarPresenter;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.Calendar;

public class MainActivity extends MvpActivity<MainTabBarView, MainTabBarPresenter> implements MainTabBarView{

    private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MvpFragment[] tagFragments;

    private ActivityMainBinding binding;
    private MainTabBarViewModel tabBarViewModel;

    public final static int CREATE_EVENT = 0;
    public final static int EDIT_EVENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        tabBarViewModel = new MainTabBarViewModel(getPresenter());
        binding.setTabBarVM(tabBarViewModel);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @NonNull
    @Override
    public MainTabBarPresenter createPresenter() {
        return new MainTabBarPresenter(this);
    }


    private void init(){
        tagFragments = new MvpFragment[4];
        tagFragments[0] = new MainCalendarFragment();
        tagFragments[1] = new MainContactsFragment();
        tagFragments[2] = new MainInboxFragment();
        tagFragments[3] = new MainSettingsFragment();


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[0]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[1]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[2]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[3]);
        fragmentTransaction.commit();
        showFragmentById(0);
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


    public void startEventCreateActivity(){
        Intent intent = new Intent(this,EventCreateActivity.class);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        Calendar calendar = Calendar.getInstance();
        initNewEvent(calendar);
        startActivityForResult(intent, EventUtil.ACTIVITY_CREATE_EVENT,bundleAnimation);
    }
    public void startEventCreateActivity(Calendar startTime){
        Intent intent = new Intent(this, EventCreateActivity.class);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        initNewEvent(startTime);
        startActivityForResult(intent, EventUtil.ACTIVITY_CREATE_EVENT,bundleAnimation);
    }


    public void initNewEvent(Calendar startTimeCalendar){
        // initial default values for new event
        Event event = EventManager.getInstance().getNewEvent();
        EventManager.getInstance().setCurrentEvent(event);
        event.setEventUid(AppUtil.generateUuid());
        event.setHostUserUid(UserUtil.getUserUid());
        long endTime = startTimeCalendar.getTimeInMillis() + 3600 * 1000;
        event.setStartTime(startTimeCalendar.getTimeInMillis());
        event.setEndTime(endTime);
        EventManager.getInstance().setCurrentEvent(event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EventUtil.ACTIVITY_CREATE_EVENT ){
            ((MainCalendarFragment)tagFragments[0]).reloadEvent();
        }else if (requestCode == EventUtil.ACTIVITY_EDIT_EVENT ){
            ((MainCalendarFragment)tagFragments[0]).reloadEvent();
        }
    }

}
