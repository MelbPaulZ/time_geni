package org.unimelb.itime.managers;

import java.util.Calendar;

/**
 * Created by Paul on 1/12/16.
 */
public class CalendarManager {
    private Calendar currentShowCalendar = Calendar.getInstance(); // default today , this will change when a calendar jump to a different date
    private static CalendarManager instance = new CalendarManager();

    private CalendarManager(){

    }

    public static CalendarManager getInstance() {
        return instance;
    }


    public Calendar getCurrentShowCalendar() {
        return this.currentShowCalendar;
    }

    public void setCurrentShowCalendar(Calendar currentShowCalendar) {
        this.currentShowCalendar = currentShowCalendar;
    }
}
