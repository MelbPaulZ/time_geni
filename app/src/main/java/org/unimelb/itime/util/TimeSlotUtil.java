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
    public static boolean isTimeSlotSelected(Context context, Event event , int position){
        Timeslot timeSlot = event.getTimeslot().get(position);

        if(event.getHostUserUid().equals(UserUtil.getInstance(context).getUserUid())){
            if (timeSlot.getIsConfirmed()==0){
                return false;
            }else{
                return true;
            }
        }else{
            if (timeSlot.getStatus().equals(Timeslot.STATUS_PENDING)){
                return false;
            }else if (timeSlot.getStatus().equals(Timeslot.STATUS_ACCEPTED)){
                return true;
            }else{ // this might change later
                return false;
            }
        }
    }


    //get all selected timeslots
    public static List<Timeslot> getSelectedTimeSlots(Context context, List<Timeslot> timeSlotList){
        List<Timeslot> selectedTimeSlots = new ArrayList<>();
        for (Timeslot timeSlot:timeSlotList){
            if (timeSlot.getIsConfirmed()==1){
                selectedTimeSlots.add(timeSlot);
            }
        }
        return selectedTimeSlots;
    }


    // get all selected timeslot in create timeslot view
    public static List<Timeslot> getPendingTimeSlots(Context context, List<Timeslot> timeSlotList){
        List<Timeslot> selectedTimeSlots = new ArrayList<>();
        for (Timeslot timeSlot:timeSlotList){
            if (timeslotStatusEquals(timeSlot, Timeslot.STATUS_PENDING)){
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

    public static boolean chooseAtLeastOnTimeSlot(Context context, Event event){
        List<Timeslot> timeSlots = event.getTimeslot();
        for (Timeslot timeSlot: timeSlots){
            if (isTimeSlotSelected(context, event, timeSlots.indexOf(timeSlot))){
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

    public static Timeslot getLatestTimeSlot(List<Timeslot> timeslotList){
        if (timeslotList!=null && timeslotList.size()>0){
            Timeslot latestTimeSlot = timeslotList.get(0);
            for (Timeslot timeslot : timeslotList){
                if (timeslot.getStartTime() < latestTimeSlot.getStartTime()){
                    latestTimeSlot = timeslot;
                }
            }
            return latestTimeSlot;
        }
        return null;
    }

}
