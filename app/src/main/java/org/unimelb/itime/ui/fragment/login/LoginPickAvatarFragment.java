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
import org.unimelb.itime.databinding.FragmentLoginPickAvatarBinding;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.SoftKeyboardStateUtil;

import static android.R.id.empty;
import static org.unimelb.itime.R.id.dialog;

/**
 * Created by Paul on 19/12/2016.
 */

public class LoginPickAvatarFragment extends LoginBaseFragment implements LoginMvpView{

    private FragmentLoginPickAvatarBinding binding;

    private Dialog emptyInvalidationDialog, specialCharactersDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_pick_avatar, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        softKeyboardStateUtil = new SoftKeyboardStateUtil(binding.getRoot());
        binding.setLoginVM(loginViewModel);
        initViews();
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

    private void initViews(){
        String emptyMessage = "Your name should contain at least one letter.";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(emptyMessage)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        emptyInvalidationDialog = builder.create();


        String specialCharactersMsg = "Only letters and spaces are allowed in your name.";
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext())
                .setMessage(specialCharactersMsg)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        specialCharactersDialog = builder1.create();
    }


    @Override
    public void invalidPopup(int reason) {
        if (reason == LoginViewModel.INVALID_ALIAS_EMPTY){
            emptyInvalidationDialog.show();
        }else if (reason == LoginViewModel.INVALID_ALIAS_SPECIAL_SINGAL){
            specialCharactersDialog.show();
        }
    }

    @Override
    public void onPageChange(int task) {
        switch (task){
            case LoginViewModel.TO_SET_PASSWORD_FRAG:{
                closeFragment(this, (LoginSetPWFragment)getFragmentManager().findFragmentByTag(LoginSetPWFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_FIND_FRIEND_FRAG:{
                openFragment(this, (LoginFindFriendFragment)getFragmentManager().findFragmentByTag(LoginFindFriendFragment.class.getSimpleName()));
                break;
            }
            case LoginViewModel.TO_TERM_AGREEMENT_FRAG:{
                // todo implement
            }
        }
    }


}
