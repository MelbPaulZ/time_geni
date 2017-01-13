package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.ui.mvpview.TimeslotMvpView;
import org.unimelb.itime.ui.presenter.TimeslotPresenter;
import org.unimelb.itime.util.EventUtil;

import java.util.Calendar;




/**
 * Created by Paul on 20/11/16.
 */
public class TimeslotCreateViewModel extends CommonViewModel {
    private TimeslotPresenter<TimeslotMvpView> presenter;
    private TimeslotMvpView mvpView;
    public final int STARTTIME = 1000;
    public final int ENDTIME = 1001;
    private Timeslot timeslot;

    private String startTimeStr = "";
    private String endTimeStr = "";


    public TimeslotCreateViewModel(TimeslotPresenter<TimeslotMvpView> presenter) {
        this.presenter = presenter;
        mvpView = presenter.getView();
    }

    private Context getContext(){
        return presenter.getContext();
    }

    private String parseTime(long l){
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
                long time = index == STARTTIME ? timeslot.getStartTime() : timeslot.getEndTime();
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


    @Bindable
    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;

        setStartTimeStr(parseTime(timeslot.getStartTime()));
        setEndTimeStr(parseTime(timeslot.getEndTime()));
        notifyPropertyChanged(BR.timeslot);
    }

    @Bindable
    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
        notifyPropertyChanged(BR.startTimeStr);
    }

    @Bindable
    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
        notifyPropertyChanged(BR.endTimeStr);
    }
}
