package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yinchuandong on 11/1/17.
 */

@Entity
public class Setting implements Serializable{
    private String userUid = "";
    private boolean enableNotification =  true;
    private boolean showPreviewText = true;
    private boolean appAlertSound = true;
    private boolean systemVibrate = true;
    private boolean enableFriendRequestEmail = true;
    private boolean enableEventInvitationEmail = true;
    private boolean enableEventConfirmEmail = true;
    private int defaultAlertTime;
    
    @Generated(hash = 1644234448)
    public Setting(String userUid, boolean enableNotification,
            boolean showPreviewText, boolean appAlertSound, boolean systemVibrate,
            boolean enableFriendRequestEmail, boolean enableEventInvitationEmail,
            boolean enableEventConfirmEmail, int defaultAlertTime) {
        this.userUid = userUid;
        this.enableNotification = enableNotification;
        this.showPreviewText = showPreviewText;
        this.appAlertSound = appAlertSound;
        this.systemVibrate = systemVibrate;
        this.enableFriendRequestEmail = enableFriendRequestEmail;
        this.enableEventInvitationEmail = enableEventInvitationEmail;
        this.enableEventConfirmEmail = enableEventConfirmEmail;
        this.defaultAlertTime = defaultAlertTime;
    }

    @Generated(hash = 909716735)
    public Setting() {
    }

    public boolean isEnableNotification() {
        return enableNotification;
    }

    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public boolean isShowPreviewText() {
        return showPreviewText;
    }

    public void setShowPreviewText(boolean showPreviewText) {
        this.showPreviewText = showPreviewText;
    }

    public boolean isAppAlertSound() {
        return appAlertSound;
    }

    public void setAppAlertSound(boolean appAlertSound) {
        this.appAlertSound = appAlertSound;
    }

    public boolean isSystemVibrate() {
        return systemVibrate;
    }

    public void setSystemVibrate(boolean systemVibrate) {
        this.systemVibrate = systemVibrate;
    }

    public boolean isEnableFriendRequestEmail() {
        return enableFriendRequestEmail;
    }

    public void setEnableFriendRequestEmail(boolean enableFriendRequestEmail) {
        this.enableFriendRequestEmail = enableFriendRequestEmail;
    }

    public boolean isEnableEventInvitationEmail() {
        return enableEventInvitationEmail;
    }

    public void setEnableEventInvitationEmail(boolean enableEventInvitationEmail) {
        this.enableEventInvitationEmail = enableEventInvitationEmail;
    }

    public boolean isEnableEventConfirmEmail() {
        return enableEventConfirmEmail;
    }

    public void setEnableEventConfirmEmail(boolean enableEventConfirmEmail) {
        this.enableEventConfirmEmail = enableEventConfirmEmail;
    }

    public boolean getEnableEventConfirmEmail() {
        return this.enableEventConfirmEmail;
    }

    public boolean getEnableEventInvitationEmail() {
        return this.enableEventInvitationEmail;
    }

    public boolean getEnableFriendRequestEmail() {
        return this.enableFriendRequestEmail;
    }

    public boolean getSystemVibrate() {
        return this.systemVibrate;
    }

    public boolean getAppAlertSound() {
        return this.appAlertSound;
    }

    public boolean getShowPreviewText() {
        return this.showPreviewText;
    }

    public boolean getEnableNotification() {
        return this.enableNotification;
    }

    public int getDefaultAlertTime() {
        return this.defaultAlertTime;
    }

    public void setDefaultAlertTime(int defaultAlertTime) {
        this.defaultAlertTime = defaultAlertTime;
    }

    public String getUserUid() {
        return this.userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
