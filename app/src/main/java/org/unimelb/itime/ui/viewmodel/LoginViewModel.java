package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.HashMap;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginViewModel extends AndroidViewModel{

    private static final String TAG = "LoginViewModel";
    private LoginPresenter presenter;


    private LoginMvpView mvpView;

    private int topIconVisibility = View.VISIBLE;

//    private ArrayList<String> suggestedEmailList = new ArrayList<>();
//    private ItemView suggestedEmailItemView = ItemView.of(BR.itemText, R.layout.listview_login_email_tips);


    private User loginUser;

    public final static int TO_EMAIL_SENT_FRAG = 1;
    public final static int TO_FIND_FRIEND_FRAG = 2;
    public final static int TO_LOGIN_FRAG = 3;
    public final static int TO_INDEX_FRAG = 4;
    public final static int TO_INPUT_EMAIL_FRAG = 5;
    public final static int TO_RESET_PASSWORD_FRAG = 7;
    public final static int TO_SET_PASSWORD_FRAG = 8;
    public final static int TO_TERM_AGREEMENT_FRAG = 9;
    public final static int TO_CALENDAR = 10;


    public LoginViewModel(LoginPresenter presenter){
        this.presenter = presenter;
        mvpView = presenter.getView();
//        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
//        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
//        this.suggestedEmailList.add("chuandongy@student.unimelb.edu.au");
    }

    private Context getContext(){
        return presenter.getContext();
    }

    public View.OnClickListener onSwitchFragment(final int task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (task){
                    case TO_EMAIL_SENT_FRAG:
                        presenter.sendResetLink(loginUser.getEmail());
                        break;
                    case TO_FIND_FRIEND_FRAG:
                        HashMap<String, String> params = new HashMap<>();
                        params.put("personalAlias", loginUser.getPersonalAlias());
                        presenter.validate(params);
                        break;
                    default:
                        mvpView.onPageChange(task);
                }
            }
        };
    }


//    public View.OnClickListener onClickSignUpBtn(){
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (loginUser.getPhoto().length()==0){
//                    presenter.uploadImageToLeanCloud(loginUser,
//                            DefaultPhotoUtil.getInstance().getPhoto(getContext(), loginUser.getPersonalAlias()));
//                }else{
//                    presenter.uploadImageToLeanCloud(loginUser, loginUser.getPhoto());
//                }
//            }
//        };
//    }


    public View.OnClickListener onClickInputEmailBtn(final int task){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate Email
                HashMap<String, String> params = new HashMap<>();
                params.put("userId", loginUser.getEmail()); // use userId to check the validation
                presenter.validate(params);

            }
        };
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

                HashMap<String, String> params = new HashMap<>();
                params.put("password", loginUser.getPassword());
                presenter.validate(params);
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
//                loginUser.setEmail(suggestedEmailList.get(i));
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
                //todo: check the userId and password
                presenter.loginByEmail(loginUser.getUserId(), loginUser.getPassword());
            }
        };
    }


    public View.OnClickListener onCleanEmail(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser.setUserId("");
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
                    mvpView.onPageChange(TO_CALENDAR);
                }
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

//    @Bindable
//    public ArrayList<String> getSuggestedEmailList(){
//        return this.suggestedEmailList;
//    }
//
//    public void setSuggestedEmailList(ArrayList<String> suggestedEmailList){
//        this.suggestedEmailList = suggestedEmailList;
//        notifyPropertyChanged(BR.suggestedEmailList);
//    }
//
//    @Bindable
//    public ItemView getSuggestedEmailItemView(){
//        return this.suggestedEmailItemView;
//    }
//
//    public void setSuggestedEmailItemView(ItemView suggestedEmailItemView){
//        this.suggestedEmailItemView = suggestedEmailItemView;
//        notifyPropertyChanged(BR.suggestedEmailItemView);
//    }

    @Bindable
    public User getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
        notifyPropertyChanged(BR.loginUser);
    }
}
