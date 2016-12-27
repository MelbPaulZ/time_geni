package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentPickAvatarBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

/**
 * Created by Paul on 19/12/2016.
 */

public class LoginPickAvatarFragment extends LoginCommonFragment implements LoginMvpView{

    private FragmentPickAvatarBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick_avatar, container, false);
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
}
