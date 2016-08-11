package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
