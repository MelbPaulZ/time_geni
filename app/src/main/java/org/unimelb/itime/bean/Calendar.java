package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import org.greenrobot.greendao.DaoException;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.CalendarDao;

/**
 * Created by Paul on 24/09/2016.
 */
@Entity(active = true)
public class Calendar implements Serializable, Cloneable{
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
    /** Used for active entity operations. */
    @Generated(hash = 859169596)
    private transient CalendarDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

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

    @Override
    public Calendar clone() throws CloneNotSupportedException {
        Calendar calendar = null;
        try
        {
            calendar = (Calendar) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1455144573)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCalendarDao() : null;
    }
}
