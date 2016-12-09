package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by yuhaoliu on 9/12/2016.
 */

public interface CommonMvpView extends MvpView {
    void onShowDialog();
    void onHideDialog();
}
