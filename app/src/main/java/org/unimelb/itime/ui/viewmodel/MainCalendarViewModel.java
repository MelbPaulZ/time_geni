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

    private String toolbarTitle = initToolBarTitle();
    private MainCalendarMvpView mvpView;


    public MainCalendarViewModel(MainCalendarPresenter presenter) {
        super();
        this.presenter = presenter;
        mvpView = presenter.getView();
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
                if (mvpView!=null){
                    mvpView.startCreateEventActivity();
                }
            }
        };
    }


//    public WeekViewBody.OnWeekBodyListener onWeekBodyListener(){
//        return new WeekViewBody.OnWeekBodyListener() {
//            @Override
//            public void onEventCreate(WeekDraggableEventView weekDraggableEventView) {
//
//            }
//
//            @Override
//            public void onEventClick(WeekDraggableEventView weekDraggableEventView) {
//                presenter.gotoEditEventActivity(weekDraggableEventView.getEvent());
//            }
//
//            @Override
//            public void onEventDragStart(WeekDraggableEventView weekDraggableEventView) {
//
//            }
//
//            @Override
//            public void onEventDragging(WeekDraggableEventView weekDraggableEventView, int i, int i1) {
//
//            }
//
//            @Override
//            public void onEventDragDrop(WeekDraggableEventView weekDraggableEventView) {
//
//            }
//        };
//    }



//    public WeekView.OnClickEventInterface onClickEvent(){
//        return new WeekView.OnClickEventInterface() {
//            @Override
//            public void onClickEditEvent(ITimeEventInterface iTimeEventInterface) {
//                presenter.gotoEditEventActivity(iTimeEventInterface);
//            }
//        };
//    }


}
