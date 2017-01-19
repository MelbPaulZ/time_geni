package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ITimeUser;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public interface BlockContactsMvpView extends ItimeCommonMvpView, TaskBasedMvpView<List<ITimeUser>> {
    Activity getActivity();
    void goToProfileFragment(Contact contact);
}
