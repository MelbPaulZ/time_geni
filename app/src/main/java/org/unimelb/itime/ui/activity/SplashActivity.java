package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ActivityOnBoardingBinding;
import org.unimelb.itime.ui.viewmodel.OnBoardingViewModel;

/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class SplashActivity extends EmptyActivity {
    ActivityOnBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding);
        OnBoardingViewModel viewModel = new OnBoardingViewModel(getApplicationContext());
        viewModel.setSkipListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });
        binding.setViewModel(viewModel);
    }

    public void startLogin(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}
