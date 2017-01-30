package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.User;

/**
 * Created by Paul on 30/1/17.
 */

public interface SettingRegionMvpView extends ItimeCommonMvpView, TaskBasedMvpView<User> {
    void toSelectCity(long locationId);
}
