package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentResetPasswordBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 20/12/2016.
 */

public class ResetPasswordFragment extends LoginBaseFragment implements LoginMvpView {
    private static final String TAG = "LoginResetPWFrag";
    private FragmentResetPasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false);
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
            case LoginViewModel.TO_LOGIN_FRAG:{
                getFragmentManager().popBackStack();
                break;
            }
            case LoginViewModel.TO_EMAIL_SENT_FRAG:{
                ResetPasswordFragment fragment = new ResetPasswordFragment();
                getBaseActivity().openFragment(fragment);
                break;
            }
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                SignupInputEmailFragment fragment = new SignupInputEmailFragment();
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
        if(taskId == LoginPresenter.TASK_VALIDATE){
            ValidateRes res = (ValidateRes) data;
            showDialog(res.getTitle(), res.getContent());
        }
    }
}
