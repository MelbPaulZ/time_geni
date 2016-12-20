package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 20/11/16.
 */
public interface TimeslotCreateMvpView extends TimeslotCommonMvpView {
    void onClickCancel();
    void onClickDone();
    void onChooseTime(int type, long time);
    void onClickPickerDone();
}
