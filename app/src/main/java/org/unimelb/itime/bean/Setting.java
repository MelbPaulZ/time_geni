package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import static android.R.attr.id;

/**
 * Created by Paul on 28/12/2016.
 */

public class Setting {

    private User user;
    private List<Calendar> calendars;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasUser(){
        return user!=null;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
    }

    public boolean hasCalendar(){
        return calendars!=null;
    }
}
