package org.unimelb.itime.ui.activity;

import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;


public class TestActivity extends BaseActivity{

    private final static String TAG = "TestActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        SettingMyProfileFragment fragment = new SettingMyProfileFragment();
    }



}
