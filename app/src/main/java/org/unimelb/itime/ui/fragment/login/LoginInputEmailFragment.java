package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginInputEmailBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by yinchuandong on 15/12/16.
 */

public class LoginInputEmailFragment extends LoginBaseFragment implements LoginMvpView {

    private final static String TAG = "LoginIndexFragment";

    private FragmentLoginInputEmailBinding binding;

    private AlertDialog unsupportEmailDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input_email, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
//        loginViewModel.setLoginUser(loginUser); // this is for init the loginUser
    }


    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSucceed(int task) {
        onPageChange(task);
    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }


    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INDEX_FRAG:{
                closeFragment(this, (LoginIndexFragment)getFragmentManager().findFragmentByTag(LoginIndexFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_SET_PASSWORD_FRAG:{
                LoginSetPWFragment loginSetPWFragment = (LoginSetPWFragment)getFragmentManager().findFragmentByTag(LoginSetPWFragment.class.getSimpleName());
                loginSetPWFragment.setLoginUser(loginUser.getCopyLoginUser());
                openFragment(this, loginSetPWFragment);
                break;
            }
            case LoginViewModel.TO_TERM_AGREEMENT_FRAG:{
                // todo implement agreement
            }
        }
    }

    @Override
    public void showErrorDialog(ValidateRes res) {
        showDialog(res.getTitle(), res.getContent());
    }
}
