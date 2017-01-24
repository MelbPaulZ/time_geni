package org.unimelb.itime.ui.presenter.event;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.event.EventBigPhotoMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventBigPhotoPresenter extends MvpBasePresenter<EventBigPhotoMvpView> {
    private Context context;

    public EventBigPhotoPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
