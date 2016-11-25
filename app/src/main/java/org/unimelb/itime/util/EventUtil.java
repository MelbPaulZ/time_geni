package org.unimelb.itime.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.util.rulefactory.FrequencyEnum;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul on 8/09/2016.
 */
public class EventUtil{
    public static final int ACTIVITY_EDIT_EVENT = 1;
    public static final int ACTIVITY_CREATE_EVENT = 2;
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public final static int REPEAT_NEVER = 0;
    public final static int REPEAT_EVERYDAY = 1;
    public final static int REPEAT_EVERYWEEK = 2;
    public final static int REPEAT_EVERY_TWOWEEKS = 3;
    public final static int REPEAT_EVERY_MONTH = 4;
    public final static int REPEAT_EVERY_YEAR = 5;

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
        if (time == 0) {
            return context.getString(R.string.repeat_never);
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String month = getMonth(context, calendar.get(Calendar.MONTH));
            return dayOfWeek + " ," + month + " " + day;
        }
    }

    public static String getAttendeeString(Context context,ArrayList<String> attendeesArrayList) {
        ArrayList<String> arrayList = attendeesArrayList;
        if (attendeesArrayList==null){
            return "solo event";
        }
        // attendees arraylist = 0 means no attendee selected, attendees arraylist = 1 means only it self, need to change later!!
        if ( attendeesArrayList.size()==0) {
            return context.getString(R.string.none);
        }else if (attendeesArrayList.size()==1){
            return attendeesArrayList.get(0);
        }
        else {
            return String.format("%s and %d more", attendeesArrayList.get(0), attendeesArrayList.size() - 1);
        }
    }

    public static ArrayList<String> fromContactListToArrayList(ArrayList<ITimeContactInterface> contactList){
        ArrayList<String> arrayList = new ArrayList<>();
        for (ITimeContactInterface contact: contactList){
            arrayList.add(contact.getName());
        }
        return arrayList;
    }

    public static ArrayList<String> fromInviteeListToArraylist(List<Invitee> inviteeArrayList){
        ArrayList<String> arrayList = new ArrayList<>();
        if (inviteeArrayList!=null) {
            for (Invitee invitee : inviteeArrayList) {
                arrayList.add(invitee.getName());
            }
        }
        return arrayList;
    }

    public static CharSequence[] getAlertTimes(Context context){
        CharSequence[] alertTimes = new CharSequence[]{
                context.getString(R.string.none),
                context.getString(R.string.five_mintues_before),
                context.getString(R.string.fifteen_minutes_before),
                context.getString(R.string.thirty_minutes_before),
                context.getString(R.string.one_hour_before),
                context.getString(R.string.two_hours_before),
                context.getString(R.string.one_day_before),
                context.getString(R.string.two_days_before),
                context.getString(R.string.one_week_before)
        };
                return alertTimes;
    }

    public static String getAlertTimeFromIndex(Context context,int index){
        return (String) getAlertTimes(context)[index];
    }


    public static String getSuggestTimeStringFromLong(Context context, Long startTime,Long endtime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE)<10? "0" + String.valueOf(calendar.get(Calendar.MINUTE)) : String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endtime);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = endCalendar.get(Calendar.MINUTE)<10? "0" + String.valueOf(endCalendar.get(Calendar.MINUTE)) : String.valueOf(endCalendar.get(Calendar.MINUTE));
        String endAmOrPm = endCalendar.get(Calendar.HOUR_OF_DAY) >=12? "PM": "AM";

        return dayOfWeek + " " + day + "/" + month + " " + startTimeHour + ":" + startMinute +
                " " + startAmOrPm + " - " + endTimeHour + ":" + endTimeMinute + endAmOrPm;

    }


    public static int generateTimeSlotUid(){
        int uid = (int)(Math.random() * 1000000);
        return uid;
    }

    public static ArrayList<PhotoUrl> fromStringToPhotoUrlList(ArrayList<String> urls){
        ArrayList<PhotoUrl> arrayList = new ArrayList<>();
        for (String url: urls){
            // here should update photoUrl, as Chuandong Request
            PhotoUrl photoUrl = new PhotoUrl();
            photoUrl.setLocalPath(url);
            photoUrl.setFilename(getPhotoFileName(url));
            photoUrl.setSuccess(0);
            photoUrl.setPhotoUid(AppUtil.generateUuid());
            photoUrl.setEventUid(EventManager.getInstance().getCurrentEvent().getEventUid());
            arrayList.add(photoUrl);
        }
        return arrayList;
    }

    public static String getPhotoFileName(String url){
        File f = new File(url);
        String name = f.getName();
        return name;
    }

    public static Event getEventInDB(Context context,String eventUid){
        Event event = DBManager.getInstance(context).getEvent(eventUid);
        event.getInvitee();
        event.getTimeslot();
        return event;
    }

    public static CharSequence[] getCalendarTypes(Context context){
        return new CharSequence[]{"Work", "Private", "Group", "Public"};
    }

    public static String getCalendarTypeFromIndex(Context context, String index){
        if (index==null){
            return null;
        }
        int num = Integer.parseInt(index);
        return (String) getCalendarTypes(context)[num];
    }

    public static CharSequence[] getRepeats(Context context, Event event){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        String dayOfWeek = EventUtil.getDayOfWeekFull(context, calendar.get(Calendar.DAY_OF_WEEK));
        return new CharSequence[]{
                context.getString(R.string.repeat_never),
                context.getString(R.string.repeat_everyday),
                String.format(context.getString(R.string.repeat_everyweek), dayOfWeek),
                String.format(context.getString(R.string.repeat_every_twoweek)),
                String.format(context.getString(R.string.repeat_every_month)),
                String.format(context.getString(R.string.repeat_every_year))};
    }

    public static void changeEventFrequency(Event event, int repeatIndex){
        if (repeatIndex == REPEAT_NEVER){
            event.getRule().setFrequencyEnum(null);
        }else if (repeatIndex == REPEAT_EVERYDAY){
            event.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
        }else if (repeatIndex == REPEAT_EVERYWEEK){
            event.getRule().setFrequencyEnum(FrequencyEnum.WEEKLY);
        }else if (repeatIndex == REPEAT_EVERY_TWOWEEKS){
            event.getRule().setFrequencyEnum(FrequencyEnum.WEEKLY);
            event.getRule().setInterval(2);
        }else if (repeatIndex == REPEAT_EVERY_MONTH){
            event.getRule().setFrequencyEnum(FrequencyEnum.MONTHLY);
        }else if (repeatIndex == REPEAT_EVERY_YEAR){
            event.getRule().setFrequencyEnum(FrequencyEnum.YEARLY);
        }
    }



