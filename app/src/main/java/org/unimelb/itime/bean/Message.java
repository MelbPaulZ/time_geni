package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Id;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.MessageDao;

/**
 * Created by yuhaoliu on 1/12/16.
 */
@Entity(active = true)
public class Message {
    String title = "";
    String subtitle1 = "";
    String subtitle2 = "";
    @Id
    String messageUid = "";
    String userUid = "";
    String eventUid = "";
    String createdAt = "";
    String updatedAt = "";

    int num1;
    int num2;
    int num3;
    int type;
    int deleteLevel;


    boolean isRead;
    boolean hasBadge;
    /** Used for active entity operations. */
    @Generated(hash = 859287859)
    private transient MessageDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 1851012146)
    public Message(String title, String subtitle1, String subtitle2,
            String messageUid, String userUid, String eventUid, String createdAt,
            String updatedAt, int num1, int num2, int num3, int type,
            int deleteLevel, boolean isRead, boolean hasBadge) {
        this.title = title;
        this.subtitle1 = subtitle1;
        this.subtitle2 = subtitle2;
        this.messageUid = messageUid;
        this.userUid = userUid;
        this.eventUid = eventUid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.type = type;
        this.deleteLevel = deleteLevel;
        this.isRead = isRead;
        this.hasBadge = hasBadge;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getNum3() {
        return num3;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle1() {
        return subtitle1;
    }

    public void setSubtitle1(String subtitle1) {
        this.subtitle1 = subtitle1;
    }

    public String getSubtitle2() {
        return subtitle2;
    }

    public void setSubtitle2(String subtitle2) {
        this.subtitle2 = subtitle2;
    }

    public String getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(String messageUid) {
        this.messageUid = messageUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isHasBadge() {
        return hasBadge;
    }

    public void setHasBadge(boolean hasBadge) {
        this.hasBadge = hasBadge;
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

    public boolean getHasBadge() {
        return this.hasBadge;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
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
    @Generated(hash = 747015224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageDao() : null;
    }

}
