package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

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
    private LoginPresenter presenter;

    private String email = "johncdyin@gmail.com";
    private String password = "123456";
    private LoginMvpView mvpView;

    private int topIconVisibility = View.VISIBLE;

    private ArrayList<String> suggestedEmailList = new ArrayList<>();
    private ItemView suggestedEmailItemView = ItemView.of(BR.itemText, R.layout.listview_login_email_tips);

    private String inputName = "";

    public final static int TO_EMAIL_SENT_FRAG = 1;
    public final static int TO_FIND_FRIEND_FRAG = 2;
    public final static int TO_LOGIN_FRAG = 3;
    public final static int TO_INDEX_FRAG = 4;
    public final static int TO_INPUT_EMAIL_FRAG = 5;
    public final static int TO_PICK_AVATAR_FRAG = 6;
    public final static int TO_RESET_PASSWORD_FRAG = 7;
    public final static int TO_SET_PASSWORD_FRAG = 8;
    public final static int TO_TERM_AGREEMENT_FRAG = 9;


    public LoginViewModel(LoginPresenter presenter){
        this.presenter = presenter;
        mvpView = presenter.getView();
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
    }

    private Context getContext(){
        return presenter.getContext();
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

    public View.OnClickListener onSwitchFragment(final int task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.onPageChange(task);
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

    public View.OnClickListener onClickInputEmailBtn(final int task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid()){
                    mvpView.onPageChange(task);
                }else{
                    mvpView.invalidPopup();
                }
            }
        };
    }


    // todo implement regix
    private boolean isEmailValid(){
        return true;
    }

    /**
     * check the focus of email edit text
     * @return
     */
    public View.OnFocusChangeListener onInputEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                if(hasFocus){
                    showKeyBoard((EditText) view);
                    setTopIconVisibility(View.GONE);
                }else{
                    closeKeyBoard((EditText) view);
                    setTopIconVisibility(View.VISIBLE);
                }
            }
        };
    }

    public View.OnClickListener onPasswordNextClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length()>=8){
                    mvpView.onPageChange(TO_PICK_AVATAR_FRAG);
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



    public AdapterView.OnItemClickListener onSuggestedEmailItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setEmail(suggestedEmailList.get(i));
            }
        };
    }

    public View.OnClickListener addFromContact(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add from contact", Toast.LENGTH_SHORT).show();
            }
        };
    }


    public View.OnClickListener addFromGmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add from Gmail", Toast.LENGTH_SHORT).show();

            }
        };
    }


    public View.OnClickListener onClickLogin(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginByEmail(getEmail(), getPassword());
                Toast.makeText(getContext(), "logging in", Toast.LENGTH_SHORT).show();
//                mvpView.invalidPopup();
            }
        };
    }


    public View.OnClickListener onCleanEmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(presenter.getContext(), "onClick X", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public View.OnClickListener onClickResetCleanEmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEmail("");
            }
        };
    }



    public View.OnClickListener toCalendar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "tocalendar here", Toast.LENGTH_SHORT).show();
            }
        };
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
    public int getTopIconVisibility(){
        return this.topIconVisibility;
    }

    public void setTopIconVisibility(int visibility){
        this.topIconVisibility = visibility;
        notifyPropertyChanged(BR.topIconVisibility);
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
    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
        notifyPropertyChanged(BR.inputName);
    }
}
