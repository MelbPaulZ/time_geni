package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;

import java.util.ArrayList;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginViewModel extends AndroidViewModel{

    private static final String TAG = "LoginViewModel";
    LoginPresenter presenter;

    private String email = "johncdyin@gmail.com";
    private String password = "123456";
    private LoginMvpView mvpView;

    private String newPassword = "";
    private int passwordKeyBoardVisibility = View.VISIBLE;

    private int topEmailIconVisibility = View.VISIBLE;

    private ArrayList<String> suggestedEmailList = new ArrayList<>();
    private ItemView suggestedEmailItemView = ItemView.of(BR.itemText, R.layout.listview_login_email_tips);

    public LoginViewModel(LoginPresenter presenter){
        this.presenter = presenter;
        mvpView = presenter.getView();
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
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
                //view.setSelected(true);
                if (isEmailValid()){

                }else{
                    mvpView.invalidPopup();
                }
                Log.d(TAG, "OnIndexBtnSignInClick: ");
            }
        };
    }

    // todo implement regix
    private boolean isEmailValid(){
        return false;
    }

    /**
     * check the focus of email edit text
     * @return
     */
    public View.OnFocusChangeListener onInputEmailEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if(hasFocus){
                    showKeyBoard((EditText) view);
                    setTopEmailIconVisibility(View.GONE);
                }else{
                    closeKeyBoard((EditText) view);
                    setTopEmailIconVisibility(View.VISIBLE);
                }
            }
        };
    }

    public View.OnFocusChangeListener onPasswordEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "onFocusChange: " + hasFocus);
                if(hasFocus){
                    showKeyBoard((EditText) v);
                }else{
                    closeKeyBoard((EditText) v);
                }
            }
        };
    }

    public View.OnClickListener onPasswordNextClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPassword.length()>=8){

                }else {
                    mvpView.invalidPopup();
                }
            }
        };
    }

    public View.OnClickListener onClickAvatar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        };
    }

    public void onPasswordChange(CharSequence s, int start, int before, int count) {
        Log.w("tag", "onTextChanged " + s);
    }

    public AdapterView.OnItemClickListener onSuggestedEmailItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setEmail(suggestedEmailList.get(i));
            }
        };
    }

    private void showKeyBoard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void closeKeyBoard(EditText view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        notifyPropertyChanged(BR.topEmailIconVisibility);
    }

    @Bindable
    public ArrayList<String> getSuggestedEmailList(){
        return this.suggestedEmailList;
    }

    public void setSuggestedEmailList(ArrayList<String> suggestedEmailList){
        this.suggestedEmailList = suggestedEmailList;
        notifyPropertyChanged(BR.suggestedEmailList);
    }

    @Bindable
    public ItemView getSuggestedEmailItemView(){
        return this.suggestedEmailItemView;
    }

    public void setSuggestedEmailItemView(ItemView suggestedEmailItemView){
        this.suggestedEmailItemView = suggestedEmailItemView;
        notifyPropertyChanged(BR.suggestedEmailItemView);
    }


    @Bindable
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        notifyPropertyChanged(BR.newPassword);
    }

    public int getPasswordKeyBoardVisibility() {
        return passwordKeyBoardVisibility;
    }

    public void setPasswordKeyBoardVisibility(int passwordKeyBoardVisibility) {
        this.passwordKeyBoardVisibility = passwordKeyBoardVisibility;
    }
}
