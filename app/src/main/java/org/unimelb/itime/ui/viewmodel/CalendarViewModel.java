package org.unimelb.itime.ui.viewmodel;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.bean.Calendar;

import java.util.List;

/**
 * Created by yuhaoliu on 12/01/2017.
 */

public class CalendarViewModel extends CommonViewModel {
    private CalendarPresenter<? extends TaskBasedMvpView<List<Calendar>>> presenter;
    private List<Calendar> cals;

    public CalendarViewModel(CalendarPresenter<? extends TaskBasedMvpView<List<Calendar>>> presenter){
        this.presenter = presenter;
        this.cals = presenter.loadCals();
    }

    public void setCalendars(List<Calendar> cals){
        this.cals = cals;
    }
}
