package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.C;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.fragment.LoginFragment;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.AuthUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LoginActivity";
    private LoginFragment loginFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFragment();
    }

    public void initFragment(){
        loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, loginFragment).commit();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

