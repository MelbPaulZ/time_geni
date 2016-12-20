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

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.FragmentLoginNewBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 20/12/2016.
 */

public class LoginFragmentNew extends LoginCommonFragment implements LoginMvpView {
    private FragmentLoginNewBinding binding;
    private AlertDialog loginFailDialog;

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
    public void switchFragment(int task) {
        switch (task){
            case LoginViewModel.TO_INDEX_FRAG:{
                switchFragment(this, (LoginIndexFragment)getFragmentManager().findFragmentByTag(LoginIndexFragment.class.getSimpleName()));
            }
            case LoginViewModel.TO_RESET_PASSWORD_FRAG:{
                switchFragment(this, (LoginResetPasswordFragment)getFragmentManager().findFragmentByTag(LoginResetPasswordFragment.class.getSimpleName()));
            }
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                switchFragment(this, (LoginInputEmailFragment)getFragmentManager().findFragmentByTag(LoginInputEmailFragment.class.getSimpleName()));
            }
        }
    }
}
