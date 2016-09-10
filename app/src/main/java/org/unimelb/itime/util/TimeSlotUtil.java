package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.TimeSlot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 10/09/2016.
 */
public class TimeSlotUtil {
    public static Map<Long, Boolean> fromTimeSlotsToMap(Context context, List<TimeSlot> timeSlotArrayList){
        Map<Long, Boolean> timeSlotsMap = new HashMap<>();
        for (TimeSlot timeSlot:timeSlotArrayList){
            String test1 = timeSlot.getStatus();
            String test2 = context.getString(R.string.timeslot_status_pending);
            if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_pending)) ){
                timeSlotsMap.put(timeSlot.getStartTime(), false);
            }else if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_accept))){
                timeSlotsMap.put(timeSlot.getStartTime(), true);
            }
        }
        return timeSlotsMap;
    }

    // if this time slot has been selected, return true
    public static boolean isTimeSlotSelected(Context context, TimeSlot timeSlot){
        if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_pending))){
            return false;
        }else if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_accept))){
            return true;
        }else{ // this might change later
            return false;
        }
    }

    public static boolean chooseAtLeastOnTimeSlot(Context context, List<TimeSlot> timeSlots){
        for (TimeSlot timeSlot: timeSlots){
            if (isTimeSlotSelected(context, timeSlot)){
                return true;
            }
        }
        return false;
    }
}
