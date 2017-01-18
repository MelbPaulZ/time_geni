package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;

/**
 * Created by 37925 on 2016/12/18.
 */

public interface MyQRCodeMvpView extends ItimeCommonMvpView {
    Activity getActivity();

    View getContentView();

    void goToScanQRCode();

    void saveQRCode();

    void back();
}
