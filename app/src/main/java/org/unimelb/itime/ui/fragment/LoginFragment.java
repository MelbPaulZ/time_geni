package org.unimelb.itime.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginBinding;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.UserUtil;

import static org.unimelb.itime.util.UserUtil.getUserUid;


/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends MvpFragment<LoginMvpView, LoginPresenter> implements LoginMvpView{
    private final static String TAG = "LoginFragment";

    private FragmentLoginBinding binding;
    private LoginViewModel loginVM;


    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String synToken = AuthUtil.getJwtToken(getContext());
        if (!synToken.equals("")){
            onLoginSucceed();
        }else {
            loginVM = new LoginViewModel(getPresenter());
            binding.setLoginVM(loginVM);
        }
    }

    @Override
    public void onLoginStart() {
        Toast.makeText(getContext(), "signing in start", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoginSucceed() {

        PushService.setDefaultPushCallback(getActivity().getApplication(), MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    Log.d(TAG, "done: " + installationId);
                    String userUid = UserUtil.getUserLoginRes()==null? UserUtil.getUserIdFromPreference(getContext()) : UserUtil.getUserUid();
                    AVInstallation.getCurrentInstallation().put("user_uid", userUid);
                } else {

                }
            }
        });
        // start service and go to main activity
        Intent intent = new Intent(getActivity(), RemoteService.class);
        getActivity().startService(intent);
//        Toast.makeText(getContext(), "signin success", Toast.LENGTH_SHORT).show();
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }

    @Override
    public void onLoginFail(int errorCode, int errorMsg) {
        Toast.makeText(getContext(), "signin failure", Toast.LENGTH_SHORT).show();
    }
}
