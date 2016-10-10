package org.unimelb.itime.bean;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;

import java.io.Serializable;

/**
 * Created by yuhaoliu on 17/08/16.
 */
@Entity
public class Contact implements ITimeContactInterface, Serializable {

    private String userUid;
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

    @Generated(hash = 401063312)
    public Contact(String userUid, String contactUid, int relationship,
            int ratingVisibility, int eventVisibility, String source,
            String aliasName, String aliasPhoto, int catchCount,
            long nextCatchupTime, long lastCatchupTime, String note, String status) {
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
    }

    @Generated(hash = 672515148)
    public Contact() {
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
}
