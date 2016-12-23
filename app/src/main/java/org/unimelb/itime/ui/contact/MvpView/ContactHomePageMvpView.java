package org.unimelb.itime.ui.contact.MvpView;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface ContactHomePageMvpView extends MvpView {

    Activity getActivity();

    void goToNewFriendFragment();

    void goToAddFriendsFragment();

    void goToProfileFragment(ITimeUser user);

    ContactHomePageBinding getBinding();
}
