package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.widget.SideBarListView;

/**
 * Created by 37925 on 2016/12/16.
 */

public interface InviteContactsMvpView extends MvpView{
    Activity getActivity();

    SideBarListView getSideBarListView();
}
