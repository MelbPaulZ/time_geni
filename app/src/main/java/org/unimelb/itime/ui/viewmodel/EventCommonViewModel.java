package org.unimelb.itime.ui.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.unimelb.itime.bean.Event;

import java.util.Calendar;

import static android.webkit.WebSettings.PluginState.ON;

/**
 * Created by Paul on 5/1/17.
 */

public class EventCommonViewModel extends CommonViewModel {
    public DatePickerDialog datePickerDialog;
    public DatePickerDialog.OnDateSetListener dateSetListener;
    public TimePickerDialog timePickerDialog;
    public TimePickerDialog.OnTimeSetListener timeSetListener;
    public enum PickerTask{
        START_TIME, END_TIME, END_REPEAT
    }

    protected Calendar updateYearMonthDay(long timebase, int year, int monthOfYear, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timebase);
        c.set(year,monthOfYear,dayOfMonth);
        return c;
    }

    protected Calendar updateHourMin(long baseTime, int hour, int minute){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(baseTime);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        return c;
    }
}
