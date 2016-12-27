package org.unimelb.itime.ui.fragment.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginCommonFragment extends BaseUiFragment<LoginMvpView, LoginPresenter> {
    protected SoftKeyboardStateUtil softKeyboardStateUtil;
    protected LoginViewModel loginViewModel;
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = new LoginViewModel(getPresenter());
        softKeyboardStateUtil = new SoftKeyboardStateUtil(getView());
        bindSoftKeyboardEvent();
    }

    /**
     * check the state of soft keyboard
     */
    protected void bindSoftKeyboardEvent(){
        softKeyboardStateUtil.addSoftKeyboardStateListener(new SoftKeyboardStateUtil.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                loginViewModel.setTopIconVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardClosed() {
                loginViewModel.setTopIconVisibility(View.VISIBLE);
            }
        });
    }

    protected  void addFragmentToManager(LoginCommonFragment fragment){
        if (!fragment.isAdded()){
            getFragmentManager().beginTransaction().add(R.id.login_framelayout,fragment,fragment.getClassName());
        }
    }
}
