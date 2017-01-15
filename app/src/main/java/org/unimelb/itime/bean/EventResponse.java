package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by yinchuandong on 15/1/17.
 */

public class EventResponse implements Serializable{

    private String responseUid;
    private String userUid;
    private String photo;
    private String eventUid;
    private String content;
    private int deleteLevel;

    public String getResponseUid() {
        return responseUid;
    }

    public void setResponseUid(String responseUid) {
        this.responseUid = responseUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }
}
