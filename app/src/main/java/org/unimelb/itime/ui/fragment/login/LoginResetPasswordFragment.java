package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginResetPasswordBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

import static org.unimelb.itime.R.string.login;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginResetPasswordFragment extends LoginCommonFragment implements LoginMvpView {
    private static final String TAG = "LoginResetPWFrag";
    private FragmentLoginResetPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_reset_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
    }

    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSucceed(int task) {
        if (task == LoginViewModel.TO_EMAIL_SENT_FRAG){
            onPageChange(task);
        }
    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }


    @Override
    public void invalidPopup() {

    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_LOGIN_FRAG:{
                closeFragment(this, (LoginFragment)getFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_EMAIL_SENT_FRAG:{
                openFragment(this, (LoginEmailSentFragment)getFragmentManager().findFragmentByTag(LoginEmailSentFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                openFragment(this, (LoginInputEmailFragment)getFragmentManager().findFragmentByTag(LoginInputEmailFragment.class.getSimpleName()));
                break;
            }
        }
    }
}
