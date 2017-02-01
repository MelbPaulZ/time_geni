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

public class LoginIndexFragment extends LoginBaseFragment implements LoginMvpView {

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

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                SignupInputEmailFragment fragment = new SignupInputEmailFragment();
                getBaseActivity().openFragment(fragment);
                break;
            }
            case LoginViewModel.TO_LOGIN_FRAG:{
                LoginFragment fragment = new LoginFragment();
                getBaseActivity().openFragment(fragment);
                break;
            }
        }
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
