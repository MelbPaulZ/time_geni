package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 10/09/2016.
 */
@Entity
public class Timeslot implements ITimeTimeSlotInterface,Serializable {
    private String timeslotUid;
    private String eventUid;
    private long startTime;
    private long endTime;
    private String status = "";
    private int acceptedNum;
    private int totalNum;
    private int peopleCount;
    private int isConfirmed;
    private int isSystemSuggested;



    @Generated(hash = 51562862)
    public Timeslot(String timeslotUid, String eventUid, long startTime,
            long endTime, String status, int acceptedNum, int totalNum,
            int peopleCount, int isConfirmed, int isSystemSuggested) {
        this.timeslotUid = timeslotUid;
        this.eventUid = eventUid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.acceptedNum = acceptedNum;
        this.totalNum = totalNum;
        this.peopleCount = peopleCount;
        this.isConfirmed = isConfirmed;
        this.isSystemSuggested = isSystemSuggested;
    }

    @Generated(hash = 1204592951)
    public Timeslot() {
    }



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

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
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
}
