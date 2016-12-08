package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends BaseObservable{
    public final static String TAG = "MainCalendarViewModel";
    private MainCalendarPresenter presenter;

    private String toolbarTitle;
    private MainCalendarMvpView mvpView;


    public MainCalendarViewModel(MainCalendarPresenter presenter) {
        super();
        this.presenter = presenter;
        mvpView = presenter.getView();
        toolbarTitle = initToolBarTitle();
    }

    public String getToday(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH) + "";
    }

    public String initToolBarTitle(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = -(calendar.get(Calendar.DAY_OF_WEEK)-1);
        calendar.add(Calendar.DATE,delta);

        return EventUtil.getMonth(getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }

    public Context getContext(){
        return presenter.getContext();
    }


    @Bindable
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        notifyPropertyChanged(BR.toolbarTitle);
    }

    public View.OnClickListener gotoEventCreateActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + System.currentTimeMillis());
                if (mvpView!=null){
                    mvpView.startCreateEventActivity();
                }
            }
        };
    }


}
