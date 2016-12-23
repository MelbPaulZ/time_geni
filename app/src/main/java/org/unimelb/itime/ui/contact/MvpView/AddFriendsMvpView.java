package org.unimelb.itime.ui.contact.MvpView;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.contact.Beans.ITimeUser;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface AddFriendsMvpView extends MvpView {
    Activity getActivity();

    void goToAddMobileContacts();

    void goToAddFacebookContacts();

    void goToAddGmailContacts();

    void goToScanQRCode();

    void goToProfileFragment(ITimeUser user, String show);
}
