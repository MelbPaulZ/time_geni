package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.base.C;
import org.unimelb.itime.databinding.ActivityOnBoardingBinding;
import org.unimelb.itime.ui.fragment.OnBoardingFragment;
import org.unimelb.itime.ui.fragment.OnBoardingItemFragment;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.viewmodel.OnBoardingViewModel;
import org.unimelb.itime.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class SplashActivity extends EmptyActivity {
    private ActivityOnBoardingBinding binding;
    private static final String SHOW_ON_BOARDING = "showOnBoarding";
    private ViewPager viewPager;
    OnBoardingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            DataBindingUtil.setContentView(this, R.layout.activity_setting);
            FragmentManager fragmentManager = getSupportFragmentManager();
            OnBoardingFragment fragment = new OnBoardingFragment();
            fragmentManager.beginTransaction().replace(getFragmentContainerId(), fragment).commit();
    }

//    private void markUnshow(){
//        SharedPreferences.Editor editor = AppUtil.getSharedPreferences(getApplicationContext()).edit();
//        editor.putBoolean(SHOW_ON_BOARDING, false);
//        editor.apply();
//    }
//
//    public void startLogin(){
//        Intent intent = new Intent();
//        intent.setClass(this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    public void initViewPager(){
//        OnBoardingFragmentPagerAdapter adapter = new OnBoardingFragmentPagerAdapter(getSupportFragmentManager());
//        OnBoardingItemFragment fragment1 = new OnBoardingItemFragment();
//        fragment1.setImg(R.drawable.onboarding_page1_graphy);
//        OnBoardingItemFragment fragment2 = new OnBoardingItemFragment();
//        fragment2.setImg(R.drawable.onboarding_page2_graphy);
//        OnBoardingItemFragment fragment3 = new OnBoardingItemFragment();
//        fragment3.setImg(R.drawable.onboarding_page3_graphy);
//        OnBoardingItemFragment fragment4 = new OnBoardingItemFragment();
//        fragment4.setImg(R.drawable.onboarding_page4_graphy);
//        LoginIndexFragment loginFragment = new LoginIndexFragment();
//
//        adapter.addFragment(fragment1);
//        adapter.addFragment(fragment2);
//        adapter.addFragment(fragment3);
//        adapter.addFragment(fragment4);
//        adapter.addFragment(loginFragment);
//
//        viewPager = binding.viewpager;
//        viewPager.setAdapter(adapter);
//        viewPager.setOnPageChangeListener(viewModel.getOnPageChangeListener());
//
//    }
//
//    public static class OnBoardingFragmentPagerAdapter extends FragmentPagerAdapter {
//        private List<Fragment> fragments = new ArrayList<>();
//
//        public void addFragment(Fragment fragment){
//            fragments.add(fragment);
//        }
//
//        public OnBoardingFragmentPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return fragments.get(i);
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//    }
}
