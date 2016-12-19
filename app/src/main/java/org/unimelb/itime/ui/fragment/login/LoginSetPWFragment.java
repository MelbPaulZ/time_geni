package org.unimelb.itime.ui.fragment.login;

import android.app.AlertDialog;
import android.app.Dialog;
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
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.databinding.FragmentLoginInputPasswordBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

/**
 * Created by Paul on 19/12/2016.
 */

public class LoginSetPWFragment extends BaseUiFragment<LoginMvpView, LoginPresenter> implements LoginMvpView{

    private FragmentLoginInputPasswordBinding binding;
    private LoginViewModel viewModel;
    private Dialog pwTooSimpleDialog;
    private SoftKeyboardStateUtil softKeyboardStateUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input_password,container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new LoginViewModel(getPresenter());
        binding.setVm(viewModel);
        softKeyboardStateUtil = new SoftKeyboardStateUtil(binding.getRoot());
        initViews();
        bindSoftKeyboardEvent();
    }

    private void initViews(){
        TextView incorrectPasswordTitle = new TextView(getContext());
        incorrectPasswordTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        incorrectPasswordTitle.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        incorrectPasswordTitle.setText(getString(R.string.password_too_simple));
        incorrectPasswordTitle.setTextSize(18);
        incorrectPasswordTitle.setPadding(0,50,0,0);
        incorrectPasswordTitle.setTextColor(getResources().getColor(R.color.black));
        String incorrectPWmsg = "Please choose a password with a minimum of 8 characters or numbers.";
        AlertDialog.Builder incorrectPWBuilder = new AlertDialog.Builder(getContext())
                .setCustomTitle(incorrectPasswordTitle)
                .setMessage(incorrectPWmsg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        pwTooSimpleDialog = incorrectPWBuilder.create();
    }

    private void bindSoftKeyboardEvent(){
        softKeyboardStateUtil.addSoftKeyboardStateListener(new SoftKeyboardStateUtil.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {

            }

            @Override
            public void onSoftKeyboardClosed() {

            }
        });
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(getContext());
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
        pwTooSimpleDialog.show();
//        TextView incorrectPWTV = (TextView) pwTooSimpleDialog.findViewById(android.R.id.message);
//        incorrectPWTV.setGravity(Gravity.CENTER);
    }
}
