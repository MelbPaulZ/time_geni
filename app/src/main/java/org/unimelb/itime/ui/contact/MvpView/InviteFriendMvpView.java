package org.unimelb.itime.ui.contact.MvpView;

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
}
