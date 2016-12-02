package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.MainInboxMvpView;

/**
 * Created by Paul on 3/10/16.
 */
public class MainInboxPresenter extends MvpBasePresenter<MainInboxMvpView> {
    private Context context;

    public MainInboxPresenter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
