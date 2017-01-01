package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;
import android.view.View;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface ProfileMvpView extends MvpView {
    Activity getActivity();

    View getContentView();
    void goToInviteFragment(Contact user);
}
