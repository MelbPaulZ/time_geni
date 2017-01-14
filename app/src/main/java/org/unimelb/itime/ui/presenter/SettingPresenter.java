package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.SeekBar;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.restfulapi.MessageApi;
import org.unimelb.itime.restfulapi.SettingApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 3/10/16.
 */
public class SettingPresenter <V extends TaskBasedMvpView<Setting>> extends MvpBasePresenter<V> {
    public static final int TASK_SETTING_UPDATE = 0;

    private static final String TAG = "SettingPresenter";
    private Context context;
    private SettingApi settingApi;

    public SettingPresenter(Context context) {
        this.context = context;
        this.settingApi = HttpUtil.createService(context, SettingApi.class);
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
                    getView().onTaskError(TASK_SETTING_UPDATE);
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

}
