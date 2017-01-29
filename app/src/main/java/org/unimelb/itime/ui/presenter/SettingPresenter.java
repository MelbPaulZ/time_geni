package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.SettingApi;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */
public class SettingPresenter <V extends TaskBasedMvpView> extends MvpBasePresenter<V> {
    public static final int TASK_SETTING_UPDATE = 0;
    public static final int TASK_SEARCH_USER = 44444;

    private static final String TAG = "SettingPresenter";
    private Context context;
    private SettingApi settingApi;
    private UserApi userApi;

    public SettingPresenter(Context context) {
        this.context = context;
        this.settingApi = HttpUtil.createService(context, SettingApi.class);
        this.userApi = HttpUtil.createService(context, UserApi.class);
    }

    public Context getContext(){
        return context;
    }

    public void update(Setting setting){
        if(getView() != null){
            getView().onTaskStart(TASK_SETTING_UPDATE);
        }

        Observable<HttpResult<Setting>> observable = settingApi.update(setting);
        Subscriber<HttpResult<Setting>> subscriber = new Subscriber<HttpResult<Setting>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_SETTING_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Setting> settingHttpResult) {
                UserUtil.getInstance(getContext()).setSetting(settingHttpResult.getData());
                if(getView() != null){
                    getView().onTaskSuccess(TASK_SETTING_UPDATE, settingHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void findFriend(String searchStr){
        if(searchStr==null || searchStr.equals("")){
            return;
        }
        DBManager dbManager = DBManager.getInstance(context);
        searchStr = searchStr.trim();
        final List<Contact> contacts = dbManager.getAllContact();
        for(Contact contact:contacts){
            if(contact.getUserDetail().getPhone().equals(searchStr)
                    || contact.getUserDetail().getEmail().equals(searchStr)){
                if(getView()!=null) {
                    getView().onTaskSuccess(TASK_SEARCH_USER, contact);
                }
                AppUtil.hideProgressBar();
                return;
            }
        }

        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                AppUtil.hideProgressBar();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                AppUtil.hideProgressBar();
                Toast.makeText(context, context.getResources().getString(R.string.access_fail),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());
                if (result.getStatus()!=1){
                    if(getView()!=null) {
                        getView().onTaskError(TASK_SEARCH_USER, null);
                    }
                }else {
                    if(result.getData().isEmpty()){
                        if(getView()!=null) {
                            getView().onTaskError(TASK_SEARCH_USER, null);
                        }
                    }else {
                        User user = result.getData().get(0);
                        if(getView()!=null) {
                            getView().onTaskSuccess(TASK_SEARCH_USER, new Contact(user));
                        }
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

}
