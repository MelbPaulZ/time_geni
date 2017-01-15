package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;

import java.util.List;

/**
 * Created by Paul on 28/08/2016.
 */
public interface EventEditMvpView extends TaskBasedMvpView<List<Event>>, ItimeCommonMvpView{
    void toEventDetailPage();
    void toLocationPage();
    void toTimeslotViewPage();
    void toInviteePickerPage();
    void toPhotoPickerPage();
}
