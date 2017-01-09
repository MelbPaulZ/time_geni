package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginFindFriendBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginFindFriendFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentLoginFindFriendBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_find_friend, container, false);
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
        if (task == LoginViewModel.TO_CALENDAR){
            successLogin();
        }

    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }


    @Override
    public void onPageChange(int task) {

    }

    @Override
    public void showErrorDialog(ValidateRes res) {

    }
}
