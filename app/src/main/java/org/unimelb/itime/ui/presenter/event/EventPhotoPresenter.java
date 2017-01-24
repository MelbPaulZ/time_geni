package org.unimelb.itime.ui.presenter.event;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.ui.mvpview.event.EventPhotoGridMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventPhotoPresenter extends MvpBasePresenter<EventPhotoGridMvpView> {
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public EventPhotoPresenter(Context context) {
        this.context = context;
    }
}
