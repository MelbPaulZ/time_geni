package org.unimelb.itime.util;


import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.base.C;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Paul on 8/09/2016.
 */
public class EventUtil{
    public static String parseTimeToString(Context context, long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getMonth(context, calendar.get(Calendar.MONTH));
        String startTimeHour = calendar.get(Calendar.HOUR_OF_DAY)<10? "0" + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0"+String.valueOf(calendar.get(Calendar.MINUTE)): String.valueOf(calendar.get(Calendar.MINUTE));
        return month + " " + day + ", " + calendar.get(Calendar.YEAR) + "   " + startTimeHour + ":" + startMinute;
    }

    public static String parseDateToString(Context context, long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getMonth(context, calendar.get(Calendar.MONTH));
        return dayOfWeek + " ," + month + " " + day;
    }

    public static String getAttendeeString(Context context,ArrayList<String> attendeesArrayList) {
        if (attendeesArrayList.size() == 0) {
            return context.getString(R.string.none);
        } else if (attendeesArrayList.size() == 1)
            return attendeesArrayList.get(0);
        else {
            return String.format("%s and %d more", attendeesArrayList.get(0), attendeesArrayList.size() - 1);
        }
    }


    public static String parseRepeatIdToRepeat(Context context,int repeat, long startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (repeat){
            case 0:
                return context.getString(R.string.repeat_never);
            case 1:
                return context.getString(R.string.repeat_everyday);
            case 2:
                return String.format(context.getString(R.string.repeat_everyweek), getDayOfWeekFull(context,dayOfWeek));
            case 3:
                return context.getString(R.string.repeat_every_twoweek);
            case 4:
                return context.getString(R.string.repeat_every_month);
            case 5:
                return context.getString(R.string.repeat_every_year);
            default:
                return context.getString(R.string.repeat_never);
        }
    }

    public static String getDayOfWeekFull(Context context,int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return context.getString(R.string.day_of_week_1_full);
            case 2:
                return context.getString(R.string.day_of_week_2_full);
            case 3:
                return context.getString(R.string.day_of_week_3_full);
            case 4:
                return context.getString(R.string.day_of_week_4_full);
            case 5:
                return context.getString(R.string.day_of_week_5_full);
            case 6:
                return context.getString(R.string.day_of_week_6_full);
            case 7:
                return context.getString(R.string.day_of_week_7_full);
        }
        return "error get day of week";
    }


    public static String getDayOfWeekAbbr(Context context, int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return context.getString(R.string.day_of_week_1_abbr);
            case 2:
                return context.getString(R.string.day_of_week_2_abbr);
            case 3:
                return context.getString(R.string.day_of_week_3_abbr);
            case 4:
                return context.getString(R.string.day_of_week_4_abbr);
            case 5:
                return context.getString(R.string.day_of_week_5_abbr);
            case 6:
                return context.getString(R.string.day_of_week_6_abbr);
            case 7:
                return context.getString(R.string.day_of_week_7_abbr);
        }
        return "";
    }

    public static String getMonth(Context context, int month){
        switch (month){
            case 0:
                return context.getString(R.string.month_1st_Abbr);
            case 1:
                return context.getString(R.string.month_2nd_Abbr);
            case 2:
                return context.getString(R.string.month_3rd_Abbr);
            case 3:
                return context.getString(R.string.month_4th_Abbr);
            case 4:
                return context.getString(R.string.month_5th_Abbr);
            case 5:
                return context.getString(R.string.month_6th_Abbr);
            case 6:
                return context.getString(R.string.month_7th_Abbr);
            case 7:
                return context.getString(R.string.month_8th_Abbr);
            case 8:
                return context.getString(R.string.month_9th_Abbr);
            case 9:
                return context.getString(R.string.month_10th_Abbr);
            case 10:
                return context.getString(R.string.month_11th_Abbr);
            case 11:
                return context.getString(R.string.month_12th_Abbr);
            default:
                return "error";
        }
    }
}
