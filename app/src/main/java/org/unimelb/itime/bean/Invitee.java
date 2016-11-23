package org.unimelb.itime.bean;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yuhaoliu on 10/09/2016.
 */
public class Invitee implements ITimeInviteeInterface, Serializable {
    private String eventUid;
    private String inviteeUid;
    private String userUid;
    private String userId;
    private String aliasName;
    private String aliasPhoto;
    private String status;
    private String reason;
    private int isHost;
    

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getIsHost() {
        return isHost;
    }

    public void setIsHost(int isHost) {
        this.isHost = isHost;
    }
}
