package org.unimelb.itime.ui.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.base.C;
import org.unimelb.itime.ui.mvpview.LoginMvpView;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginPresenter extends MvpBasePresenter<LoginMvpView>{
    private static final String TAG = "LoginPresenter";

    private Retrofit retrofit;

    public LoginPresenter(){
        retrofit = new Retrofit.Builder()
                .baseUrl(C.api.BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void loginByEmail(String email, String password){

        LoginMvpView view = getView();
        if (view != null){
            // call retrofit
            view.onLoginStart();
        }
    }

}
