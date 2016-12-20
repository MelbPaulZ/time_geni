package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends CommonViewModel{
    public final static String TAG = "MainCalendarViewModel";
    private CommonPresenter<MainCalendarMvpView> presenter;

    private String toolbarTitle;
    private MainCalendarMvpView mvpView;


    public MainCalendarViewModel(CommonPresenter<MainCalendarMvpView> presenter) {
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

    public View.OnClickListener onClickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mvpView!=null){
                    mvpView.backToGroupEvent();
                }
            }
        };
    }

}
