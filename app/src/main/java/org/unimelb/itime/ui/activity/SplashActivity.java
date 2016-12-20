package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.LoginFragmentNew;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.fragment.login.LoginInputEmailFragment;
import org.unimelb.itime.ui.fragment.login.LoginPickAvatarFragment;
import org.unimelb.itime.ui.fragment.login.LoginSetPWFragment;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private LoginIndexFragment indexFragment;
    private LoginInputEmailFragment inputEmailFragment;
    private LoginSetPWFragment setPWFragment;
    private LoginPickAvatarFragment pickAvatarFragment;
    private LoginFragmentNew loginFragmentNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initFragment();
    }

    public void initFragment(){
//        indexFragment = new LoginIndexFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, indexFragment).commit();
//        inputEmailFragment = new LoginInputEmailFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, inputEmailFragment).commit();
//        setPWFragment = new LoginSetPWFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, setPWFragment).commit();
//        pickAvatarFragment = new LoginPickAvatarFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, pickAvatarFragment).commit();
        loginFragmentNew = new LoginFragmentNew();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, loginFragmentNew).commit();


    }


}
