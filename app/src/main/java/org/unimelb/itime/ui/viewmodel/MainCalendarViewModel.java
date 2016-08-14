package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends BaseObservable{
    public final static String TAG = "MainCalendarViewModel";
    private MainCalendarPresenter presenter;

    private User user;


    public MainCalendarViewModel(MainCalendarPresenter presenter) {
        super();
        this.presenter = presenter;
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public View.OnClickListener testClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setUserId("yin-changed");
                notifyPropertyChanged(BR.user);
                Log.d(TAG, "onClick: " + user.getId());
                presenter.testHttp();
            }
        };
    }


}
