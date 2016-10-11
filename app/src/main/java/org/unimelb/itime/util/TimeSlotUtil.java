package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 10/09/2016.
 */
public class TimeSlotUtil {

    // if this time slot has been selected, return true
    public static boolean isTimeSlotSelected(Context context, Timeslot timeSlot){
        if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_pending))){
            return false;
        }else if (timeSlot.getStatus().equals(context.getString(R.string.timeslot_status_accept))){
            return true;
        }else{ // this might change later
            return false;
        }
    }


    //get all selected timeslots
    public static List<Timeslot> getSelectedTimeSlots(Context context, List<Timeslot> timeSlotList){
        List<Timeslot> selectedTimeSlots = new ArrayList<>();
        for (Timeslot timeSlot:timeSlotList){
            if (timeslotStatusEquals(timeSlot, context.getString(R.string.timeslot_status_accept))){
                selectedTimeSlots.add(timeSlot);
            }
        }
        return selectedTimeSlots;
    }

    // get all selected timeslot in create timeslot view
    public static List<Timeslot> getPendingTimeSlots(Context context, List<Timeslot> timeSlotList){
        List<Timeslot> selectedTimeSlots = new ArrayList<>();
        for (Timeslot timeSlot:timeSlotList){
            if (timeslotStatusEquals(timeSlot, context.getString(R.string.timeslot_status_pending))){
                selectedTimeSlots.add(timeSlot);
            }
        }
        return selectedTimeSlots;
    }

    public static boolean timeslotStatusEquals(Timeslot timeSlot, String status){
        if (timeSlot.getStatus().equals(status) || timeSlot.getStatus() == status){
            return true;
        }
        return false;
    }

    public static boolean chooseAtLeastOnTimeSlot(Context context, List<Timeslot> timeSlots){
        for (Timeslot timeSlot: timeSlots){
            if (isTimeSlotSelected(context, timeSlot)){
                return true;
            }
        }
        return false;
    }

    public static Timeslot getTimeSlot(Event event, Timeslot timeSlot){
        for (Timeslot eventTimeSlot : event.getTimeslot()){
            if(eventTimeSlot.getTimeslotUid() == timeSlot.getTimeslotUid()){
                return eventTimeSlot;
            }
        }
        return null;
    }

}
