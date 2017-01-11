package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.app.Dialog;
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
import org.unimelb.itime.databinding.FragmentLoginPickAvatarBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

import static android.R.id.empty;
import static org.unimelb.itime.R.id.dialog;

/**
 * Created by Paul on 19/12/2016.
 */

public class LoginPickAvatarFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentLoginPickAvatarBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_pick_avatar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        softKeyboardStateUtil = new SoftKeyboardStateUtil(binding.getRoot());
        binding.setLoginVM(loginViewModel);
    }

    @Override
    public void onLoginStart() {
        AppUtil.showProgressBar(getContext(), "Signing Up", "Waiting");
    }

    @Override
    public void onLoginSucceed(int task) {
        if (task == LoginViewModel.SIGN_UP){
            loginViewModel.signup();
        }else {
            AppUtil.hideProgressBar();
            onPageChange(task);
        }
    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }



    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_SET_PASSWORD_FRAG:{
                closeFragment(this, (LoginSetPWFragment)getFragmentManager().findFragmentByTag(LoginSetPWFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_FIND_FRIEND_FRAG:{
                openFragment(this, (LoginFindFriendFragment)getFragmentManager().findFragmentByTag(LoginFindFriendFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_TERM_AGREEMENT_FRAG:{
                // todo implement
            }
        }
    }

    @Override
    public void showErrorDialog(ValidateRes res) {
        showDialog(res.getTitle(), res.getContent());
    }


}
