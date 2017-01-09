package org.unimelb.itime.managers;

import java.util.Calendar;

/**
 * Created by Paul on 1/12/16.
 */
public class CalendarManager {
    private static Calendar currentShowCalendar; // default today , this will change when a calendar jump to a different date
    private static CalendarManager instance;

    private CalendarManager(){

    }

    public static CalendarManager getInstance() {
        if (currentShowCalendar== null ) {
            currentShowCalendar = Calendar.getInstance();
        }
        if (instance == null){
            instance = new CalendarManager();
        }
        return instance;
    }


    public Calendar getCurrentShowCalendar() {
        return instance.currentShowCalendar;
    }

    public void setCurrentShowCalendar(Calendar currentShowCalendar) {
        instance.currentShowCalendar = currentShowCalendar;
    }
}
