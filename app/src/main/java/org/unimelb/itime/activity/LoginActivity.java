package org.unimelb.itime.activity;

import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}

