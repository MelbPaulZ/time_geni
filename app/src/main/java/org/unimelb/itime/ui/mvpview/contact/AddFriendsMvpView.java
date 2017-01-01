package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface AddFriendsMvpView extends MvpView {
    Activity getActivity();

    void goToAddMobileContacts();

    void goToAddFacebookContacts();

    void goToAddGmailContacts();

    void goToScanQRCode();

    void goToProfileFragment(Contact user);
}
