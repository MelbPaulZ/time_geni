package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Timeslot;

import java.util.List;

/**
 * Created by Paul on 27/08/2016.
 */
public interface EventCreateNewTimeSlotMvpView extends MvpView {
    void onClickDone();
    void onClickBack();
    void initTimeSlots(Event event);
    void onRecommend(List<Timeslot> list);

}
