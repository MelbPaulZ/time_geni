package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class UserPresenter<V extends TaskBasedMvpView<User>> extends MvpBasePresenter<V>{

    public UserPresenter(Context context){

    }

    public void update(User user){

    }
}
