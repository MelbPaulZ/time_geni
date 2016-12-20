package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.LoginEmailSentFragment;
import org.unimelb.itime.ui.fragment.login.LoginFindFriendFragment;
import org.unimelb.itime.ui.fragment.login.LoginFragmentNew;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.fragment.login.LoginInputEmailFragment;
import org.unimelb.itime.ui.fragment.login.LoginPickAvatarFragment;
import org.unimelb.itime.ui.fragment.login.LoginResetPasswordFragment;
import org.unimelb.itime.ui.fragment.login.LoginSetPWFragment;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private LoginIndexFragment indexFragment;
    private LoginInputEmailFragment inputEmailFragment;
    private LoginSetPWFragment setPWFragment;
    private LoginPickAvatarFragment pickAvatarFragment;
    private LoginFragmentNew loginFragmentNew;
    private LoginResetPasswordFragment resetPasswordFragment;
    private LoginEmailSentFragment sentFragment;
    private LoginFindFriendFragment findFriendFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initFragment();
        showIndexFragment();
    }

    private void initFragment(){
        indexFragment = new LoginIndexFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, indexFragment).hide(indexFragment).commit();
        inputEmailFragment = new LoginInputEmailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, inputEmailFragment).hide(inputEmailFragment).commit();
        setPWFragment = new LoginSetPWFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, setPWFragment).hide(setPWFragment).commit();
        pickAvatarFragment = new LoginPickAvatarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, pickAvatarFragment).hide(pickAvatarFragment).commit();
        loginFragmentNew = new LoginFragmentNew();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, loginFragmentNew).hide(loginFragmentNew).commit();
        resetPasswordFragment = new LoginResetPasswordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, resetPasswordFragment).hide(resetPasswordFragment).commit();
        sentFragment = new LoginEmailSentFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, sentFragment).hide(sentFragment).commit();
        findFriendFragment = new LoginFindFriendFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, findFriendFragment).hide(findFriendFragment).commit();
    }

    private void showIndexFragment(){
        getSupportFragmentManager().beginTransaction().show(indexFragment).commit();
    }


}
