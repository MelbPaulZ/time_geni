package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginIndexBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by yinchuandong on 15/12/16.
 */

public class LoginIndexFragment extends LoginCommonFragment implements LoginMvpView {

    private final static String TAG = "LoginIndexFragment";

    private FragmentLoginIndexBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_index, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
    }


    public void signUpClick(View v){

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

    @Override
    public void invalidPopup() {

    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                openFragment(this, (LoginInputEmailFragment)getFragmentManager().findFragmentByTag(LoginInputEmailFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_LOGIN_FRAG:{
                openFragment(this, (LoginFragment)getFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName()));
                break;
            }
        }
    }

}
