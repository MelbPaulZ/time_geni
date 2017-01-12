package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.CalendarPrefMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.mvpview.UserMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.ui.presenter.UserPresenter;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPrefViewModel extends CommonViewModel {

    public final static String TAG = "UserProfileViewModel";

    private CalendarPrefMvpView mvpView;
    private CommonPresenter<? extends TaskBasedMvpView<User>> presenter;
    private User user;

    public CalendarPrefViewModel(CommonPresenter<? extends TaskBasedMvpView<User>> presenter){
        this.presenter = presenter;
        if(presenter.getView() instanceof CalendarPrefMvpView){
            this.mvpView = (CalendarPrefMvpView) presenter.getView();
        }
    }

}
