package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 24/09/2016.
 */
@Entity
public class Calendar {
    private String iCalUID;
    private String summary;
    private String color;
    private String access;
    private String status;
    @Id
    private String calendarUid;
    private String groupUid;
    private String groupTitle;
    private int visibility;
    private int deleteLevel;
    private String createdAt;
    private String updatedAt;
    private String userUid;

    @Generated(hash = 78326509)
    public Calendar(String iCalUID, String summary, String color, String access,
            String status, String calendarUid, String groupUid, String groupTitle,
            int visibility, int deleteLevel, String createdAt, String updatedAt,
            String userUid) {
        this.iCalUID = iCalUID;
        this.summary = summary;
        this.color = color;
        this.access = access;
        this.status = status;
        this.calendarUid = calendarUid;
        this.groupUid = groupUid;
        this.groupTitle = groupTitle;
        this.visibility = visibility;
        this.deleteLevel = deleteLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userUid = userUid;
    }

    @Generated(hash = 2039519234)
    public Calendar() {
    }

    public String getiCalUID() {
        return iCalUID;
    }

    public void setiCalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCalendarUid() {
        return calendarUid;
    }

    public void setCalendarUid(String calendarUid) {
        this.calendarUid = calendarUid;
    }

    public String getGroupUid() {
        return groupUid;
    }

    public void setGroupUid(String groupUid) {
        this.groupUid = groupUid;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
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

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getICalUID() {
        return this.iCalUID;
    }

    public void setICalUID(String iCalUID) {
        this.iCalUID = iCalUID;
    }
}
