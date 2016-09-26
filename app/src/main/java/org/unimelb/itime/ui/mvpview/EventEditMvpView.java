package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 28/08/2016.
 */
public interface EventEditMvpView extends MvpView {
    void toHostEventDetail(Event event);
    void changeLocation();
    void toTimeSlotView(String tag);
    void toInviteePicker(String tag);
    void toPhotoPicker(String tag);
    void toSoloEventDetail();
    void toSoloEventDetail(Event event);
}
