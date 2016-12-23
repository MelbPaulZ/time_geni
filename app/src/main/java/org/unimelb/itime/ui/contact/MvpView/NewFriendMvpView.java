package org.unimelb.itime.ui.contact.MvpView;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface NewFriendMvpView extends MvpView {
    Activity getActivity();

    void goToAddFriendsFragment();
}
