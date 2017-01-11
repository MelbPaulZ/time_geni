package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.fragment.settings.SettingIndexFragment;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingCommonPresenter<T extends SettingCommonMvpView> extends CommonPresenter<T> {

    private static final String TAG = "SettingWrapper";
    private UserApi userApi;

    public SettingCommonPresenter(Context context) {
        super(context);
        userApi = HttpUtil.createService(context, UserApi.class);
    }

    public void findFriend(String searchStr, final SettingIndexFragment.FindFriendCallBack callBack){
        DBManager dbManager = DBManager.getInstance(getContext());
        List<Contact> contacts = dbManager.getAllContact();
        for(Contact contact:contacts){
            if(contact.getUserDetail().getPhone().equals(searchStr)
                    || contact.getUserDetail().getEmail().equals(searchStr)){
                callBack.success(contact);
                return;
            }
        }

        Observable<HttpResult<List<User>>> observable = userApi.search(searchStr);
        Subscriber<HttpResult<List<User>>> subscriber = new Subscriber<HttpResult<List<User>>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                callBack.failed();
                e.printStackTrace();
            }

            @Override
            public void onNext(HttpResult<List<User>> result) {
                Log.d(TAG, "onNext: " + result.getInfo());

                if (result.getStatus()!=1){

                }else {
                    if(result.getData().isEmpty()){
                        callBack.failed();
                    }else {
                        User user = result.getData().get(0);
                        callBack.success(new Contact(user));
                    }
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }


    public void update(){
        
    }

}
