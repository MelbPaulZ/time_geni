package org.unimelb.itime.ui.mvpview;

/**
 * Created by Paul on 31/08/2016.
 */
public interface EventCreateDetailBeforeSendingMvpView extends EventCommonMvpView {
    void onClickSend();
    void onClickCancel();
    void changeLocation();
    void pickInvitees();
    void pickPhoto();
    void onClickProposedTimeslots();
}
