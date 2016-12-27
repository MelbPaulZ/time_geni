package org.unimelb.itime.bean;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;

import java.io.Serializable;
import org.greenrobot.greendao.DaoException;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.ContactDao;
import org.unimelb.itime.dao.UserDao;

/**
 * Created by yuhaoliu on 17/08/16.
 */
@Entity
public class Contact implements ITimeContactInterface, Serializable {
    public static final String DELETED = "deleted";
    public static final String ACTIVATED = "activated";


    private String userUid;
    @Id
    private String contactUid;
    private int relationship;
    private int ratingVisibility;
    private int eventVisibility;
    private String source;
    private String aliasName;
    private String aliasPhoto;
    private int catchCount;
    private long nextCatchupTime;
    private long lastCatchupTime;
    private String note;
    private String status;
    private int blockLevel;

    @ToOne(joinProperty = "contactUid")
    private User userDetail;
    @Generated(hash = 317222054)
    private transient String userDetail__resolvedKey;
    /** Used for active entity operations. */
    @Generated(hash = 2046468181)
    private transient ContactDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;



//@Generated(hash = 283800893)
//    public Contact(String userUid, String contactUid, int relationship, int ratingVisibility, int eventVisibility, String source, String aliasName, String aliasPhoto, int catchCount, long nextCatchupTime, long lastCatchupTime, String note, String status, int blockLevel) {
//        this.userUid = userUid;
//        this.contactUid = contactUid;
//        this.relationship = relationship;
//        this.ratingVisibility = ratingVisibility;
//        this.eventVisibility = eventVisibility;
//        this.source = source;
//        this.aliasName = aliasName;
//        this.aliasPhoto = aliasPhoto;
//        this.catchCount = catchCount;
//        this.nextCatchupTime = nextCatchupTime;
//        this.lastCatchupTime = lastCatchupTime;
//        this.note = note;
//        this.status = status;
//        this.blockLevel = blockLevel;
//    }

@Generated(hash = 283800893)
public Contact(String userUid, String contactUid, int relationship, int ratingVisibility, int eventVisibility, String source, String aliasName, String aliasPhoto, int catchCount, long nextCatchupTime, long lastCatchupTime, String note, String status, int blockLevel) {
    this.userUid = userUid;
    this.contactUid = contactUid;
    this.relationship = relationship;
    this.ratingVisibility = ratingVisibility;
    this.eventVisibility = eventVisibility;
    this.source = source;
    this.aliasName = aliasName;
    this.aliasPhoto = aliasPhoto;
    this.catchCount = catchCount;
    this.nextCatchupTime = nextCatchupTime;
    this.lastCatchupTime = lastCatchupTime;
    this.note = note;
    this.status = status;
    this.blockLevel = blockLevel;
}

    

    //    @Generated(hash = 283800893)
//    public Contact(String userUid, String contactUid, int relationship, int ratingVisibility, int eventVisibility, String source, String aliasName, String aliasPhoto, int catchCount, long nextCatchupTime, long lastCatchupTime, String note, String status, int blockLevel) {
//        this.userUid = userUid;
//        this.contactUid = contactUid;
//        this.relationship = relationship;
//        this.ratingVisibility = ratingVisibility;
//        this.eventVisibility = eventVisibility;
//        this.source = source;
//        this.aliasName = aliasName;
//        this.aliasPhoto = aliasPhoto;
//        this.catchCount = catchCount;
//        this.nextCatchupTime = nextCatchupTime;
//        this.lastCatchupTime = lastCatchupTime;
//        this.note = note;
//        this.status = status;
//        this.blockLevel = blockLevel;
//    }




    @Generated(hash = 672515148)
    public Contact() {
    }

    public int getBlockLevel() {
        return blockLevel;
    }

    public void setBlockLevel(int blockLevel) {
        this.blockLevel = blockLevel;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    @Override
    public String getContactUid() {
        return contactUid;
    }

    @Override
    public void setPhoto(String s) {
        this.setAliasPhoto(s);
    }

    @Nullable
    @Override
    public String getPhoto() {
        return this.getAliasPhoto();
    }

    @Override
    public void setName(String s) {
        this.setAliasName(s);
    }

    @Override
    public String getName() {
        return this.getAliasName();
    }

    @Override
    public void setContactUid(String contactUid) {
        this.contactUid = contactUid;
    }

    public int getRelationship() {
        return relationship;
    }

    public void setRelationship(int relationship) {
        this.relationship = relationship;
    }

    public int getRatingVisibility() {
        return ratingVisibility;
    }

    public void setRatingVisibility(int ratingVisibility) {
        this.ratingVisibility = ratingVisibility;
    }

    public int getEventVisibility() {
        return eventVisibility;
    }

    public void setEventVisibility(int eventVisibility) {
        this.eventVisibility = eventVisibility;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasPhoto() {
        return aliasPhoto;
    }

    public void setAliasPhoto(String aliasPhoto) {
        this.aliasPhoto = aliasPhoto;
    }

    public int getCatchCount() {
        return catchCount;
    }

    public void setCatchCount(int catchCount) {
        this.catchCount = catchCount;
    }

    public long getNextCatchupTime() {
        return nextCatchupTime;
    }

    public void setNextCatchupTime(long nextCatchupTime) {
        this.nextCatchupTime = nextCatchupTime;
    }

    public long getLastCatchupTime() {
        return lastCatchupTime;
    }

    public void setLastCatchupTime(long lastCatchupTime) {
        this.lastCatchupTime = lastCatchupTime;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1688336360)
    public User getUserDetail() {
        String __key = this.contactUid;
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
    @Generated(hash = 1323659472)
    public void setUserDetail(User userDetail) {
        synchronized (this) {
            this.userDetail = userDetail;
            contactUid = userDetail == null ? null : userDetail.getUserUid();
            userDetail__resolvedKey = contactUid;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2088270543)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactDao() : null;
    }
}
