package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public interface LoginMvpView extends MvpView{

    void onLoginStart();
    void onLoginSucceed();
    void onLoginFail(int errorCode, int errorMsg);
}
