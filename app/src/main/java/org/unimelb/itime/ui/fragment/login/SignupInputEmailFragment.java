package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentSignupInputEmailBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by yinchuandong on 15/12/16.
 */

public class SignupInputEmailFragment extends LoginBaseFragment implements LoginMvpView {

    private final static String TAG = "LoginIndexFragment";

    private FragmentSignupInputEmailBinding binding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_input_email, container, false);
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
            case LoginViewModel.TO_INDEX_FRAG:{
                getFragmentManager().popBackStack();
                break;
            }
            case LoginViewModel.TO_TERM_AGREEMENT_FRAG:{
                // todo implement agreement
            }
        }
    }

    @Override
    public void onTaskStart(int taskId) {
        showProgressDialog();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        hideProgressDialog();
        SignupSetPWFragment fragment = new SignupSetPWFragment();
        fragment.setData(loginUser);
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        hideProgressDialog();
        if(taskId == LoginPresenter.TASK_VALIDATE){
            ValidateRes res = (ValidateRes)data;
            showDialog(res.getTitle(), res.getContent());
        }
    }

}
