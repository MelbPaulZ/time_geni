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
    public static final String SENT = "sent";
    public static final String CONFIRMED = "confirmed";
    public static final String ITIME = "itime";
    public static final String MOBILE = "mobile";
    public static final String GMAIL = "gmail";
    public static final String FACEBOOK = "facebook";

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

    @Transient
    private Contact contact;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @Generated(hash = 348599141)
    public FriendRequest(String source, String note, String status, String freqUid, String userUid, String freqUserUid, String userId, int isRead, String createdAt, String updatedAt) {
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

}
