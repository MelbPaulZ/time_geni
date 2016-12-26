package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingMyProfileMvpView;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingCommonPresenter<T extends SettingCommonMvpView> extends CommonPresenter<T> {

    public SettingCommonPresenter(Context context) {
        super(context);
    }

}
