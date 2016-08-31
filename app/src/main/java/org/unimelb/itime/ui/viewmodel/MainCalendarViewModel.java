package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.presenter.MainCalendarPresenter;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends BaseObservable{
    public final static String TAG = "MainCalendarViewModel";
    private MainCalendarPresenter presenter;

    private String toolbarTitle = getMonthName(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR);


    public MainCalendarViewModel(MainCalendarPresenter presenter) {
        super();
        this.presenter = presenter;
    }


    @Bindable
    public String getToolbarTitle() {
        return toolbarTitle;
    }

    public void setToolbarTitle(String toolbarTitle) {
        this.toolbarTitle = toolbarTitle;
        notifyPropertyChanged(BR.toolbarTitle);
    }


    public WeekView.OnWeekViewChangeListener onWeekViewChange(){
        return new WeekView.OnWeekViewChangeListener() {
            @Override
            public void onWeekChanged(Calendar calendar) {
                int month = calendar.get(Calendar.MONTH);
                String tmp = getMonthName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
                setToolbarTitle(tmp);

            }
        };
    }


    private String getMonthName(int index){
        String[] Months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        return Months[index];
    }

    public View.OnClickListener gotoEventCreateActivity(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoCreateEventActivity();
            }
        };
    }

    public WeekView.OnClickEventInterface onClickEvent(){
        return new WeekView.OnClickEventInterface() {
            @Override
            public void onClickEditEvent(ITimeEventInterface iTimeEventInterface) {
                presenter.gotoEditEventActivity(iTimeEventInterface);
            }
        };
    }


}
