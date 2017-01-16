package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public interface BlockContactsMvpView extends MvpView {
    Activity getActivity();
    void goToProfileFragment(Contact contact);
}
