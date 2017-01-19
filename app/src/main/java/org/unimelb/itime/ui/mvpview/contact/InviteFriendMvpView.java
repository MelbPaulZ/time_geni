package org.unimelb.itime.ui.mvpview.contact;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface InviteFriendMvpView extends TaskBasedMvpView, ItimeCommonMvpView{

    void toInviteFacebookContactsPage();

    void toInviteGmailContactsPage();

    void toInviteMobileContactsPage();

    void toScanQRCodePage();
}
