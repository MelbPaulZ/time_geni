package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;

import java.io.Serializable;

/**
 * Created by Paul on 10/09/2016.
 */
public class Timeslot implements ITimeTimeSlotInterface,Serializable {
    private String timeslotUid;
    private String eventUid;
    private String userUid;
    private long startTime;
    private long endTime;
    private String status = "";
    private int acceptedNum;
    private int totalNum;
    private int rejectedNum;
    private int pendingNum;
    private int isConfirmed;
    private int isSystemSuggested;
    
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
}
