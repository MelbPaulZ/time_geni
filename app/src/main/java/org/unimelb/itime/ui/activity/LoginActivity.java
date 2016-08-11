package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ActivityLoginBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends MvpActivity<LoginMvpView, LoginPresenter> implements LoginMvpView{

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private LoginViewModel loginVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginVM = new LoginViewModel(getPresenter());
        binding.setLoginVM(loginVM);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }


    @Override
    public void onLoginStart() {
        Log.d(TAG, "onLoginStart: ");
    }

    @Override
    public void onLoginSucceed() {
        Log.d(TAG, "onLoginSucceed: ");
    }

    @Override
    public void onLoginFail(int errorCode, int errorMsg) {
        Log.d(TAG, "onLoginFail: " + errorCode + "--" + errorMsg);
    }


}

