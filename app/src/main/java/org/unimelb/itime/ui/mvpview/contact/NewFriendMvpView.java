package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface NewFriendMvpView extends ItimeCommonMvpView, TaskBasedMvpView<List<RequestFriend>> {
    Activity getActivity();

    void goToAddFriendsFragment();

    void goToProfileFragment(String userId, int mode);
}
