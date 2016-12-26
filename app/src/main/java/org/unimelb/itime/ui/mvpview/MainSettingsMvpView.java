package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 26/12/2016.
 */

public interface MainSettingsMvpView extends MvpView, SettingCommonMvpView {
    void logout();
}
