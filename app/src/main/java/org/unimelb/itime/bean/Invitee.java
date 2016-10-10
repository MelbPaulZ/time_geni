package org.unimelb.itime.bean;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;
import org.unimelb.itime.dao.ContactDao;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.InviteeDao;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yuhaoliu on 10/09/2016.
 */
@Entity
public class Invitee implements ITimeInviteeInterface, Serializable {
    private String eventUid;
    private String inviteeUid;
    private String aliasName;
    private String aliasPhoto;
    private String status;

    @Generated(hash = 1566734362)
    public Invitee(String eventUid, String inviteeUid, String aliasName,
            String aliasPhoto, String status) {
        this.eventUid = eventUid;
        this.inviteeUid = inviteeUid;
        this.aliasName = aliasName;
        this.aliasPhoto = aliasPhoto;
        this.status = status;
    }

    @Generated(hash = 15121660)
    public Invitee() {
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    @Nullable
    @Override
    public String getPhoto() {
        return this.getAliasPhoto();
    }

    @Override
    public String getName() {
        return getAliasName();
    }

    @Override
    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
