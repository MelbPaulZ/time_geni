package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
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


    //get all selected timeslots
    public static List<TimeSlot> getSelectedTimeSlots(Context context,List<TimeSlot> timeSlotList){
        List<TimeSlot> selectedTimeSlots = new ArrayList<>();
        for (TimeSlot timeSlot:timeSlotList){
            if (timeslotStatusEquals(timeSlot, context.getString(R.string.timeslot_status_accept))){
                selectedTimeSlots.add(timeSlot);
            }
        }
        return selectedTimeSlots;
    }

    public static boolean timeslotStatusEquals(TimeSlot timeSlot, String status){
        if (timeSlot.getStatus().equals(status) || timeSlot.getStatus() == status){
            return true;
        }
        return false;
    }

    public static boolean chooseAtLeastOnTimeSlot(Context context, List<TimeSlot> timeSlots){
        for (TimeSlot timeSlot: timeSlots){
            if (isTimeSlotSelected(context, timeSlot)){
                return true;
            }
        }
        return false;
    }

    public static TimeSlot getTimeSlot(Event event, TimeSlot timeSlot){
        for (TimeSlot eventTimeSlot : event.getTimeslots()){
            if(eventTimeSlot.getTimeSlotUid() == timeSlot.getTimeSlotUid()){
                return eventTimeSlot;
            }
        }
        return null;
    }

}
