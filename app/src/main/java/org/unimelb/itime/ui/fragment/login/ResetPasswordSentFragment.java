package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentResetPasswordSentBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 20/12/2016.
 */

public class ResetPasswordSentFragment extends LoginBaseFragment implements LoginMvpView {

    private FragmentResetPasswordSentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password_sent, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
    }

    @Override
    public void onPageChange(int task) {
        switch(task){
            case LoginViewModel.TO_INDEX_FRAG: {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                LoginIndexFragment fragment = new LoginIndexFragment();
                getBaseActivity().openFragment(fragment);
                break;
            }
            case LoginViewModel.TO_RESET_PASSWORD_FRAG:{
                getFragmentManager().popBackStack();
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
