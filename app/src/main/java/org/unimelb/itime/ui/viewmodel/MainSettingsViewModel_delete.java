package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.SettingWrapper;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView_delete;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yuhaoliu on 5/12/2016.
 */

public class MainSettingsViewModel_delete extends CommonViewModel{
    private static final String TAG = "MainSettingsViewModel_delete";
    private SettingCommonPresenter presenter;
    private SettingCommonMvpView_delete mvpView;
    private SettingWrapper setting;

    private ObservableList<Calendar> calendars = new ObservableArrayList<>();
    private ItemView calendarItemView = ItemView.of(BR.calendar, R.layout.setting_default_calendar_listview);

    public final static boolean SAVE = true;
    public final static boolean NOT_SAVE = false;

    public final static int TASK_LOGOUT = 1;
    public final static int TASK_VIEW_AVATAR = 2;
    public final static int TASK_TO_MY_PROFILE = 3;
    public final static int TASK_TO_SETTING = 4;
    public final static int TASK_TO_MY_PROFILE_NAME = 5;
    public final static int TASK_TO_RESET_PASSWORD = 6;
    public final static int TASK_TO_QR_CODE = 7;
    public final static int TASK_TO_GENDER = 8;
    public final static int TASK_TO_REGION = 9;
    public final static int TASK_TO_SCAN_QR_CODE = 10;
    public final static int TASK_TO_BLOCK_USER = 11;
    public final static int TASK_TO_NOTICIFATION = 12;
    public final static int TASK_TO_CALENDAR_PREFERENCE = 13;
    public final static int TASK_TO_HELP_AND_FEEDBACK = 14;
    public final static int TASK_TO_ABOUT = 15;
    public final static int TASK_TO_DEFAULT_ALERT = 16;
    public final static int TASK_TO_IMPORT_UNIMELB_CALENDAR = 17;
    public final static int TASK_TO_IMPORT_GOOGLE_CALENDAR = 18;
    public final static int TASK_TO_IMPORT_CALENDAR = 19;

    private int genderTickVisibility = View.GONE;

    public MainSettingsViewModel_delete(SettingCommonPresenter presenter){
        this.presenter = presenter;
        this.mvpView = (SettingCommonMvpView_delete) presenter.getView();

        if (setting == null){
            this.setting = SettingManager.getInstance(getContext()).getSetting();
        }

    }

    private Context getContext(){
        return presenter.getContext();
    }



    public View.OnClickListener onViewChange(final int task, final boolean isSave){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.onViewChange(task, isSave);
            }
        };
    }

    public View.OnClickListener chooseGender(final int index){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserGender(index);
            }
        };
    }

    private List<String> getGenderStringList(){
        ArrayList<String> list = new ArrayList<>();
        list.add(getContext().getString(R.string.setting_male));
        list.add(getContext().getString(R.string.setting_female));
        list.add(getContext().getString(R.string.setting_undefined));
        return list;
    }

    private void updateUserGender(int index){
        String gender = getGenderStringList().get(index);
        getSetting().getUser().setGender(gender);
        setSetting(setting);
    }




    @Bindable
    public ItemView getCalendarItemView() {
        return calendarItemView;
    }

    public void setCalendarItemView(ItemView calendarItemView) {
        this.calendarItemView = calendarItemView;
        notifyPropertyChanged(BR.itemView);
    }

    @Bindable
    public ObservableList<Calendar> getCalendars() {
        return this.calendars;
    }

    public void setCalendars(ObservableList<Calendar> calendars) {
        this.calendars = calendars;
        notifyPropertyChanged(BR.calendars);
    }

    @Bindable
    public SettingWrapper getSetting() {
        return setting;
    }

    public void setSetting(SettingWrapper setting) {
        this.setting = setting;
        notifyPropertyChanged(BR.setting);
    }


    public int getGenderVisibility(int from, SettingWrapper setting){
        String gender = setting.getUser().getGender();
        if (from == getGenderStringList().indexOf(gender)){
            return View.VISIBLE;
        }else{
            return View.GONE;
        }
    }

}