//    public static String parseRepeatIdToRepeat(Context context,String repeat, long startTime){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(startTime);
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
////        return "every day( change later )";
//        if (context==null){
//            return null;
//        }
//        if (repeat==null){
//            return context.getString(R.string.repeat_never);
//        }
//        switch (repeat){
//            case "0":
//                return context.getString(R.string.repeat_never);
//            case "1":
//                return context.getString(R.string.repeat_everyday);
//            case "2":
//                return String.format(context.getString(R.string.repeat_everyweek), getDayOfWeekFull(context,dayOfWeek));
//            case "3":
//                return context.getString(R.string.repeat_every_twoweek);
//            case "4":
//                return context.getString(R.string.repeat_every_month);
//            case "5":
//                return context.getString(R.string.repeat_every_year);
//            default:
//                return context.getString(R.string.repeat_never);
//        }
//    }

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

    public static ArrayList<String> getDurationTimes(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("15 mins");
        list.add("30 mins");
        list.add("45 mins");
        list.add("1 hour");
        list.add("2 hours");
        list.add("3 hours");
        list.add("4 hours");
        list.add("5 hours");
        list.add("6 hours");
        list.add("12 hours");
        list.add("24 hours");
        return list;
    }

    public static int getDurationInMintues(int position){
        switch (position){
            case 0:
                return 15;
            case 1:
                return 30;
            case 2:
                return 45;
            case 3:
                return 60;
            case 4:
                return 120;
            case 5:
                return 180;
            case 6:
                return 240;
            case 7:
                return 300;
            case 8:
                return 360;
            case 9: return 720;
            case 10: return 1440;
        }
        return 0;
    }


    public static void startEditEventActivity(Context context, Activity activity, ITimeEventInterface iTimeEventInterface) {
        Intent intent = new Intent(activity, EventDetailActivity.class);
        Event event = (Event)iTimeEventInterface;
        event.getInvitee();
        EventManager.getInstance().setCurrentEvent((Event)iTimeEventInterface);
        activity.startActivityForResult(intent, ACTIVITY_EDIT_EVENT);
    }

    public static String getEventConfirmStatus(Event event){
        switch (event.getDisplayStatus()){
            case 0:
                return "has not confirmed this event";
            case 1:
                return "has updating this event";
            case 2:
                return "has confirmed this event";
            case 3:
                return "has cancelled this event";
            default:
                return "has confirmed this event";
        }
    }

    public static String getHostName(Event event){
        // need to change later
        String hostUid = event.getHostUserUid();
        return "Captain America";
    }

    /** This get Repeat String methods return the message that should be displayed on screen
     * */
    public static String getRepeatString(Context context, Event event){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        String dayOfWeek = EventUtil.getDayOfWeekFull(context, calendar.get(Calendar.DAY_OF_WEEK));
        FrequencyEnum frequencyEnum = event.getRule().getFrequencyEnum();
        if (frequencyEnum == null){
            return String.format(context.getString(R.string.repeat_never));
        }else if (frequencyEnum == FrequencyEnum.DAILY){
            return String.format(context.getString(R.string.repeat_everyday));
        }else if (frequencyEnum == FrequencyEnum.WEEKLY){
            if (event.getRule().getInterval() == 1){
                return String.format(context.getString(R.string.repeat_everyweek), dayOfWeek);
            }else if (event.getRule().getInterval() ==2){
                return String.format(context.getString(R.string.repeat_every_twoweek));
            }
        }else if (frequencyEnum == FrequencyEnum.MONTHLY){
            return String.format(context.getString(R.string.repeat_every_month));
        }else if (frequencyEnum == FrequencyEnum.YEARLY){
            return String.format(context.getString(R.string.repeat_every_year));
        }

        // if not all of this above (impossible)
        return "";
    }


    public static int parseEventType(String type){
        Map<String, Integer> map = new HashMap<>();
        map.put("solo", 0);
        map.put("group",1);
        map.put("public",2);
        if (!map.containsKey(type)){
            return 0;
        }
        return map.get(type);
    }

    public static int parseEventStatus(String Status){
        Map<String, Integer> map = new HashMap<>();
        map.put("pending", 0);
        map.put("updating",1);
        map.put("confirmed",2);
        map.put("cancelled",3);

        if (!map.containsKey(Status)){
            return 0;
        }
        return map.get(Status);
    }

    public static boolean isEventRepeat(Event event){
        if (event.hasRecurrence() && event.getRecurrence().equals("0")){
            return false;
        }else if(!event.hasRecurrence()){
            return false;
        }else{
            return true;
        }
    }

    public static List<String> getYears(){
        List<String> years = new ArrayList<>();
        for (int i = 2010 ; i<2030 ; i++){
            years.add(i+"");
        }
        return years;
    }

    public static List<String> getMonths(){
        List<String> months = new ArrayList<>();
        for(int i = 1 ; i <= 12 ; i ++){
            months.add(i + "");
        }
        return months;
    }

    public static List<String> getDays(int month){
        List<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1 ; i<= numOfDays ; i++){
            days.add(i + "");
        }
        return days;
    }

    public static List<String> getHours(){
        List<String> hours = new ArrayList<>();
        for( int i = 0 ; i<24; i ++){
            hours.add( i + "");
        }
        return hours;
    }

    public static List<String> getMinutes(){
        List<String> minutes = new ArrayList<>();
        for (int i = 0 ; i < 4 ; i ++){
            minutes.add( i*15 + "");
        }
        return minutes;
    }

    public static String getEventType(Event event, String userUid){
        if (event.getInvitee().size()>1){
            return "group";
        }else{
            if (event.getInvitee().size()==0){
                return "solo";
            }else{
                return "group";
            }
        }
    }

    /** use when event has no invitees before, add self as host with a new generated inviteeUid
     * */
    public static void addSelfInInvitee(Context context,Event event){
        Invitee self = new Invitee();
        self.setStatus(context.getString(R.string.accept));
        if (EventManager.getInstance().getHostInviteeUid(EventManager.getInstance().getCurrentEvent())!=null){
            self.setInviteeUid(EventManager.getInstance().getHostInviteeUid(EventManager.getInstance().getCurrentEvent()));
        }else{
            self.setInviteeUid(AppUtil.generateUuid());
        }
        self.setUserUid(UserUtil.getUserUid());
        self.setEventUid(event.getEventUid());
        self.setUserId(UserUtil.getInstance().getUser().getUserId());
        self.setIsHost(1); // 1 refers to host
        self.setAliasName(UserUtil.getInstance().getUser().getPersonalAlias());
        event.addInvitee(self);
    }


    public static void addSoloEventBasicInfo(Context context,Event event){
        event.setCalendarUid(CalendarUtil.getInstance().getCalendar().get(0).getCalendarUid());
        event.setStatus(context.getString(R.string.confirmed));
        event.setEventUid(AppUtil.generateUuid());
        event.setEventId(""); // might need change later, ask chuandong what is eventId
        event.setUserUid(UserUtil.getUserUid());
        event.setEventType(context.getString(R.string.solo));
        event.setInviteeVisibility(1);
        event.setFreebusyAccess(1); // ask chuandong
    }




}
