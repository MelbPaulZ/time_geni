package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginInputEmailBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by yinchuandong on 15/12/16.
 */

public class LoginInputEmailFragment extends MvpFragment<LoginMvpView, LoginPresenter> implements LoginMvpView {

    private final static String TAG = "LoginIndexFragment";

    private FragmentLoginInputEmailBinding binding;
    private LoginViewModel loginViewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input_email, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = new LoginViewModel(getPresenter());
        binding.setLoginVM(loginViewModel);
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getContext());
    }

    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSucceed() {

    }

    @Override
    public void onLoginFail(int errorCode, int errorMsg) {

    }
}
