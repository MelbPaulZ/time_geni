package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;

/**
 * Created by Paul on 25/12/2016.
 */

public interface SettingCommonMvpView_delete extends ItimeCommonMvpView {

    void onViewChange(int task, boolean isSave);
}
