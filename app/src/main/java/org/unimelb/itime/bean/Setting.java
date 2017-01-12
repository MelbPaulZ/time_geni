package org.unimelb.itime.bean;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class Setting {

    boolean enableNotification =  true;
    boolean showPreviewText = true;
    boolean appAlertSound = true;
    boolean systemVibrate = true;
    boolean enableFriendRequestEmail = true;
    boolean enableEventInvitationEmail = true;
    boolean enableEventConfirmEmail = true;

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
}
