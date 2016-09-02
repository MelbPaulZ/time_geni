package org.unimelb.itime.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.TestActivityPaul;
import org.unimelb.itime.ui.presenter.EventCreateTimeSlotPresenter;
import org.unimelb.itime.vendor.BR;
import org.unimelb.itime.vendor.timeslotview.WeekTimeSlotView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeslotViewModel extends BaseObservable {
    private final String TAG = "TimeslotViewModel";
    private String toolbarString = getMonthName(Calendar.getInstance().get(Calendar.MONTH)) + " " + Calendar.getInstance().get(Calendar.YEAR);
    private EventCreateTimeSlotPresenter eventCreateTimeSlotPresenter;
    private ObservableField<Boolean> isChangeDuration = new ObservableField<>(false);
    private String durationTimeString = "1 hour";

    public EventCreateTimeslotViewModel(EventCreateTimeSlotPresenter eventCreateTimeSlotPresenter) {
        super();
        this.eventCreateTimeSlotPresenter = eventCreateTimeSlotPresenter;
    }


    //    ************************************************
    private String getMonthName(int index){
        String[] Months = {"January","February","March","April","May","June",
                "July","August","September","October","November","December"};
        return Months[index];
    }

    @Bindable
    public String getToolbarString() {
        return toolbarString;
    }

    public void setToolbarString(String toolbarString) {
        this.toolbarString = toolbarString;
        notifyPropertyChanged(BR.toolbarString);
    }

    public WeekTimeSlotView.OnTimeSlotWeekViewChangeListener onTimeSlotWeekViewChange(){
        return new WeekTimeSlotView.OnTimeSlotWeekViewChangeListener() {
            @Override
            public void onWeekChanged(Calendar calendar) {
                String tmp = getMonthName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
                setToolbarString(tmp);
            }
        };
    }

    public WeekTimeSlotView.OnTimeSlotClickListener onTimeSlotClick(){
        return new WeekTimeSlotView.OnTimeSlotClickListener() {
            @Override
            public void onTimeSlotClick(long l) {

            }
        };
    }

    public void clickDoneBtn(){
        eventCreateTimeSlotPresenter.toNewEventDetailBeforeSending();

    }




    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(RelativeLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }

    @Bindable
    public boolean getIsChangeDuration() {
        return isChangeDuration.get();
    }

    public void setIsChangeDuration(boolean isChangeDuration) {
        this.isChangeDuration.set(isChangeDuration);
        notifyPropertyChanged(BR.isChangeDuration);
    }

    @Bindable
    public String getDurationTimeString() {
        return durationTimeString;
    }

    public void setDurationTimeString(String durationTimeString) {
        this.durationTimeString = durationTimeString;
        notifyPropertyChanged(BR.durationTimeString);
    }
}
