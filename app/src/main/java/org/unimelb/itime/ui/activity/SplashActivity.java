package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.fragment.login.LoginInputEmailFragment;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private LoginIndexFragment indexFragment;
    private LoginInputEmailFragment inputEmailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.activity_login);
        initFragment();
    }

    public void initFragment(){
//        indexFragment = new LoginIndexFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, indexFragment).commit();
        inputEmailFragment = new LoginInputEmailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, inputEmailFragment).commit();
    }

}
