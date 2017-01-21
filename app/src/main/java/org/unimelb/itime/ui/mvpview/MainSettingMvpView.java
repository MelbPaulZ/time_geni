package org.unimelb.itime.ui.mvpview;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface MainSettingMvpView<T extends Object> extends TaskBasedMvpView<T>, ItimeCommonMvpView{

    void toProfilePage();
    void toQRcodePage();
    void toBlockedUserPage();
    void toNotificationPage();
    void toCalendarPreferencePage();
    void toHelpFdPage();
    void toAboutPage();
    void onLogOut();
}
