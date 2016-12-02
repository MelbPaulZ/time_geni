package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.ui.presenter.LoginPresenter;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginViewModel extends BaseObservable{

    private static final String TAG = "LoginViewModel";
    LoginPresenter presenter;

//    private String email = "johncdyin@gmail.com";
    private String email = "johncdyin@gmail.com";
    private String password = "123456";

    public LoginViewModel(LoginPresenter presenter){
        this.presenter = presenter;

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public View.OnClickListener onBtnEmailLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onEmailLoginClick: " + getEmail() +
                        "/" + getPassword());
                presenter.loginByEmail(getEmail(), getPassword());
            }
        };
    }


    public View.OnClickListener onBtnRefreshToken(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.refreshToken();
            }
        };
    }


    public View.OnClickListener onBtnListUser(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                presenter.listUser();
                presenter.testList();
            }
        };
    }
}
