package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginNewBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.UserUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginFragmentNew extends LoginCommonFragment implements LoginMvpView {
    private FragmentLoginNewBinding binding;
    private AlertDialog loginFailDialog;
    private final static String TAG = "LoginFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_new, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
        initViews();

        String synToken = AuthUtil.getJwtToken(getContext());
        // this use to create DB manager...
        DBManager.getInstance(getContext());
        EventManager.getInstance(getContext());
        if (!synToken.equals("")){
            onLoginSucceed();
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

    /** init unsupported warning dialog
     */
    private void initViews(){
        TextView title = new TextView(getContext());
        title.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        title.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        title.setText(getString(R.string.login_fail_alert_title));
        title.setTextSize(18);
        title.setPadding(0,50,0,0);
        title.setTextColor(getResources().getColor(R.color.black));
        String message = getString(R.string.login_fail_alert_msg);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.login_fail_alert_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        loginFailDialog = builder.create();
    }

    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSucceed() {
        PushService.setDefaultPushCallback(getActivity().getApplication(), MainActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    Log.d(TAG, "done: " + installationId);
                } else {

                }
                String userUid = UserUtil.getInstance(getContext()).getUserUid();

                AVInstallation.getCurrentInstallation().put("user_uid", userUid);
            }
        });
        // start service and go to main activity
        long start = System.currentTimeMillis();
        Intent intent = new Intent(getActivity(), RemoteService.class);
        getActivity().startService(intent);
//        Toast.makeText(getContext(), "signin success", Toast.LENGTH_SHORT).show();
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
        Log.i(TAG, "onLoginSucceed: " + (System.currentTimeMillis() - start));
    }

    @Override
    public void onLoginFail(int errorCode, int errorMsg) {

    }

    @Override
    public void invalidPopup() {
        loginFailDialog.show();
        TextView msg = (TextView) loginFailDialog.findViewById(android.R.id.message);
        msg.setGravity(Gravity.CENTER);
        msg.setTextSize(13);
    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INDEX_FRAG:{
                closeFragment(this, (LoginIndexFragment)getFragmentManager().findFragmentByTag(LoginIndexFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_RESET_PASSWORD_FRAG:{
                openFragment(this, (LoginResetPasswordFragment)getFragmentManager().findFragmentByTag(LoginResetPasswordFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                openFragment(this, (LoginInputEmailFragment)getFragmentManager().findFragmentByTag(LoginInputEmailFragment.class.getSimpleName()));
                break;
            }
        }
    }
}
