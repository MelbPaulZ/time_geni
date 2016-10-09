package org.unimelb.itime.bean;

/**
 * Created by Paul on 24/09/2016.
 */
public class Calendar {
    private String iCalUID;
    private String summary;
    private String color;
    private String access;
    private String status;
    private String calendarUid;
    private int groupUid;
    private String groupTitle;
    private int visibility;
    private int deleteLevel;
    private String createdAt;
    private String updatedAt;

    public String getiCalUID() {
        return iCalUID;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
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

    public int getGroupUid() {
        return groupUid;
    }

    public void setGroupUid(int groupUid) {
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
