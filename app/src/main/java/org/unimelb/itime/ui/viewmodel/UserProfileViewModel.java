package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.UserMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class UserProfileViewModel extends CommonViewModel {

    public final static String TAG = "UserProfileViewModel";

    private UserMvpView mvpView;
    private User user;

    public UserProfileViewModel(UserMvpView mvpView){
        this.mvpView = mvpView;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    public View.OnClickListener onAvatarClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditPhotoPage();
            }
        };
    }

    public View.OnClickListener onNameClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditNamePage();
            }
        };
    }

    public View.OnClickListener onResetPasswordClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditPasswordPage();
            }
        };
    }

    public View.OnClickListener onMyQrCodeClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditMyQrCodePage();
            }
        };
    }

    public View.OnClickListener onGenderClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditGenderPage();
            }
        };
    }

    public View.OnClickListener onRegionClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvpView.toEditRegionPage();
            }
        };
    }



}
