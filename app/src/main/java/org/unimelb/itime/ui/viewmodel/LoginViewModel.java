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
import org.unimelb.itime.bean.LoginUser;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import me.tatarka.bindingcollectionadapter.ItemView;

import static org.unimelb.itime.vendor.contact.widgets.SideBar.b;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginViewModel extends AndroidViewModel{

    private static final String TAG = "LoginViewModel";
    private LoginPresenter presenter;


    private LoginMvpView mvpView;

    private int topIconVisibility = View.VISIBLE;

    private ArrayList<String> suggestedEmailList = new ArrayList<>();
    private ItemView suggestedEmailItemView = ItemView.of(BR.itemText, R.layout.listview_login_email_tips);


    private LoginUser loginUser;

    public final static int TO_EMAIL_SENT_FRAG = 1;
    public final static int TO_FIND_FRIEND_FRAG = 2;
    public final static int TO_LOGIN_FRAG = 3;
    public final static int TO_INDEX_FRAG = 4;
    public final static int TO_INPUT_EMAIL_FRAG = 5;
    public final static int TO_PICK_AVATAR_FRAG = 6;
    public final static int TO_RESET_PASSWORD_FRAG = 7;
    public final static int TO_SET_PASSWORD_FRAG = 8;
    public final static int TO_TERM_AGREEMENT_FRAG = 9;
    public final static int TO_CALENDAR = 10;

    public final static int INVALID_EMAIL_ALREADY_REGISTER = 0;
    public final static int INVALID_UNSUPPORTED_EMAIL = 1;
    public final static int INVALID_PASSWORD_TOO_SIMPLE = 2;
    public final static int INVALID_PASSWORD_TOO_LONG = 3;
    public final static int INVALID_ALIAS_EMPTY =4;
    public final static int INVALID_ALIAS_SPECIAL_SINGAL = 5;


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

//    public View.OnClickListener onBtnEmailLogin(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "onEmailLoginClick: " + loginUser.getEmail() +
//                        "/" + loginUser.getPassword());
//                presenter.loginByEmail(loginUser.getEmail(), loginUser.getPassword());
//            }
//        };
//    }

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

                switch (task){
                    case TO_EMAIL_SENT_FRAG:
                        presenter.sendResetLink(task, loginUser.getEmail());
                        break;
                    case TO_FIND_FRIEND_FRAG:
                        if (isAliasValidation()){
                            signUp(task);
                        }
                        break;
                    default:
                        mvpView.onPageChange(task);
                }
            }
        };
    }

    private boolean isAliasValidation(){
        if (loginUser.getPersonalAlias().isEmpty()){
            mvpView.invalidPopup(INVALID_ALIAS_EMPTY);
            return false;
        }else if (!isAlphaNumeric(loginUser.getPersonalAlias())){
            mvpView.invalidPopup(INVALID_ALIAS_SPECIAL_SINGAL);
            return false;
        }else{
            return true;
        }
    }

    private boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
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
                    mvpView.invalidPopup(INVALID_UNSUPPORTED_EMAIL);
                }
            }
        };
    }

    private void signUp(int task){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", loginUser.getEmail()); // might need to change later
        hashMap.put("password", loginUser.getPassword());
        hashMap.put("email", loginUser.getEmail());
        hashMap.put("personalAlias", loginUser.getPersonalAlias());
        hashMap.put("phone", loginUser.getPhone());
        hashMap.put("photo", loginUser.getPhoto());
        hashMap.put("source", LoginUser.SOURCE_EMAIL);
        presenter.signUp(task, hashMap);
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
                    showKeyBoard(view);
                    setTopIconVisibility(View.GONE);
                }else{
                    closeKeyBoard(view);
                    setTopIconVisibility(View.VISIBLE);
                }
            }
        };
    }

    public View.OnClickListener onPasswordNextClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginUser.getPassword().length()<8){
                    mvpView.invalidPopup(INVALID_PASSWORD_TOO_SIMPLE);
                }else if (loginUser.getPassword().length()>16){
                    mvpView.invalidPopup(INVALID_PASSWORD_TOO_LONG);
                }else{
                    mvpView.onPageChange(TO_PICK_AVATAR_FRAG);
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
                loginUser.setEmail(suggestedEmailList.get(i));
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
                presenter.loginByEmail( TO_CALENDAR ,loginUser.getEmail(), loginUser.getPassword());
                Toast.makeText(getContext(), "logging in", Toast.LENGTH_SHORT).show();
//                mvpView.invalidPopup();
            }
        };
    }


    public View.OnClickListener onCleanEmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser.setEmail("");
                setLoginUser(loginUser);
            }
        };
    }

    public View.OnClickListener onClickResetCleanEmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser.setEmail("");
                setLoginUser(loginUser);
            }
        };
    }



    public View.OnClickListener toCalendar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.onLoginSucceed(TO_CALENDAR);
                }
                Toast.makeText(getContext(), "tocalendar here", Toast.LENGTH_SHORT).show();
            }
        };
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
    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
        notifyPropertyChanged(BR.loginUser);
    }
}
