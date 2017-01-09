package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.ui.mvpview.TimeslotCreateMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.unitviews.DraggableTimeSlotView;

import java.util.Calendar;

/**
 * Created by Paul on 20/11/16.
 */
public class TimeslotCreateViewModel extends CommonViewModel {
    private DraggableTimeSlotView newTimeSlotView;
    private TimeslotCommonPresenter<TimeslotCreateMvpView> presenter;
    private TimeslotCreateMvpView mvpView;
    public final int STARTTIME = 1000;
    public final int ENDTIME = 1001;


    public TimeslotCreateViewModel(TimeslotCommonPresenter<TimeslotCreateMvpView> presenter) {
        this.presenter = presenter;
        mvpView = presenter.getView();

    }

    private Context getContext(){
        return presenter.getContext();
    }

    public String getTimeString(long l){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        int min = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String minStr = min<10? "0"+ min : min + "";
        String hourStr = hour<10? "0" + hour : hour + "";
        return EventUtil.getMonth(getContext(),month) + " " + day + ", " + year + "    " + hourStr + ":"+minStr;
    }

    public View.OnClickListener onChooseTime(final int index){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = index==STARTTIME? newTimeSlotView.getNewStartTime() : newTimeSlotView.getNewEndTime();
                mvpView.onChooseTime(index, time);
            }
        };
    }

    public View.OnClickListener onClickPickerDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickPickerDone();
                }
            }
        };
    }

    public void setStartTime(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        newTimeSlotView.setCalendar(new MyCalendar(calendar));
        setNewTimeSlotView(newTimeSlotView);
    }



    @Bindable
    public DraggableTimeSlotView getNewTimeSlotView() {
        return newTimeSlotView;
    }

    public void setNewTimeSlotView(DraggableTimeSlotView newTimeSlotView) {
        this.newTimeSlotView = newTimeSlotView;
        notifyPropertyChanged(BR.newTimeSlotView);
    }

    public View.OnClickListener onClickCancel(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickCancel();
                }
            }
        };
    }

    public View.OnClickListener onClickDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickDone();
                }
            }
        };
    }


}
