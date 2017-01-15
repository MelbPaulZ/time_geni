package org.unimelb.itime.ui.mvpview.contact;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface InviteFriendMvpView extends MvpView, ItimeCommonMvpView{

    void toInviteFacebookContactsPage();

    void toInviteGmailContactsPage();

    void toInviteMobileContactsPage();

    void toScanQRCodePage();
}
