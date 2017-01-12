package org.unimelb.itime.ui.presenter;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPresenter<V extends TaskBasedMvpView<List<Calendar>>> extends MvpBasePresenter<V>{

    public CalendarPresenter(Context context){
    }

    public List<Calendar> loadCals(){
        return null;
    }
}
