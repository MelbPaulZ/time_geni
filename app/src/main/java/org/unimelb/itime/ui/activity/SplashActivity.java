package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.base.C;
import org.unimelb.itime.databinding.ActivityOnBoardingBinding;
import org.unimelb.itime.ui.viewmodel.OnBoardingViewModel;
import org.unimelb.itime.util.AppUtil;

/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class SplashActivity extends EmptyActivity {
    private ActivityOnBoardingBinding binding;
    private static final String SHOW_ON_BOARDING = "showOnBoarding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean showOnboarding = AppUtil.getSharedPreferences(getApplicationContext()).getBoolean(SHOW_ON_BOARDING, true);
        if(showOnboarding) {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);
            markUnshow();
            OnBoardingViewModel viewModel = new OnBoardingViewModel(getApplicationContext());
            viewModel.setSkipListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startLogin();
                }
            });
            binding.setViewModel(viewModel);
        }else{
            startLogin();
        }
    }

    private void markUnshow(){
        SharedPreferences.Editor editor = AppUtil.getSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean(SHOW_ON_BOARDING, false);
        editor.apply();
    }

    public void startLogin(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
