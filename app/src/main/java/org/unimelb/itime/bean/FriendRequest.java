package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.FriendRequestDao;
import org.unimelb.itime.dao.UserDao;

/**
 * Created by Qiushuo Huang on 2016/12/21.
 */

@Entity
public class FriendRequest implements Serializable {
    public static final String STATUS_SENT = "sent";
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String SOURCE_ITIME = "itime";
    public static final String SOURCE_MOBILE = "mobile";
    public static final String SOURCE_GMAIL = "gmail";
    public static final String SOURCE_FACEBOOK = "facebook";
    public static final String DISPLAY_STATUS_ADDED = "Added";
    public static final String DISPLAY_STATUS_ACCEPT = "Accept";
    public static final String DISPLAY_STATUS_ACCEPTED = "Accepted";


    private String source;
    private String note;
    private String status;
    @Id
    private String freqUid;

    //sender uid
    private String userUid;

    //receiver uid
    private String freqUserUid;
    private String userId;
    private int isRead;
    private String createdAt;
    private String updatedAt;
    private String displayStatus;

    @ToOne(joinProperty = "userUid")
    private User userDetail;
    /** Used for active entity operations. */
    @Generated(hash = 1817986260)
    private transient FriendRequestDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    @Generated(hash = 317222054)
    private transient String userDetail__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2123810054)
    public User getUserDetail() {
        String __key = this.userUid;
        if (userDetail__resolvedKey == null || userDetail__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userDetailNew = targetDao.load(__key);
            synchronized (this) {
                userDetail = userDetailNew;
                userDetail__resolvedKey = __key;
            }
        }
        return userDetail;
    }

    public User getUser(){
        return userDetail;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 660910200)
    public void setUserDetail(User userDetail) {
        synchronized (this) {
            this.userDetail = userDetail;
            userUid = userDetail == null ? null : userDetail.getUserUid();
            userDetail__resolvedKey = userUid;
        }
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    @Generated(hash = 1332562442)
    public FriendRequest(String source, String note, String status, String freqUid, String userUid, String freqUserUid, String userId, int isRead, String createdAt, String updatedAt, String displayStatus) {
        this.source = source;
        this.note = note;
        this.status = status;
        this.freqUid = freqUid;
        this.userUid = userUid;
        this.freqUserUid = freqUserUid;
        this.userId = userId;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.displayStatus = displayStatus;
    }

    public FriendRequest(){}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFreqUid() {
        return freqUid;
    }

    public void setFreqUid(String freqUid) {
        this.freqUid = freqUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getFreqUserUid() {
        return freqUserUid;
    }

    public void setFreqUserUid(String freqUserUid) {
        this.freqUserUid = freqUserUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return isRead>0;
    }

    public void setRead(boolean read) {
       if(read){
           isRead =1;
       }else{
           isRead = 0;
       }
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

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
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
    @Generated(hash = 240767073)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFriendRequestDao() : null;
    }

}
