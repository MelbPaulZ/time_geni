package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.dao.UserDao;
import org.unimelb.itime.restfulapi.PasswordApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.ui.viewmodel.LoginViewModel;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class LoginPresenter extends MvpBasePresenter<LoginMvpView> {
    private static final String TAG = "LoginPresenter";

    private Context context;
    private UserApi userApi;
    private PasswordApi passwordApi;

    public LoginPresenter(Context context) {
        this.context = context;
        userApi = HttpUtil.createService(context, UserApi.class);
        passwordApi = HttpUtil.createService(context, PasswordApi.class);
    }

    public Context getContext(){
        return context;
    }

    public void loginByEmail(final int task, String email, String password) {

        LoginMvpView view = getView();
        if (view != null) {
            // call retrofit
            view.onLoginStart();
        }
        AuthUtil.clearJwtToken(context);
        Observable<HttpResult<UserLoginRes>> observable = userApi.login(email, password);
        Subscriber<HttpResult<UserLoginRes>> subscriber = new Subscriber<HttpResult<UserLoginRes>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                // for test.... because always on error
//                if (getView() != null) {
//                    getView().onLoginSucceed();
//                }
            }

            @Override
            public void onNext(HttpResult<UserLoginRes> result) {
                if(result.getStatus() != 1){
                    throw new RuntimeException(result.getInfo());
                }
                Log.d(TAG, "onNext: " + result.getData().getToken());
                if (result.getStatus()!=1){
                    Toast.makeText(context, "username or password error",Toast.LENGTH_SHORT);
                }else {
                    AuthUtil.saveJwtToken(context, result.getData().getToken());
                    UserUtil.getInstance(context).login(result.getData());
                    if (getView() != null) {
                        getView().onLoginSucceed(task);
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    public void refreshToken() {
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


    public void listUser() {
        Observable<User> observable = userApi.list()
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
                });
        Subscriber<User> subscriber = new Subscriber<User>() {
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
                if (user != null) {
                    Log.d(TAG, "listuser: onNext : " + user.getUserId());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void testList() {
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onNext(HttpResult<List<User>> listHttpResult) {
                Log.d(TAG, "onNext: ");
            }
        };
        HttpUtil.subscribe(userApi.test(), subscriber);
    }


    public void sendResetLink(final int task, String contact){

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("contact", contact);

        Observable<HttpResult<Void>> observable = passwordApi.sendResetLink(hashMap);
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onLoginFail(task, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<Void> voidHttpResult) {
                if (getView()!=null) {
                    if (voidHttpResult.getStatus() != 1) {
                        getView().onLoginFail(task, voidHttpResult.getInfo());
                    }else{
                        getView().onLoginSucceed(task);

                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    public void reset(final int task){
        Observable<HttpResult<Void>> observable = passwordApi.reset();
        Subscriber<HttpResult<Void>> subscriber = new Subscriber<HttpResult<Void>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onLoginFail(task, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<Void> voidHttpResult) {
                if (getView()!=null){
                    getView().onLoginSucceed(task);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void signUp(final int task, final HashMap<String, Object> params){
        Observable<HttpResult<User>> observable = userApi.signup(params);
        Subscriber<HttpResult<User>> subscriber = new Subscriber<HttpResult<User>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (getView()!=null){
                    getView().onLoginFail(task, e.getMessage());
                }
            }

            @Override
            public void onNext(HttpResult<User> userHttpResult) {
                if (userHttpResult.getStatus()!=1){ // if some error
                    getView().onLoginFail(task, userHttpResult.getInfo());
                }
                if (getView()!=null){
                    loginByEmail( LoginViewModel.TO_FIND_FRIEND_FRAG,(String)params.get("email"), (String)params.get("password"));

//                    getView().onLoginSucceed(task);
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

}
