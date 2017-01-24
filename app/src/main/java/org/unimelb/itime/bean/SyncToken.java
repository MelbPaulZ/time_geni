package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Id;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.SyncTokenDao;

/**
 * Created by yuhaoliu on 20/01/2017.
 */
@Entity(active = true)
public class SyncToken {
    public final static String PREFIX_CAL = "calendar_list";
    public final static String PREFIX_EVENT = "event_list";
    public final static String PREFIX_MESSAGE = "message_list";

    private String userUid = "";
    @Id
    private String name = "";
    private String value = "";
    /** Used for active entity operations. */
    @Generated(hash = 1412225853)
    private transient SyncTokenDao myDao;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserUid() {
        return this.userUid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
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
    @Generated(hash = 181667044)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSyncTokenDao() : null;
    }
    @Generated(hash = 2051447372)
    public SyncToken(String userUid, String name, String value) {
        this.userUid = userUid;
        this.name = name;
        this.value = value;
    }
    @Generated(hash = 1307131303)
    public SyncToken() {
    }
}
