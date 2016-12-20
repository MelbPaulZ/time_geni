package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 21/12/2016.
 */

public class CommonPresenter<T extends MvpView> extends MvpBasePresenter<T> {
    private Context context;
    public CommonPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
