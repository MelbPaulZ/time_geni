package org.unimelb.itime.util;

import org.unimelb.itime.bean.Calendar;

import java.util.List;

/**
 * Created by Paul on 24/09/2016.
 */
public class CalendarUtil {
    private static CalendarUtil instance;
    private List<Calendar> calendars;
    private CalendarUtil(){

    }

    public static CalendarUtil getInstance(){
        if (instance == null){
            instance = new CalendarUtil();
        }
        return instance;
    }

    public List<Calendar> getCalendar() {
        return calendars;
    }

    public void setCalendar(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public java.util.Calendar getNowCalendar(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        return calendar;
    }
}
