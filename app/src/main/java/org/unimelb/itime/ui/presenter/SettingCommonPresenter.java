package org.unimelb.itime.ui.presenter;

import android.content.Context;

import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView_delete;
import org.unimelb.itime.util.HttpUtil;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingCommonPresenter<T extends SettingCommonMvpView_delete> extends CommonPresenter<T> {

    private static final String TAG = "SettingWrapper";
    private UserApi userApi;

    public SettingCommonPresenter(Context context) {
        super(context);
        userApi = HttpUtil.createService(context, UserApi.class);
    }



    public void update(){
        
    }

}
