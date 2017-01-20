package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.ContactHomePageBinding;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface ContactHomePageMvpView extends ItimeCommonMvpView, TaskBasedMvpView<List<ITimeUser>> {

    Activity getActivity();

    void goToNewFriendFragment();

    void goToAddFriendsFragment();

    void goToProfileFragment(Contact user);

    ContactHomePageBinding getBinding();
}
