package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSignupPickAvatarBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

/**
 * Created by Paul on 19/12/2016.
 */

public class SignupPickAvatarFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentSignupPickAvatarBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_pick_avatar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        softKeyboardStateUtil = new SoftKeyboardStateUtil(binding.getRoot());
        binding.setLoginVM(loginViewModel);
    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_SET_PASSWORD_FRAG:{
                getFragmentManager().popBackStack();
                break;
            }
            case LoginViewModel.TO_FIND_FRIEND_FRAG:{
                SignupFindFriendFragment friendFragment = new SignupFindFriendFragment();
                getBaseActivity().openFragment(friendFragment);
                break;
            }
            case LoginViewModel.TO_TERM_AGREEMENT_FRAG:{
                // todo implement
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
        if(taskId == LoginPresenter.TASK_VALIDATE){
            ValidateRes res = (ValidateRes)data;
            showDialog(res.getTitle(), res.getContent());
        }
    }


}