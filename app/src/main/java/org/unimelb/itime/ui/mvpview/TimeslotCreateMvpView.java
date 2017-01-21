package org.unimelb.itime.ui.mvpview;

/**
 * Created by Paul on 20/11/16.
 */
public interface TimeslotCreateMvpView extends ItimeCommonMvpView{
    void onChooseTime(int type, long time);
    void onClickPickerDone();
}
