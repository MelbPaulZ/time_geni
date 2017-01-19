package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.SoftKeyboardStateUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginBaseFragment extends BaseUiAuthFragment<LoginMvpView, LoginPresenter> {
    protected SoftKeyboardStateUtil softKeyboardStateUtil;
    protected LoginViewModel loginViewModel;
    protected User loginUser;
    protected AlertDialog dialog;

    public LoginBaseFragment(){
        this.loginUser = new User();
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String synToken = AuthUtil.getJwtToken(getContext());
        // this use to create DB manager...
        DBManager.getInstance(getContext());
        EventManager.getInstance(getContext());
        if (!synToken.equals("")){
            successLogin();
            return;
        }

        loginViewModel = new LoginViewModel(getPresenter());
        softKeyboardStateUtil = new SoftKeyboardStateUtil(getView());
        bindSoftKeyboardEvent();
        loginViewModel.setLoginUser(loginUser); // maintain consistency of viewmodel and fragment
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

    public void setData(User loginUser){
        this.loginUser = loginUser;
    }


    /**
     * called if successfully login
     */
    public void successLogin(){
        PushService.setDefaultPushCallback(getActivity().getApplication(), MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                } else {

                }
                String userUid = UserUtil.getInstance(getContext()).getUserUid();

                AVInstallation.getCurrentInstallation().put("user_uid", userUid);
            }
        });
        // start service and go to main activity
        Intent intent = new Intent(getActivity(), RemoteService.class);
        getActivity().startService(intent);
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
