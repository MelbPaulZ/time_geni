package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginFragment extends LoginBaseFragment implements LoginMvpView {
    private FragmentLoginBinding binding;
    private AlertDialog loginFailDialog;
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
        binding.setLoginVM(loginViewModel);
        initViews();
        loginViewModel.setLoginUser(loginUser);

        String synToken = AuthUtil.getJwtToken(getContext());
        // this use to create DB manager...
        DBManager.getInstance(getContext());
        EventManager.getInstance(getContext());
        if (!synToken.equals("")){
            onLoginSucceed(LoginViewModel.TO_CALENDAR);
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
    public void onLoginSucceed(int task) {
        if (task == LoginViewModel.TO_CALENDAR) {
            successLogin();
        }
    }



    @Override
    public void onLoginFail(int task, String msg) {

    }

    @Override
    public void invalidPopup(int reason) {
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
