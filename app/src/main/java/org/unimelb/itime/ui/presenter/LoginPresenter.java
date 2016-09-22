package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.dao.UserDao;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.GreenDaoUtil;
import org.unimelb.itime.util.HttpUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginPresenter extends MvpBasePresenter<LoginMvpView>{
    private static final String TAG = "LoginPresenter";

    private Context context;
    private UserApi userApi;

    private UserDao userDao;

    public LoginPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        userDao = GreenDaoUtil.getDaoSession(context).getUserDao();
    }

    public void loginByEmail(String email, String password){

        LoginMvpView view = getView();
        if (view != null){
            // call retrofit
            view.onLoginStart();
        }

        AuthUtil.clearJwtToken(context);
        userApi.login("johncdyin@gmail.com", "123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JwtToken>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(JwtToken jwtToken) {
                        Log.d(TAG, "onNext: " + jwtToken.getToken());
                        AuthUtil.saveJwtToken(context, jwtToken.getToken());
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
                AuthUtil.saveJwtToken(context, jwt.getToken());
                Log.d(TAG, "refresh: onResponse: " + jwt.getToken());
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.d(TAG, "refresh: onFailure: " + t.getMessage());
            }
        });
    }


    public void listUser(){
        userApi.list()
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        try {
                            Log.d(TAG, "doOnNext: call: ");
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException he = (HttpException) e;
                        Log.d(TAG, "onError: " + he.response().errorBody());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(User user) {
                        if (user != null){
                            Log.d(TAG, "listuser: onNext : " + user.getUserId());
                        }
                    }
                });

    }

}
