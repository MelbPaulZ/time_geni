package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 31/08/2016.
 */
public interface EventCreateDetailBeforeSendingMvpView extends CommonMvpView {
    void onClickSend();
    void onClickCancel();
    void changeLocation();
    void pickInvitees();
    void pickPhoto(String tag);
    void onClickProposedTimeslots();
}
