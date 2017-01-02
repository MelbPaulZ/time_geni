package org.unimelb.itime.ui.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.LoginUser;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginBaseFragment extends BaseUiFragment<LoginUser,LoginMvpView, LoginPresenter> {
    protected SoftKeyboardStateUtil softKeyboardStateUtil;
    protected LoginViewModel loginViewModel;
    protected LoginUser loginUser;
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
        loginUser = new LoginUser();
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

    protected  void addFragmentToManager(LoginBaseFragment fragment){
        if (!fragment.isAdded()){
            getFragmentManager().beginTransaction().add(R.id.login_framelayout,fragment,fragment.getClassName());
        }
    }

    /** the set loginUser set loginUser to fragment and viewmodel,
     *  data from fragment and vimodel are same(from same address)
     *  @param loginUser
     * */
    public void setLoginUser(LoginUser loginUser){
        this.loginUser = loginUser;
        loginViewModel.setLoginUser(loginUser);
    }


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

    @Override
    public void setData(LoginUser loginUser) {

    }
}
