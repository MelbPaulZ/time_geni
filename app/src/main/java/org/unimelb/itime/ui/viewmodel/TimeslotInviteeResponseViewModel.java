package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.ui.presenter.TimeslotCreatePresenter;
import org.unimelb.itime.ui.presenter.TimeslotInviteeResponsePresenter;
import org.unimelb.itime.util.EventUtil;

import java.sql.Time;
import java.util.List;

/**
 * Created by yuhaoliu on 3/12/2016.
 */

public class TimeslotInviteeResponseViewModel extends CommonViewModel {
    private TimeslotInviteeResponsePresenter presenter;
    private Timeslot responseTimeslot;

    public TimeslotInviteeResponseViewModel(TimeslotInviteeResponsePresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public Timeslot getResponseTimeslot(){
        return this.responseTimeslot;
    }

    public void setResponseTimeslot(Timeslot responseTimeslot){
        this.responseTimeslot = responseTimeslot;
        notifyPropertyChanged(BR.responseTimeslot);
    }

    public String getTimeTitle(Timeslot timeslot){
        if (timeslot == null){
            return  "";
        }

        return EventUtil.getSuggestTimeStringFromLong(presenter.getContext()
                ,timeslot.getStartTime()
                ,timeslot.getEndTime());
    }

    public String getSubTimeTitle(Timeslot timeslot){
        if (timeslot == null){
            return "";
        }

        return EventUtil.getDayOfWeekString(presenter.getContext()
                , timeslot.getStartTime());
    }
}
