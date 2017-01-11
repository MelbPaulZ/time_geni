package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.User;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface UserMvpView extends TaskBasedMvpView<User>, ItimeCommonMvpView{

    void toEditPhotoPage();
    void toEditNamePage();
    void toEditEmailPage();
    void toEditPasswordPage();
    void toEditMyQrCodePage();
    void toEditGenderPage();
    void toEditRegionPage();
}
