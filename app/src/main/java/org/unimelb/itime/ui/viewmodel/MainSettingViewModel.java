package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.MainSettingMvpView;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingViewModel extends CommonViewModel{
    private MvpBasePresenter presenter;
    private MainSettingMvpView mvpView;
    private User user;

    public MainSettingViewModel(MvpBasePresenter presenter) {
        this.presenter = presenter;
        if (presenter.getView() instanceof MainSettingMvpView){
            this.mvpView = (MainSettingMvpView) presenter.getView();
        }
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public View.OnClickListener onProfileClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toProfilePage();
            }
        };
    }

    public View.OnClickListener onQRcodeClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toQRcodePage();
            }
        };
    }

    public View.OnClickListener onBlockUserClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toBlockedUserPage();
            }
        };
    }

    public View.OnClickListener onNotificationClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toNotificationPage();
            }
        };
    }

    public View.OnClickListener onCalendarPreferenceClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toCalendarPreferencePage();
            }
        };
    }

    public View.OnClickListener onHelpFdClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toHelpFdPage();
            }
        };
    }

    public View.OnClickListener onAboutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toAboutPage();
            }
        };
    }

    public View.OnClickListener onLogOutClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.onLogOut();
            }
        };
    }




}
