package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;

public class LoginActivity extends EmptyActivity {

    private static final String TAG = "LoginActivity";

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();

        LoginIndexFragment indexFragment = new LoginIndexFragment();
        fragmentManager.beginTransaction().
                replace(getFragmentContainerId(), indexFragment)
                .commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_framelayout;
    }
}
