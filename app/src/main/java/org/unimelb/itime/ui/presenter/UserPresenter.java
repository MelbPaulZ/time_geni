package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

import static org.unimelb.itime.ui.presenter.contact.ContextPresenter.getContext;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class UserPresenter<V extends TaskBasedMvpView<User>> extends MvpBasePresenter<V>{
    private static final String TAG = "UserPresenter";

    public static final int TASK_USER_UPDATE = 0;
    public static final int TASK_USER_PSW_NOT_MATCH = 1;

    private Context context;
    private UserApi userApi;

    public UserPresenter(Context context){
        this.context = context;
        userApi = HttpUtil.createService(context,UserApi.class);
    }

    public void updateProfile(User user){
        if(getView() != null){
            getView().onTaskStart(TASK_USER_UPDATE);
        }

        Observable<HttpResult<User>> observable = userApi.updateProfile(user);
        Subscriber<HttpResult<User>> subscriber = new Subscriber<HttpResult<User>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_USER_UPDATE);
                }
            }

            @Override
            public void onNext(HttpResult<User> userHttpResult) {
                //update UserLoginRes
                UserUtil.getInstance(getContext()).getUserLoginRes().setUser(userHttpResult.getData());
                //update sharedPreference
                UserUtil.getInstance(getContext()).login(UserUtil.getInstance(getContext()).getUserLoginRes());

                if(getView() != null){
                    getView().onTaskSuccess(TASK_USER_UPDATE, userHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void updatePassword(String psw, String confirmedPsw){
        if(getView() != null){
            getView().onTaskStart(TASK_USER_UPDATE);
        }

        HashMap<String,Object> pswPackage = new HashMap<>();
        pswPackage.put("password",psw);
        pswPackage.put("password_confirmation",confirmedPsw);

        Observable<HttpResult<User>> observable = userApi.updatePassword(pswPackage);
        Subscriber<HttpResult<User>> subscriber = new Subscriber<HttpResult<User>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_USER_UPDATE);
                }
            }

            @Override
            public void onNext(HttpResult<User> userHttpResult) {
                if(getView() != null){
                    getView().onTaskSuccess(TASK_USER_UPDATE, userHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
