package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;

import java.util.List;

/**
 * Created by Paul on 28/08/2016.
 */
public interface EventCustomRepeatMvpView extends TaskBasedMvpView<List<Event>>, ItimeCommonMvpView{
    void popUpFrequencyPickerDialog();
    void popUpEveryPickerDialog();
}
