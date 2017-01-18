package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSignupSetPasswordBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 19/12/2016.
 */

public class SignupSetPWFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentSignupSetPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_set_password,container, false);
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
                getFragmentManager().popBackStack();
                break;
            }
            case LoginViewModel.TO_PICK_AVATAR_FRAG:{
                SignupPickAvatarFragment avatarFragment = new SignupPickAvatarFragment();
                avatarFragment.setData(loginUser);
                getBaseActivity().openFragment(avatarFragment);
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
        if(taskId == LoginPresenter.TASK_VALIDATE){
            ValidateRes res = (ValidateRes)data;
            showDialog(res.getTitle(), res.getContent());
        }
    }
}
