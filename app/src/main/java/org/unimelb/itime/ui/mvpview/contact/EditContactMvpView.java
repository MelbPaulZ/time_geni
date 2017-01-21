package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public interface EditContactMvpView extends ItimeCommonMvpView {
    Activity getActivity();
    void showAlert();
}
