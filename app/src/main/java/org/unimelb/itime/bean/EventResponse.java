package org.unimelb.itime.bean;

import java.io.Serializable;

/**
 * Created by yinchuandong on 15/1/17.
 */

public class EventResponse implements Serializable{

    private String responseUid;
    private String userUid;
    //todo: need to change it after server is done
    private String photo = "http://img.zybus.com/uploads/allimg/131213/1-131213111353.jpg";
    private String eventUid;
    private String content;
    private int deleteLevel;
    private String createdAt;
    private String updatedAt;

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
