package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginPresenter extends MvpBasePresenter<LoginMvpView>{
    private static final String TAG = "LoginPresenter";

    private Context context;
    private UserApi userApi;
    public LoginPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
    }

    public void loginByEmail(String email, String password){

        LoginMvpView view = getView();
        if (view != null){
            // call retrofit
            view.onLoginStart();
        }

        AuthUtil.clearJwtToken(context);
        Call<JwtToken> call = userApi.login("johncdyin@gmail.com", "123456");
        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                JwtToken jwt = response.body();
                Log.d(TAG, "login: onResponse: " + jwt.getToken());
                AuthUtil.saveJwtToken(context, jwt.getToken());
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.d(TAG, "login: onFailure: " + t.getMessage());
            }
        });
    }


    public void refreshToken(){
        String authToken = AuthUtil.getJwtToken(context);
        Call<JwtToken> call = userApi.refreshToken(authToken);
        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                JwtToken jwt = response.body();
                Log.d(TAG, "refresh: onResponse: " + jwt.getToken());
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.d(TAG, "refresh: onFailure: " + t.getMessage());
            }
        });
    }


    public void listUser(){
        Call<User> call = userApi.list();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null){
                    Log.d(TAG, "listuser: onResponse: " + user.getUserId());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "listuser: onFailure: " + t.getMessage());
            }
        });
    }

}
