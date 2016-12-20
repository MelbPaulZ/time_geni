package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.bean.Timeslot;

import java.util.List;

/**
 * Created by Paul on 20/12/2016.
 */

public interface TimeslotCommonMvpView extends MvpView {

    void onRecommend(List<Timeslot> timeslotList);

}
