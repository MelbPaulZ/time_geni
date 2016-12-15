package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    private int topEmailIconVisibility = View.VISIBLE;

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

    @Bindable
    public int getTopEmailIconVisibility(){
        return this.topEmailIconVisibility;
    }

    public void setTopEmailIconVisibility(int visibility){
        this.topEmailIconVisibility = visibility;
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


    // step 1, index btn sign in click
    public View.OnClickListener onIndexBtnSignInClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view.setSelected(true);
                Log.d(TAG, "OnIndexBtnSignInClick: ");
            }
        };
    }

    public View.OnFocusChangeListener onInputEmailEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if(hasFocus){
                    showKeyBoard((EditText) view);
                }else{
                    closeKeyBoard((EditText) view);
                }
            }
        };
    }

    @BindingAdapter("android:onFocusChange")
    public static void setOnFocusChangeListener(View view, View.OnFocusChangeListener onFocusChangeListener){
        view.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void showKeyBoard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void closeKeyBoard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
