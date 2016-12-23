package org.unimelb.itime.ui.contact.MvpView;

import android.app.Activity;
import android.view.View;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.contact.Beans.ITimeUser;

/**
 * Created by 37925 on 2016/12/14.
 */

public interface ProfileMvpView extends MvpView {
    Activity getActivity();

    View getContentView();
    void goToInviteFragment(ITimeUser user);
}
