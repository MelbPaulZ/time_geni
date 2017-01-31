package org.unimelb.itime.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.databinding.ActivityOnBoardingBinding;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.activity.SplashActivity;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.viewmodel.OnBoardingItemViewModel;
import org.unimelb.itime.ui.viewmodel.OnBoardingViewModel;
import org.unimelb.itime.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/30.
 */

public class OnBoardingFragment extends BaseUiAuthFragment {
    private ActivityOnBoardingBinding binding;
    private static final String SHOW_ON_BOARDING = "showOnBoarding";
    private ViewPager viewPager;
    private OnBoardingViewModel viewModel;
    private OnBoardingFragmentPagerAdapter adapter;
    private OnBoardingItemFragment fragment1;
    private OnBoardingItemFragment fragment2;
    private OnBoardingItemFragment fragment3;
    private OnBoardingItemFragment fragment4;
    private LoginIndexFragment loginFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                    R.layout.activity_on_boarding, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        boolean showOnboarding = AppUtil.getSharedPreferences(getActivity()).getBoolean(SHOW_ON_BOARDING, true);
        if(showOnboarding) {
            markUnshow();
            viewModel = new OnBoardingViewModel(getActivity());
            binding.setViewModel(viewModel);
            viewModel.setSkipListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLogin();
                    Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                }
            });
            initViewPager();
        }else{
            startLogin();
        }
    }

    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }

    private void markUnshow(){
        SharedPreferences.Editor editor = AppUtil.getSharedPreferences(getActivity()).edit();
        editor.putBoolean(SHOW_ON_BOARDING, false);
        editor.apply();
    }

    public void startLogin(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void initViewPager(){
        adapter = new OnBoardingFragmentPagerAdapter(getChildFragmentManager());
        fragment1 = new OnBoardingItemFragment();
        fragment1.setImg(R.drawable.onboarding_page1_graphy);
        fragment2 = new OnBoardingItemFragment();
        fragment2.setImg(R.drawable.onboarding_page2_graphy);
        fragment3 = new OnBoardingItemFragment();
        fragment3.setImg(R.drawable.onboarding_page3_graphy);
        fragment4 = new OnBoardingItemFragment();
        fragment4.setImg(R.drawable.onboarding_page4_graphy);
        loginFragment = new LoginIndexFragment();

        adapter.addFragment(fragment1);
        adapter.addFragment(fragment2);
        adapter.addFragment(fragment3);
        adapter.addFragment(fragment4);
        adapter.addFragment(loginFragment);

        viewPager = binding.viewpager;
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(viewModel.getOnPageChangeListener());
    }

    @Override
    public void onResume(){
        super.onResume();
        viewPager.setCurrentItem(viewModel.getCurrentPage());
    }

    public static class OnBoardingFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();

        public void addFragment(Fragment fragment){
            fragments.add(fragment);
        }

        public OnBoardingFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
