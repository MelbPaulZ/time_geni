package org.unimelb.itime.managers;

import java.util.Calendar;

/**
 * Created by Paul on 1/12/16.
 */

/**
 * The calendar Manager used for control when the calendar should be displayed
 */
public class CalendarManager {
    private Calendar currentShowCalendar; // default today , this will change when a calendar jump to a different date
    private static CalendarManager instance;

    private CalendarManager(){

    }

    public static CalendarManager getInstance() {
        if (instance == null){
            instance = new CalendarManager();
        }

        if (instance.currentShowCalendar== null ) {
            instance.currentShowCalendar = Calendar.getInstance();
        }
        return instance;
    }


    public Calendar getCurrentShowCalendar() {
        return instance.currentShowCalendar;
    }

    public void setCurrentShowCalendar(Calendar currentShowCalendar) {
        instance.currentShowCalendar = currentShowCalendar;
    }

    public void clear(){
        currentShowCalendar = null;
        instance = null;
    }
}
