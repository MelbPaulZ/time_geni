package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface InviteFriendMvpView  extends MvpView {

    Activity getActivity();

    void gotoInviteFacebookContacts();

    void gotoInviteGmailContacts();

    void gotoInviteMobileContacts();

    void onDoneClicked();

    void onBackClicked();

    void gotoScanQRCode();
}
