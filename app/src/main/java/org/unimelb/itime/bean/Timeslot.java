package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;

import java.io.Serializable;

/**
 * Created by Paul on 10/09/2016.
 */
public class Timeslot implements ITimeTimeSlotInterface,Serializable {
    public final static String STATUS_CREATING = "creating";
    public final static String STATUS_PENDING = "pending";
    public final static String STATUS_ACCEPTED = "accepted";
    public final static String STATUS_REJECTED = "rejected";

    private String timeslotUid = ""; //
    private String eventUid = ""; //
    private String userUid = "";
    private long startTime; //
    private long endTime; //
    private String status = ""; //
    private int acceptedNum; //
    private int totalNum; //
    private int rejectedNum; //
    private int pendingNum; //
    private int isConfirmed; //
    private int isSystemSuggested; //
    private String inviteeUid = "";

//    private transient boolean isSelected = false;
    private transient boolean isSelected = false;

/*"status": "pending",
          "rate": 1,
          "timeslotUid": "1",
          "eventUid": "1",
          "startTime": 1480225682797,
          "endTime": 1480229282797,
          "acceptedNum": 1,
          "rejectedNum": 0,
          "pendingNum": 2,
          "totalNum": 3,
          "isConfirmed": 0,
          "isSystemSuggested": 0,
          "inviteeUid": "2",
          "userUid": 2
    * */
    
    @Override
    public void setStartTime(long l) {
        this.startTime = l;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public void setEndTime(long l) {
        this.endTime = l;
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public void setStatus(String s) {
        this.status = s;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setDisplayStatus(boolean b) {
        this.isSelected = b;
    }


    @Override
    public boolean getDisplayStatus() {
        return this.isSelected;
    }

    @Override
    public int getAcceptedNum() {
        return acceptedNum;
    }

    @Override
    public void setAcceptedNum(int i) {
        this.acceptedNum = i;
    }

    @Override
    public int getTotalNum() {
        return totalNum;
    }

    @Override
    public void setTotalNum(int i) {
        this.totalNum = i;
    }

    @Override
    public String getTimeslotUid() {
        return timeslotUid;
    }

    public void setTimeslotUid(String timeslotUid) {
        this.timeslotUid = timeslotUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public int getRejectedNum() {
        return rejectedNum;
    }

    public void setRejectedNum(int rejectedNum) {
        this.rejectedNum = rejectedNum;
    }

    public int getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public int getIsSystemSuggested() {
        return isSystemSuggested;
    }

    public void setIsSystemSuggested(int isSystemSuggested) {
        this.isSystemSuggested = isSystemSuggested;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(int pendingNum) {
        this.pendingNum = pendingNum;
    }

    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }
}
