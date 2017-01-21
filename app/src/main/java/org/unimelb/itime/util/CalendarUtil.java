package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.dao.CalendarDao;
import org.unimelb.itime.dao.UserDao;
import org.unimelb.itime.managers.DBManager;

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
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(Calendar.class);
        List<Calendar> cals = dao.queryBuilder().where(
                CalendarDao.Properties.CalendarUid.eq(event.getCalendarUid())
        ).list();

        if (cals.size() == 0){
            return "N/A";
        }

        return cals.get(0).getSummary();
    }

    private void init(){
        List<Calendar> calendarList = DBManager.getInstance(context).getAllCalendarsForUser();
        this.calendars = calendarList;
    }

    public void clear(){
        instance = null;
    }



}
