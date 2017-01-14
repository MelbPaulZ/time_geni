package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.mvpview.CalendarImportMvpView;
import org.unimelb.itime.ui.mvpview.CalendarPreferenceMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPreferenceViewModel extends CommonViewModel {
    public final static String TAG = "UserProfileViewModel";

    private CalendarPreferenceMvpView mvpViewCalPref;
    private CalendarImportMvpView mvpViewCalImport;

    private MvpBasePresenter presenter;
    private User user;

    private Setting setting;

    public CalendarPreferenceViewModel(MvpBasePresenter presenter){
        this.presenter = presenter;

        if(presenter.getView() instanceof CalendarPreferenceMvpView){
            this.mvpViewCalPref = (CalendarPreferenceMvpView) presenter.getView();
        }else if (presenter.getView() instanceof CalendarImportMvpView){
            this.mvpViewCalImport = (CalendarImportMvpView) presenter.getView();
        }
    }

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }
    /**
     * For Calendar preference page
     * @return
     */
    public View.OnClickListener onCalendarsClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mvpViewCalPref.toCalendarPage();
            }
        };
    }

    public View.OnClickListener onAlertClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mvpViewCalPref.toAlertTimePage();
            }
        };
    }

    public View.OnClickListener onImportCalClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mvpViewCalPref.toImportPage();
            }
        };
    }

    /**
     * For Calendar import page
     * @return
     */
    public View.OnClickListener onImportGoogleClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mvpViewCalImport.toGoogleCal();
            }
        };
    }

    public View.OnClickListener onImportUnimelbClick(){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mvpViewCalImport.toUnimebCal();
            }
        };
    }

}
