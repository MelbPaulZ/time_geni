package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingCommonPresenter<T extends MvpView> extends CommonPresenter<T> {

    public SettingCommonPresenter(Context context) {
        super(context);
    }
}
