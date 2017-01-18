package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.login.ResetPasswordSentFragment;
import org.unimelb.itime.ui.fragment.login.SignupFindFriendFragment;
import org.unimelb.itime.ui.fragment.login.LoginFragment;
import org.unimelb.itime.ui.fragment.login.LoginIndexFragment;
import org.unimelb.itime.ui.fragment.login.SignupInputEmailFragment;
import org.unimelb.itime.ui.fragment.login.SignupPickAvatarFragment;
import org.unimelb.itime.ui.fragment.login.ResetPasswordFragment;
import org.unimelb.itime.ui.fragment.login.SignupSetPWFragment;

public class LoginActivity extends EmptyActivity {

    private static final String TAG = "LoginActivity";
    private LoginIndexFragment indexFragment;
    private SignupInputEmailFragment inputEmailFragment;
    private SignupSetPWFragment setPWFragment;
    private SignupPickAvatarFragment pickAvatarFragment;
    private LoginFragment loginFragment;
    private ResetPasswordFragment resetPasswordFragment;
    private ResetPasswordSentFragment sentFragment;
    private SignupFindFriendFragment findFriendFragment;


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
        inputEmailFragment = new SignupInputEmailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, inputEmailFragment, inputEmailFragment.getClassName()).hide(inputEmailFragment).commit();
        setPWFragment = new SignupSetPWFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, setPWFragment, setPWFragment.getClassName()).hide(setPWFragment).commit();
        pickAvatarFragment = new SignupPickAvatarFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, pickAvatarFragment, pickAvatarFragment.getClassName()).hide(pickAvatarFragment).commit();
        loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, loginFragment, loginFragment.getClassName()).hide(loginFragment).commit();
        resetPasswordFragment = new ResetPasswordFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, resetPasswordFragment, resetPasswordFragment.getClassName()).hide(resetPasswordFragment).commit();
        sentFragment = new ResetPasswordSentFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, sentFragment, sentFragment.getClassName()).hide(sentFragment).commit();
        findFriendFragment = new SignupFindFriendFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.login_framelayout, findFriendFragment, findFriendFragment.getClassName()).hide(findFriendFragment).commit();
    }

    private void showIndexFragment(){
        getSupportFragmentManager().beginTransaction().show(indexFragment).commit();
    }


}
