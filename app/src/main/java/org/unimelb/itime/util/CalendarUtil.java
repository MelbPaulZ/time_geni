package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Event;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by Paul on 24/09/2016.
 */
public class CalendarUtil {
    private static CalendarUtil instance;
    private List<Calendar> calendars;
    private Context context;
    private CalendarUtil(Context context){
        this.context = context;
    }

    public static CalendarUtil getInstance(Context context){
        if (instance == null){
            instance = new CalendarUtil(context);
            instance.init();
        }
        return instance;
    }

    public List<Calendar> getCalendar() {
        return calendars;
    }

    public void setCalendar(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public String getCalendarName(Event event){
        String calendarUid = event.getCalendarUid();
        for (Calendar calendar: getCalendar()){
            if (calendarUid.equals(calendar.getCalendarUid())){
                return calendar.getSummary();
            }
        }

        // find default calendar
        Calendar defaultCalendar = null;
        for (Calendar cal: CalendarUtil.getInstance(context).getCalendar()){
            if (cal.getCalendarUid().equals(UserUtil.getInstance(context).getUserUid())){
                defaultCalendar = cal;
            }
        }

        if (defaultCalendar!=null){
            return defaultCalendar.getSummary();
        }else {
            return "";
        }
    }

    private void init(){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String calendarStr = sp.getString(C.calendarString.CALENDAR_STRING,"");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Calendar>>() {}.getType();
        List<Calendar> calendars = gson.fromJson(calendarStr, listType);
        this.calendars = calendars;
    }


    public static List<Calendar> getCalendarsFromPreferences(Context context){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        String calendarStr = sp.getString(C.calendarString.CALENDAR_STRING,"");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Calendar>>() {}.getType();
        List<Calendar> calendars = gson.fromJson(calendarStr, listType);
        return calendars;
    }


}
