package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class SlotResponse implements Serializable {
    private String status="";
    private int rate;
    private String timeslotUid="";
    private String inviteeUid="";
    private String eventUid="";
    private int userUid;

    public SlotResponse(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTimeslotUid() {
        return timeslotUid;
    }

    public void setTimeslotUid(String timeslotUid) {
        this.timeslotUid = timeslotUid;
    }

    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public int getUserUid() {
        return userUid;
    }

    public void setUserUid(int userUid) {
        this.userUid = userUid;
    }
}
