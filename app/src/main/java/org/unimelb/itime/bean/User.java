package org.unimelb.itime.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;

/**
 * Created by yinchuandong on 20/06/2016.
 */

@Entity(
        active = false
)
public class User implements Serializable{

    private String userUid;
    private String userId;
    private String personalAlias;
    private String email;
    private String phone;
    private String photo;
    private String source;
    private String deviceToken;
    private String deviceId;
    private String averageRatingValue;
    private String timezone;
    private String lastSigninTime;
    private int signinCount;
    private String createdAt;
    private String updatedAt;




    @Generated(hash = 6482242)
    public User(String userUid, String userId, String personalAlias, String email,
            String phone, String photo, String source, String deviceToken,
            String deviceId, String averageRatingValue, String timezone,
            String lastSigninTime, int signinCount, String createdAt,
            String updatedAt) {
        this.userUid = userUid;
        this.userId = userId;
        this.personalAlias = personalAlias;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.source = source;
        this.deviceToken = deviceToken;
        this.deviceId = deviceId;
        this.averageRatingValue = averageRatingValue;
        this.timezone = timezone;
        this.lastSigninTime = lastSigninTime;
        this.signinCount = signinCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 586692638)
    public User() {
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

    public String getPersonalAlias() {
        return personalAlias;
    }

    public void setPersonalAlias(String personalAlias) {
        this.personalAlias = personalAlias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAverageRatingValue() {
        return averageRatingValue;
    }

    public void setAverageRatingValue(String averageRatingValue) {
        this.averageRatingValue = averageRatingValue;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLastSigninTime() {
        return lastSigninTime;
    }

    public void setLastSigninTime(String lastSigninTime) {
        this.lastSigninTime = lastSigninTime;
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

    public int getSigninCount() {
        return signinCount;
    }

    public void setSigninCount(int signinCount) {
        this.signinCount = signinCount;
    }
}


