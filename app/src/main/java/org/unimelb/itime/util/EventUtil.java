package org.unimelb.itime.util;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.ITimeComparable;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.SlotResponse;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.util.rulefactory.FrequencyEnum;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Paul on 8/09/2016.
 */
public class EventUtil {
    public static final int ACTIVITY_EDIT_EVENT = 1;
    public static final int ACTIVITY_CREATE_EVENT = 2;
    public static double latitude;
    public static double longitude;

    public final static int REPEAT_NEVER = 0;
    public final static int REPEAT_EVERYDAY = 1;
    public final static int REPEAT_EVERYWEEK = 2;
    public final static int REPEAT_EVERY_TWOWEEKS = 3;
    public final static int REPEAT_EVERY_MONTH = 4;
    public final static int REPEAT_EVERY_YEAR = 5;
    public final static int REPEAT_CUSTOM = 6;
    public final static long allDayMilliseconds = 24 * 60 * 60 * 1000;


    public static String parseTimeToString(Context context, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = getMonth(context, calendar.get(Calendar.MONTH));
        String startTimeHour = calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE) < 10 ? "0" + String.valueOf(calendar.get(Calendar.MINUTE)) : String.valueOf(calendar.get(Calendar.MINUTE));
        return month + " " + day + ", " + calendar.get(Calendar.YEAR) + "   " + startTimeHour + ":" + startMinute;
    }


    public static String getRepeatEndString(Context context, Event event){
        Calendar calendar = Calendar.getInstance();
        if (event.getRule().getUntil()!=null) {
            calendar.setTime(event.getRule().getUntil());
            String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            String month = getMonth(context, calendar.get(Calendar.MONTH));
            String year = calendar.get(Calendar.YEAR) + "";
            return dayOfWeek + " ," + month + " " + day + ", " + year;
        }else{
            return context.getResources().getString(R.string.event_default_end_repeate);
        }
    }

    public static String getAttendeeString(Context context, ArrayList<String> attendeesArrayList) {
        ArrayList<String> arrayList = attendeesArrayList;
        if (attendeesArrayList == null) {
            return "solo event";
        }
        // attendees arraylist = 0 means no attendee selected, attendees arraylist = 1 means only it self, need to change later!!
        if (attendeesArrayList.size() == 0) {
            return context.getString(R.string.none);
        } else if (attendeesArrayList.size() == 1) {
            return attendeesArrayList.get(0);
        } else {
            return String.format("%s and %d more", attendeesArrayList.get(0), attendeesArrayList.size() - 1);
        }
    }

    public static ArrayList<String> fromInviteeListToArraylist(List<Invitee> inviteeArrayList) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (inviteeArrayList != null) {
            for (Invitee invitee : inviteeArrayList) {
                arrayList.add(invitee.getName());
            }
        }
        return arrayList;
    }

    public static CharSequence[] getAlertTimes(Context context) {
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

    public static String getAlertTimeFromIndex(Context context, int index) {
        return (String) getAlertTimes(context)[index];
    }

    public static String getSelectTimeStringFromLong(Context context, long time){
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String t = df.format(calendar.getTime());
        return t;
    }

    public static String getSelectDateStringFromLong(Context context, long time){
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String d = df.format(calendar.getTime());
        return d;
    }

    public static String getSuggestTimeStringFromLong(Context context, Long startTime, Long endtime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek =  calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, context.getResources().getConfiguration().locale);
        dayOfWeek = dayOfWeek.toUpperCase();
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d",calendar.get(Calendar.MONTH) + 1);
        String startTimeStr = df.format(calendar.getTime());

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endtime);
        String endTimeStr = df.format(endCalendar.getTime());

        return dayOfWeek + " " + day + "/" + month + " " + startTimeStr + " - " + endTimeStr;
    }

    public static List<String> getTimeslotViewResponseFromLong(Context context, Long startTime, Long endtime) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek =  calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, context.getResources().getConfiguration().locale);
        dayOfWeek = dayOfWeek.toUpperCase();
        String day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
        String year = String.format("%04d", calendar.get(Calendar.YEAR));
        String startTimeStr = df.format(calendar.getTime());
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endtime);
        String endTimeStr = df.format(endCalendar.getTime());
        String endAmOrPm = endCalendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        List<String> times = new ArrayList<String>();
        times.add(startTimeStr + " " + startAmOrPm + " - " + endTimeStr + " " + endAmOrPm);
        times.add(dayOfWeek + " " + year);
        return times;
    }

    public static String getSlotStringFromLong(Context context, Long startTime, Long endtime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String startTimeHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String startMinute = calendar.get(Calendar.MINUTE) < 10 ? "0" + String.valueOf(calendar.get(Calendar.MINUTE)) : String.valueOf(calendar.get(Calendar.MINUTE));
        String startAmOrPm = calendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endtime);
        String endTimeHour = String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY));
        String endTimeMinute = endCalendar.get(Calendar.MINUTE) < 10 ? "0" + String.valueOf(endCalendar.get(Calendar.MINUTE)) : String.valueOf(endCalendar.get(Calendar.MINUTE));
        String endAmOrPm = endCalendar.get(Calendar.HOUR_OF_DAY) >= 12 ? "PM" : "AM";

        return startTimeHour + ":" + startMinute +
                " " + startAmOrPm + " - " + endTimeHour + ":" + endTimeMinute + endAmOrPm;

    }

    public static String getDayOfWeekString(Context context, Long startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        String dayOfWeek = getDayOfWeekAbbr(context, calendar.get(Calendar.DAY_OF_WEEK));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);

        return dayOfWeek + " " + day + "/" + month;
    }



    public static ArrayList<PhotoUrl> fromStringToPhotoUrlList(Context context, ArrayList<String> urls) {
        ArrayList<PhotoUrl> arrayList = new ArrayList<>();
        for (String url : urls) {
            // here should update photoUrl, as Chuandong Request
            PhotoUrl photoUrl = new PhotoUrl();
            photoUrl.setLocalPath(url);
            photoUrl.setFilename(getPhotoFileName(url));
            photoUrl.setSuccess(0);
            photoUrl.setPhotoUid(AppUtil.generateUuid());
            photoUrl.setEventUid(EventManager.getInstance(context).getCurrentEvent().getEventUid());
            arrayList.add(photoUrl);
        }
        return arrayList;
    }

    public static PhotoUrl fromStringToPhotoUrl(Context context, String url) {
            // here should update photoUrl, as Chuandong Request
        PhotoUrl photoUrl = new PhotoUrl();
        photoUrl.setLocalPath(url);
        photoUrl.setFilename(getPhotoFileName(url));
        photoUrl.setSuccess(0);
        photoUrl.setPhotoUid(AppUtil.generateUuid());
        photoUrl.setEventUid(EventManager.getInstance(context).getCurrentEvent().getEventUid());
        return photoUrl;
    }

    private static String getPhotoFileName(String url) {
        File f = new File(url);
        String name = f.getName();
        return name;
    }


    public static CharSequence[] getCalendarTypes(Context context) {
        ArrayList<String> calendarNames = new ArrayList<>();
        for (org.unimelb.itime.bean.Calendar calendar : CalendarUtil.getInstance(context).getCalendar()){
            calendarNames.add(calendar.getSummary());
        }
        return calendarNames.toArray(new CharSequence[calendarNames.size()]);
    }


    public static CharSequence[] getRepeats(Context context, Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        String dayOfWeek = EventUtil.getDayOfWeekFull(context, calendar.get(Calendar.DAY_OF_WEEK));
        return new CharSequence[]{
                context.getString(R.string.repeat_never),
                context.getString(R.string.repeat_everyday),
                String.format(context.getString(R.string.repeat_everyweek), dayOfWeek),
                String.format(context.getString(R.string.repeat_every_twoweek)),
                String.format(context.getString(R.string.repeat_every_month)),
                String.format(context.getString(R.string.repeat_every_year)),
                String.format(context.getString(R.string.repeat_custom))};
    }

    public static void changeEventFrequency(Event event, int repeatIndex) {
        if (repeatIndex == REPEAT_NEVER) {
            event.getRule().setFrequencyEnum(null);
            event.getRule().setInterval(1);
        } else if (repeatIndex == REPEAT_EVERYDAY) {
            event.getRule().setFrequencyEnum(FrequencyEnum.DAILY);
            event.getRule().setInterval(1);
        } else if (repeatIndex == REPEAT_EVERYWEEK) {
            event.getRule().setFrequencyEnum(FrequencyEnum.WEEKLY);
            event.getRule().setInterval(1);
        } else if (repeatIndex == REPEAT_EVERY_TWOWEEKS) {
            event.getRule().setFrequencyEnum(FrequencyEnum.WEEKLY);
            event.getRule().setInterval(2);
        } else if (repeatIndex == REPEAT_EVERY_MONTH) {
            event.getRule().setFrequencyEnum(FrequencyEnum.MONTHLY);
            event.getRule().setInterval(1);
        } else if (repeatIndex == REPEAT_EVERY_YEAR) {
            event.getRule().setFrequencyEnum(FrequencyEnum.YEARLY);
            event.getRule().setInterval(1);
        } else if(repeatIndex == REPEAT_CUSTOM){
            // TODO: 15/01/2017 custom

        }
        event.setRecurrence(event.getRule().getRecurrence());
    }


    public static String getDayOfWeekFull(Context context, int dayOfWeek) {
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


    private static String getDayOfWeekAbbr(Context context, int dayOfWeek) {
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

    public static String getMonth(Context context, int month) {
        switch (month) {
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

    public static ArrayList<String> getDurationTimes() {
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

    public static int getDurationInMintues(int position) {
        int[] arr = {15, 30, 45, 60, 120, 180, 240, 300, 360, 720, 1440};
        return arr[position];
    }

    public static String getEventConfirmStatus(Context context, Event event) {

        if (isUserHostOfEvent(context, event)) {
            if (event.getStatus().equals(Event.STATUS_PENDING)) {
                return context.getString(R.string.You_have_not_confirmed_this_event);
            } else if (event.getStatus().equals(Event.STATUS_CONFIRMED)) {
                return context.getString(R.string.You_have_confirmed_this_event);
            } else {
                return "todo:" + event.getStatus();
            }
        } else {
            if (event.getStatus().equals(Event.STATUS_PENDING)) {
                return context.getString(R.string.has_not_confirmed_this_event);
            } else if (event.getStatus().equals(Event.STATUS_CONFIRMED)) {
                return context.getString(R.string.has_confirmed_this_event);
            } else {
                return "todo:" + event.getStatus();
            }
        }
    }

    public static String getHostName(Event event) {
        // need to change later
        String hostUid = event.getHostUserUid();
        for (Invitee invitee : event.getInvitee()) {
            if (invitee.getUserUid().equals(hostUid)) {
                return invitee.getAliasName();
            }
        }
        return "not found this person";
    }

    /**
     * This get Repeat String methods return the message that should be displayed on screen
     */
    public static String getRepeatString(Context context, Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getStartTime());
        String dayOfWeek = EventUtil.getDayOfWeekFull(context, calendar.get(Calendar.DAY_OF_WEEK));
        FrequencyEnum frequencyEnum = event.getRule().getFrequencyEnum();
        int interval = event.getRule().getInterval();

        // for event detail and edit event, the frequencyEnum will be null,
        // but the recurrence is not null, so need to get the frequenceEnum from the recurrence
        if (frequencyEnum==null){
            RuleModel ruleModel = RuleFactory.getInstance().getRuleModel(event);
            event.setRule(ruleModel);
            frequencyEnum = ruleModel.getFrequencyEnum();
        }

        // when view event details, the fraquencyEnum will be null
        if (frequencyEnum == null){
            return "None";
        }

        switch (frequencyEnum){
            case DAILY:
                return String.format(context.getString(R.string.repeat_everyday_cus),interval==1?"":" "+interval+" ");
            case WEEKLY:
                return String.format(context.getString(R.string.repeat_everyweek_cus),interval==1?" ":" "+interval+" ",dayOfWeek);
            case MONTHLY:
                return String.format(context.getString(R.string.repeat_every_month_cus),interval==1?" ":" "+interval+" ");
            case YEARLY:
                return String.format(context.getString(R.string.repeat_every_year_cus),interval==1?" ":" "+interval+" ");
            default:
                return String.format(context.getString(R.string.repeat_never));
        }
    }


    public static int parseEventType(String type) {
        Map<String, Integer> map = new HashMap<>();
        map.put("solo", 0);
        map.put("group", 1);
        map.put("public", 2);
        if (!map.containsKey(type)) {
            return 0;
        }
        return map.get(type);
    }

    public static int parseEventStatus(String Status) {
        Map<String, Integer> map = new HashMap<>();
        map.put("pending", 0);
        map.put("updating", 1);
        map.put("confirmed", 2);
        map.put("cancelled", 3);

        if (!map.containsKey(Status)) {
            return 0;
        }
        return map.get(Status);
    }

    public static boolean isEventRepeat(Event event) {
        if (event.getRecurrence().length>0) {
            return true;
        } else {
            return false;
        }
    }

    public static List<String> getYears() {
        List<String> years = new ArrayList<>();
        for (int i = 2010; i < 2030; i++) {
            years.add(i + "");
        }
        return years;
    }

    public static List<String> getMonths() {
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(i + "");
        }
        return months;
    }

    public static List<String> getDays(int month) {
        List<String> days = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        int numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= numOfDays; i++) {
            days.add(i + "");
        }
        return days;
    }

    public static List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i + "");
        }
        return hours;
    }

    public static List<String> getMinutes() {
        List<String> minutes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            minutes.add(i * 15 + "");
        }
        return minutes;
    }

    public static String getEventType(Event event, String userUid) {
        if (event.getInvitee().size() > 1) {
            return "group";
        } else {
            return "solo";
        }
    }

    /**
     * use when event has no invitees before, add self as host with a new generated inviteeUid
     */
    public static void addSelfInInvitee(Context context, Event event) {
        if (!isSelfInEvent(context, event)) {
            Invitee self = new Invitee();
            self.setStatus(Invitee.STATUS_ACCEPTED);
            String inviteeUid = EventManager.getInstance(context).getHostInviteeUid(EventManager.getInstance(context).getCurrentEvent());
            Log.i("inviteeUid", "addSelfInInvitee: " + inviteeUid);
            self.setInviteeUid(inviteeUid!=null ? inviteeUid : AppUtil.generateUuid());
//            if (EventManager.getInstance(context).getHostInviteeUid(EventManager.getInstance(context).getCurrentEvent()) != null) {
//                self.setInviteeUid(
//                        EventManager.getInstance(context).getHostInviteeUid(EventManager.getInstance(context).getCurrentEvent()));
//            } else {
//                self.setInviteeUid(AppUtil.generateUuid());
//            }
            self.setUserUid(UserUtil.getInstance(context).getUserUid());
            self.setEventUid(event.getEventUid());
            self.setUserId(UserUtil.getInstance(context).getUser().getUserId());
            self.setIsHost(1); // 1 refers to host
            self.setAliasName(UserUtil.getInstance(context).getUser().getPersonalAlias());
            self.setAliasPhoto(UserUtil.getInstance(context).getUser().getPhoto());
            event.addInvitee(self);
        }
    }

    private static boolean isSelfInEvent(Context context, Event event) {
        String selfUserUid = UserUtil.getInstance(context).getUserUid();
        for (Invitee invitee : event.getInvitee()) {
            if (invitee.getUserUid().equals(selfUserUid)) {
                return true;
            }
        }
        return false;
    }

    public static Calendar getBeginOfDayCalendar(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cal.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getDayBeginMilliseconds(long startTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        return calendar.getTimeInMillis();
    }

    public static long getDayEndMilliseconds(long endTime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);

        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    public static Calendar parseTimeStringToCalendar(String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar updateTimeCalendar = Calendar.getInstance();
        updateTimeCalendar.setTime(d);
        return updateTimeCalendar;
    }

    /**
     *
     * @param ctx
     * @param todayM
     * @param comparedTime
     * @return
     */
    public static String parseRelativeTime(Context ctx, Calendar todayM, Calendar comparedTime){
        Calendar todayMCalendar = EventUtil.getBeginOfDayCalendar(todayM);
        Calendar currentDayMCalendar = EventUtil.getBeginOfDayCalendar(comparedTime);

        long delta = currentDayMCalendar.getTimeInMillis() - todayMCalendar.getTimeInMillis();
        long oneDay = 24 * 60 * 60 * 1000;
        if (delta==0){
            return String.format("%1$tH:%1$tM",comparedTime);
        }else if (delta == oneDay){
            return ctx.getString(R.string.Yesterday);
        }else{
            return String.format("%1$td/%1$tm/%1$tY", comparedTime);
        }
    }

    /**
     *
     * @param ctx
     * @param timeString
     * @return
     */
    public static String parseRelativeTime(Context ctx, String timeString){
        Calendar updateTimeCalendar = EventUtil.parseTimeStringToCalendar(timeString);
        Calendar now = Calendar.getInstance();
        return parseRelativeTime(ctx, now, updateTimeCalendar);
    }

    public static boolean isUserHostOfEvent(Context context, Event event) {
        return event.getHostUserUid().equals(UserUtil.getInstance(context).getUserUid());
    }

    public static CharSequence[] getRepeatEventChangeOptions(Context context) {
        CharSequence[] sequences = new CharSequence[3];
        sequences[0] = context.getString(R.string.Save_for_this_event_only);
        sequences[1] = context.getString(R.string.Save_for_future_events);
        sequences[2] = context.getString(R.string.cancel);
        return sequences;
    }

    public static Invitee getSelfInInvitees(Context context, Event event) {
        for (Invitee invitee : event.getInvitee()) {
            if (invitee.getUserUid().equals(UserUtil.getInstance(context).getUserUid())) {
                return invitee;
            }
        }
        return null;
    }

    public static List<Invitee> removeSelfInInvitees(Context context, List<Invitee> invitees){
        if(context == null){
            return null;
        }
        List<Invitee> rst = new ArrayList<>();
        for(Invitee invitee: invitees){
            if (!invitee.getUserUid().equals(UserUtil.getInstance(context).getUserUid())){
                rst.add(invitee);
            }
        }
        return rst;
    }

    public static <T extends Transformation> void bindUrlHelper(Context context, String url, ImageView view, T transformer) {
        if (url != null && !url.equals("")) {
            Picasso.with(context).load(url).transform(transformer).into(view);
        } else {
            Picasso.with(context).load(org.unimelb.itime.vendor.R.drawable.invitee_selected_default_picture).transform(transformer).into(view);
        }
    }

    public static void bindUrlHelper(Context context, int res, ImageView view) {
        Picasso.with(context).load(res).into(view);
    }

    public static String getHostPhotoUrl(Event event){
        for(Invitee invitee :event.getInvitee() ){
            if (invitee.getUserUid().equals(event.getHostUserUid())){
                return invitee.getAliasPhoto();
            }
        }
        return null;
    }

    /**
     *
     * @param event
     * @return
     * String: timeslot Uid, List<StatusKeyStruct>: get(status),get(invitees)
     */
    public static Map<String, List<StatusKeyStruct>> getAdapterData(Event event){
        List<Invitee> invitees = event.getInvitee();
        List<Timeslot> timeSlots = event.getTimeslot();
        //slo Uid -- List (status - List invitee)
        Map<String,List<StatusKeyStruct>> results = new HashMap<>();

        for (Timeslot slot: timeSlots
                ) {
            List<StatusKeyStruct> structs = new ArrayList<>();
            StatusKeyStruct acp_st = new StatusKeyStruct(Timeslot.STATUS_ACCEPTED);
            structs.add(acp_st);

            StatusKeyStruct rejected_st = new StatusKeyStruct(Timeslot.STATUS_REJECTED);
            structs.add(rejected_st);

            StatusKeyStruct pending_st = new StatusKeyStruct(Timeslot.STATUS_PENDING);
            structs.add(pending_st);

            results.put(slot.getTimeslotUid(),structs);
        }

        for (Invitee invitee:invitees
                ) {
            List<SlotResponse> responses = invitee.getSlotResponses();

            for (SlotResponse response: responses
                    ) {
                List<StatusKeyStruct> structs = results.get(response.getTimeslotUid());
                for (int i = 0; i < structs.size(); i++) {
                    if (structs.get(i).getStatus().equals(response.getStatus())){
                        structs.get(i).addInvitee(invitee);
                        break;
                    }
                }

            }
        }

        return results;
    }

    public static class StatusKeyStruct{
        String status;
        //status is key
        List<Invitee> response = new ArrayList<>();

        public StatusKeyStruct(String status) {
            this.status = status;
        }

        public String getStatus(){
            return this.status;
        }

        public void addInvitee(Invitee invitee){
            response.add(invitee);
        }

        public List<Invitee> getInviteeList(){
            return this.response;
        }
    }


    public static boolean isSameDay(long t1, long t2){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(t1);
        cal2.setTimeInMillis(t2);
        boolean sameDay = (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) &&
                (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));

        return sameDay;
    }

    public static int getDayDifferent(long oldL,long newL){
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(oldL);
        Calendar c1Begin = getBeginOfDayCalendar(c1);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(newL);
        Calendar c2Begin = getBeginOfDayCalendar(c2);

        long oneDay = 24 * 60 * 60 * 1000;
        long delta = c2Begin.getTimeInMillis() - c1Begin.getTimeInMillis();
        int dif = (int) (delta/oneDay);
        return dif;
    }

    public static boolean isGroupEvent(Context context, Event event){
        return event.getEventType().equals(Event.TYPE_GROUP);
    }

    public static boolean isEventConfirmed(Context context, Event event){
        return event.getStatus().equals(Event.STATUS_CONFIRMED);
    }


    public static boolean isInvitee(Context context, String status, Event event){
        Invitee me = getSelfInInvitees(context, event);
        if (me.getStatus().equals(status)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean hasOtherInviteeExceptSelf(Context context, Event event){
        for (Invitee invitee: event.getInvitee()){
            if (!invitee.getUserUid().equals(UserUtil.getInstance(context).getUserUid())){
                return true;
            }
        }
        return false;
    }

    public static List<Timeslot> getTimeslotFromStatus(Event event, String status){
        List<Timeslot> timeslots = new ArrayList<>();
        for (Timeslot timeslot: event.getTimeslot()){
            if (timeslot.getStatus().equals(status)){
                timeslots.add(timeslot);
            }
        }
        return timeslots;
    }

    public static List<Timeslot> getTimeslotFromPending(Context context, Event event){
       return getTimeslotFromStatus(event, Timeslot.STATUS_PENDING);
    }

    public static <T extends ITimeComparable> T getItemInList(List<T> list, T obj){
        for (T item:list
                ) {
            if (item.iTimeEquals(obj)){
                return item;
            }
        }

        return null;
    }

    public static <T extends ITimeComparable> void removeWhileLooping(ArrayList<T> list, T rmObj){
        Iterator<T> i = list.iterator();
        while (i.hasNext()) {
            T obj = i.next(); // must be called before you can call i.remove()
            if (obj.iTimeEquals(rmObj)){
                i.remove();
                break;
            }
        }
    }

    public static boolean isAllDayEvent(ITimeEventInterface event) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(event.getStartTime());
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        long duration = event.getEndTime() - event.getStartTime();
        boolean isAllDay = hour == 0
                && minutes == 0
                && duration >= (allDayMilliseconds * 0.9);

        return isAllDay;
    }



    /**
     *
     * @param event
     * @return
     */
    public static Event copyEvent(Event event){
        Gson gson = new Gson();

        String eventStr = gson.toJson(event);
        Event copyEvent = gson.fromJson(eventStr, Event.class);

        Type dataType = new TypeToken<RuleModel<Event>>() {}.getType();
        RuleModel response = gson.fromJson(gson.toJson(event.getRule(), dataType), dataType);
        copyEvent.setRule(response);

        return copyEvent;
    }


    public static String[] getRepeatFreqStr(){
        return new String[]{"Daily","Weekly","Monthly","Annually"};
    }

    public static String[] getRepeatIntervalStr(){
        return new String[] {"1","2","3","4","5","6","7","8","9","10"};
    }

    public static FrequencyEnum getFreqEnum(String code){
        final String[] values = getRepeatFreqStr();

        if (code.equals(values[0])){
            return FrequencyEnum.DAILY;
        }else if(code.equals(values[1])){
            return FrequencyEnum.WEEKLY;
        }else if(code.equals(values[2])){
            return FrequencyEnum.MONTHLY;
        }else if(code.equals(values[3])){
            return FrequencyEnum.YEARLY;
        }

        return null;
    }

    public static String getRepeatStrByFreq(FrequencyEnum frequencyEnum){
        final String[] values = getRepeatFreqStr();

        if (frequencyEnum == null){
            return values[0];
        }

        switch (frequencyEnum){
            case DAILY:
               return values[0];
            case WEEKLY:
               return values[1];
            case MONTHLY:
               return values[2];
            case YEARLY:
               return values[3];

        }

        return "UnKnow";
    }

    public static List<Invitee> getInviteeWithStatus(List<Invitee> invitees, String... status){
        List<Invitee> result = new ArrayList<>();
        for (Invitee invitee:invitees
             ) {
            for (String state:status
                 ) {
                if (invitee.getStatus().equals(state)){
                    result.add(invitee);
                    break;
                }
            }
        }

        return result;
    }

}
