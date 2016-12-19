package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;

/**
 * Created by Paul on 28/08/2016.
 */
public interface EventEditMvpView extends EventCommonMvpView {
    void toHostEventDetail();
    void changeLocation();
    void toTimeSlotView(Event event);
    void toInviteePicker(Event event);
    void toPhotoPicker();
}
