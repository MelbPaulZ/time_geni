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
import org.unimelb.itime.databinding.FragmentSignupSetPasswordBinding;
import org.unimelb.itime.restfulresponse.ValidateRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;

/**
 * Created by Paul on 19/12/2016.
 */

public class SignupSetPWFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentSignupSetPasswordBinding binding;
    private Dialog pwTooSimpleDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup_set_password,container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setLoginVM(loginViewModel);
        initViews();
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



    @Override
    public void onLoginStart() {

    }

    @Override
    public void onLoginSucceed(int task) {
        onPageChange(task);
    }

    @Override
    public void onLoginFail(int task, String errorMsg) {

    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_INPUT_EMAIL_FRAG:{
                closeFragment(this, (SignupInputEmailFragment)getFragmentManager().findFragmentByTag(SignupInputEmailFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_PICK_AVATAR_FRAG:{
                SignupPickAvatarFragment avatarFragment = (SignupPickAvatarFragment)getFragmentManager().findFragmentByTag(SignupPickAvatarFragment.class.getSimpleName());
                avatarFragment.setLoginUser(loginUser.getCopyLoginUser());
                openFragment(this, avatarFragment);
                break;
            }
        }
    }

    @Override
    public void showErrorDialog(ValidateRes res) {
        showDialog(res.getTitle(), res.getContent());
    }
}
