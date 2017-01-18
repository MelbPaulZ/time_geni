package org.unimelb.itime.ui.fragment.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginFragment extends LoginBaseFragment implements LoginMvpView {
    private FragmentLoginBinding binding;
    private final static String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginUser.setUserId("johncdyin@gmail.com");
        loginUser.setPassword("123456");
        binding.setLoginVM(loginViewModel);

        String synToken = AuthUtil.getJwtToken(getContext());
        // this use to create DB manager...
        DBManager.getInstance(getContext());
        EventManager.getInstance(getContext());
        if (!synToken.equals("")){
            onTaskSuccess(LoginPresenter.TASK_LOGIN, null);
        }else {
            loadData();
        }
    }

    private void loadData(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                EventManager.getInstance(getContext()).loadDB();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
            }
        }.start();
    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INDEX_FRAG:{
                getFragmentManager().popBackStack();
                break;
            }
            case LoginViewModel.TO_RESET_PASSWORD_FRAG:{
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
        if(taskId == LoginPresenter.TASK_LOGIN){
            successLogin();
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }

}
