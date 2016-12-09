package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 28/08/2016.
 */
public interface EventEditMvpView extends CommonMvpView {
    void toHostEventDetail(Event event);
    void toHostEventDetail();
    void changeLocation();
    void toTimeSlotView(Event event);
    void toInviteePicker(Event event);
    void toPhotoPicker();
    void toSoloEventDetail();
    void toSoloEventDetail(Event event);
}
