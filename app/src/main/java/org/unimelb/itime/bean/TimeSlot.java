package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.unimelb.itime.vendor.listener.ITimeTimeSlotInterface;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by Paul on 10/09/2016.
 */
@Entity
public class TimeSlot implements ITimeTimeSlotInterface,Serializable {
    @Id
    private int timeSlotUid;
    private long startTime;
    private long endTime;
    private String status;
    private int accetpedNum;
    private int totalNum;
    @Generated(hash = 1270889899)
    public TimeSlot(int timeSlotUid, long startTime, long endTime, String status,
            int accetpedNum, int totalNum) {
        this.timeSlotUid = timeSlotUid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.accetpedNum = accetpedNum;
        this.totalNum = totalNum;
    }

    @Generated(hash = 1337764006)
    public TimeSlot() {
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
        return accetpedNum;
    }

    @Override
    public void setAcceptedNum(int i) {
        this.accetpedNum = i;
    }

    @Override
    public int getTotalNum() {
        return totalNum;
    }

    @Override
    public void setTotalNum(int i) {
        this.totalNum = i;
    }


    public int getTimeSlotUid() {
        return timeSlotUid;
    }

    public void setTimeSlotUid(int timeSlotUid) {
        this.timeSlotUid = timeSlotUid;
    }

    public int getAccetpedNum() {
        return this.accetpedNum;
    }

    public void setAccetpedNum(int accetpedNum) {
        this.accetpedNum = accetpedNum;
    }
}
