package org.unimelb.itime.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.ui.fragment.MainCalendarFragment;
import org.unimelb.itime.ui.fragment.MainContactsFragment;
import org.unimelb.itime.ui.fragment.MainInboxFragment;
import org.unimelb.itime.ui.fragment.MainSettingsFragment;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.presenter.MainTabBarPresenter;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.Calendar;

public class MainActivity extends MvpActivity<MainTabBarView, MainTabBarPresenter> implements MainTabBarView{

    private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Fragment[] tagFragments;

    private ActivityMainBinding binding;
    private MainTabBarViewModel tabBarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        tabBarViewModel = new MainTabBarViewModel(getPresenter());
        binding.setTabBarVM(tabBarViewModel);
        init();
//        getNewEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getNewEvent(intent);
    }

    public void getNewEvent(Intent intent){
        if (intent.hasExtra(getString(R.string.new_event))) {
            Event event = (Event) intent.getSerializableExtra(getString(R.string.new_event));
            ((MainCalendarFragment)tagFragments[0]).addNewEvent(event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        getNewEvent();
    }

    @NonNull
    @Override
    public MainTabBarPresenter createPresenter() {
        return new MainTabBarPresenter(this);
    }


    private void init(){
        tagFragments = new Fragment[4];
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

        startActivity(intent, bundleAnimation);
    }
    public void startEventCreateActivity(Calendar startTime){
        Intent intent = new Intent(this, EventCreateActivity.class);
        Bundle bundleAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(),R.anim.create_event_animation1, R.anim.create_event_animation2).toBundle();
        intent.putExtra(getString(R.string.new_event),startTime.getTimeInMillis());
        startActivity(intent, bundleAnimation);

    }


//    public void startEventEditActivity(ITimeEventInterface iTimeEventInterface){
//        Intent intent = new Intent(this,EventDetailActivity.class);
//        Event event = (Event) iTimeEventInterface;
//        intent.putExtra(getString(R.string.event),event);
//        startActivityForResult(intent,1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
