package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginEmailSentBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginEmailSentFragment extends LoginBaseFragment implements LoginMvpView {

    private FragmentLoginEmailSentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_email_sent, container, false);
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

    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }


    @Override
    public void invalidPopup(int reason) {

    }

    @Override
    public void onPageChange(int task) {
        switch(task){
            case LoginViewModel.TO_INDEX_FRAG: {
                closeFragment(this, (LoginIndexFragment) getFragmentManager().findFragmentByTag(LoginIndexFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_RESET_PASSWORD_FRAG:{
                closeFragment(this, (LoginResetPasswordFragment)getFragmentManager().findFragmentByTag(LoginResetPasswordFragment.class.getSimpleName()));
                break;
            }
        }
    }
}
