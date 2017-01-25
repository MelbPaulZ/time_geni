package org.unimelb.itime.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.ActivityMainBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageInboxMessage;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.ui.fragment.MainCalendarFragment;
import org.unimelb.itime.ui.fragment.MainContactFragment;
import org.unimelb.itime.ui.fragment.MainInboxFragment;
import org.unimelb.itime.ui.fragment.MainSettingFragment;
import org.unimelb.itime.ui.fragment.calendars.CalendarBaseViewFragment;
import org.unimelb.itime.ui.mvpview.MainTabBarView;
import org.unimelb.itime.ui.presenter.MainTabBarPresenter;
import org.unimelb.itime.ui.viewmodel.MainTabBarViewModel;

import java.util.List;

public class MainActivity extends BaseActivity<MainTabBarView, MainTabBarPresenter> implements MainTabBarView{

    private final static String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private MvpFragment[] tagFragments;

    private ActivityMainBinding binding;
    private MainTabBarViewModel tabBarViewModel;

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
        init();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//
//    }

    @NonNull
    @Override
    public MainTabBarPresenter createPresenter() {
        return new MainTabBarPresenter(this);
    }

    private void init(){
        tagFragments = new MvpFragment[4];
        tagFragments[0] = new MainCalendarFragment();
        tagFragments[1] = new MainContactFragment();
        tagFragments[2] = new MainInboxFragment();
        tagFragments[3] = new MainSettingFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[0]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[1]);
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[2], MainInboxFragment.class.getSimpleName());
        fragmentTransaction.add(R.id.main_fragment_container, tagFragments[3], MainSettingFragment.class.getSimpleName());

        fragmentTransaction.commit();
        showFragmentById(0);
    }

    /**
     * for listen the badge of inbox tab
     * @param messageInboxMessage
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenInbox(MessageInboxMessage messageInboxMessage){
        List<Message> messageList = DBManager.getInstance(getApplicationContext()).getAllMessages();
        int unReadNum = 0;
        for (Message message : messageList){
            if (message.getHasBadge()){
                unReadNum+=1;
            }
        }
        tabBarViewModel.setUnReadNum(unReadNum+"");
        ((MainInboxFragment)tagFragments[2]).setData(messageList);
    }

    /**
     * for listen the badge of contact tab
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenFriendRequest(MessageNewFriendRequest msg){
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CalendarBaseViewFragment.REQ_EVENT_CREATE ){
            if (resultCode == Activity.RESULT_OK) {
                ((MainCalendarFragment) tagFragments[0]).reloadEvent();
                ((MainCalendarFragment) tagFragments[0]).scrollToWithOffset(eventManager.getCurrentEvent().getStartTime());
            }
        }else if (requestCode == CalendarBaseViewFragment.REQ_EVENT_DETAIL ){
            if (resultCode == Activity.RESULT_OK) {
                ((MainCalendarFragment) tagFragments[0]).reloadEvent();
                ((MainCalendarFragment) tagFragments[0]).scrollToWithOffset(eventManager.getCurrentEvent().getStartTime());
            }
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
