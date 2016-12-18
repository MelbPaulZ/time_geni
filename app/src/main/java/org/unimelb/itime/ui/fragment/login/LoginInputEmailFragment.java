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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.databinding.FragmentLoginInputEmailBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

import static android.os.Build.VERSION_CODES.M;
import static org.unimelb.itime.R.id.dialog;
import static org.unimelb.itime.vendor.contact.widgets.SideBar.b;

/**
 * Created by yinchuandong on 15/12/16.
 */

public class LoginInputEmailFragment extends MvpFragment<LoginMvpView, LoginPresenter> implements LoginMvpView {

    private final static String TAG = "LoginIndexFragment";

    private FragmentLoginInputEmailBinding binding;
    private LoginViewModel loginViewModel;

    private SoftKeyboardStateUtil softKeyboardStateUtil;
    private AlertDialog unsupportEmailDialog, incorrectPWDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_input_email, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel = new LoginViewModel(getPresenter());
        binding.setLoginVM(loginViewModel);
        softKeyboardStateUtil = new SoftKeyboardStateUtil(binding.getRoot());
        bindSoftKeyboardEvent();
        initViews();
    }

    /**
     * check the state of soft keyboard
     */
    private void bindSoftKeyboardEvent(){
        softKeyboardStateUtil.addSoftKeyboardStateListener(new SoftKeyboardStateUtil.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                loginViewModel.setTopEmailIconVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardClosed() {
                loginViewModel.setTopEmailIconVisibility(View.VISIBLE);
            }
        });
    }

    /** init unsupported warning dialog
     */
    private void initViews(){
        TextView unsupportedEmailTitle = new TextView(getContext());
        unsupportedEmailTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        unsupportedEmailTitle.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        unsupportedEmailTitle.setText(getString(R.string.unsupported_email));
        unsupportedEmailTitle.setTextSize(18);
        unsupportedEmailTitle.setPadding(0,50,0,0);
        unsupportedEmailTitle.setTextColor(getResources().getColor(R.color.black));
        String message = "iTime doesn't support your university at the moment, but a wider support is coming. Thank you for your patient.";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(unsupportedEmailTitle)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        unsupportEmailDialog = builder.create();

        TextView incorrectPasswordTitle = new TextView(getContext());
        incorrectPasswordTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        incorrectPasswordTitle.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
        incorrectPasswordTitle.setText(getString(R.string.incorrect_password));
        incorrectPasswordTitle.setTextSize(18);
        incorrectPasswordTitle.setPadding(0,50,0,0);
        incorrectPasswordTitle.setTextColor(getResources().getColor(R.color.black));
        String incorrectPWmsg = "The password you entered is incorrect. Please try again";
        AlertDialog.Builder incorrectPWBuilder = new AlertDialog.Builder(getContext())
                .setCustomTitle(incorrectPasswordTitle)
                .setMessage(incorrectPWmsg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        incorrectPWDialog = incorrectPWBuilder.create();


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
    public void invalidEmail() {
        unsupportEmailDialog.show();
    }
}
