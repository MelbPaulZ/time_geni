package org.unimelb.itime.util;

import org.unimelb.itime.bean.Calendar;

/**
 * Created by Paul on 24/09/2016.
 */
public class CalendarUtil {
    private static CalendarUtil instance;
    private Calendar[] calendars;
    private CalendarUtil(){

    }

    public static CalendarUtil getInstance(){
        if (instance == null){
            instance = new CalendarUtil();
        }
        return instance;
    }

    public Calendar[] getCalendar() {
        return calendars;
    }

    public void setCalendar(Calendar[] calendars) {
        this.calendars = calendars;
    }

    public java.util.Calendar getNowCalendar(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        return calendar;
    }
}
