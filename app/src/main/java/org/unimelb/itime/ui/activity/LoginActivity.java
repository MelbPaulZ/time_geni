package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.LoginEmailSentFragment;
import org.unimelb.itime.ui.fragment.login.LoginFindFriendFragment;
import org.unimelb.itime.ui.fragment.login.LoginFragment;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.fragment.login.LoginInputEmailFragment;
import org.unimelb.itime.ui.fragment.login.LoginPickAvatarFragment;
import org.unimelb.itime.ui.fragment.login.LoginResetPasswordFragment;
import org.unimelb.itime.ui.fragment.login.LoginSetPWFragment;

public class LoginActivity extends EmptyActivity {

    private static final String TAG = "LoginActivity";
    private LoginIndexFragment indexFragment;
    private LoginInputEmailFragment inputEmailFragment;
    private LoginSetPWFragment setPWFragment;
    private LoginPickAvatarFragment pickAvatarFragment;
    private LoginFragment loginFragment;
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
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, indexFragment, indexFragment.getClassName()).hide(indexFragment).commit();
        inputEmailFragment = new LoginInputEmailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, inputEmailFragment, inputEmailFragment.getClassName()).hide(inputEmailFragment).commit();
        setPWFragment = new LoginSetPWFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, setPWFragment, setPWFragment.getClassName()).hide(setPWFragment).commit();
        pickAvatarFragment = new LoginPickAvatarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, pickAvatarFragment, pickAvatarFragment.getClassName()).hide(pickAvatarFragment).commit();
        loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, loginFragment, loginFragment.getClassName()).hide(loginFragment).commit();
        resetPasswordFragment = new LoginResetPasswordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, resetPasswordFragment, resetPasswordFragment.getClassName()).hide(resetPasswordFragment).commit();
        sentFragment = new LoginEmailSentFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, sentFragment, sentFragment.getClassName()).hide(sentFragment).commit();
        findFriendFragment = new LoginFindFriendFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, findFriendFragment, findFriendFragment.getClassName()).hide(findFriendFragment).commit();
    }

    private void showIndexFragment(){
        getSupportFragmentManager().beginTransaction().show(indexFragment).commit();
    }


}
