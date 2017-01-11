package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseActivity;
import org.unimelb.itime.ui.fragment.contact.MyQRCodeFragment;
import org.unimelb.itime.ui.fragment.settings.SettingAboutFragment;
import org.unimelb.itime.ui.fragment.settings.SettingCalendarPreferenceFragment;
import org.unimelb.itime.ui.fragment.settings.SettingDefaultAlertFragment;
import org.unimelb.itime.ui.fragment.settings.SettingImportCalendarFragment;
import org.unimelb.itime.ui.fragment.settings.SettingImportCalendarUnimelbFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileNameFragment;
import org.unimelb.itime.ui.fragment.settings.SettingNotificationFragment;
import org.unimelb.itime.ui.fragment.settings.SettingProfileGenderFragment;
import org.unimelb.itime.ui.fragment.settings.SettingProfileResetPasswordFragment;

public class SettingActivity extends BaseActivity{

    public final static String TASK = "task";
    private SettingMyProfileFragment myProfileFragment;
    private SettingMyProfileNameFragment myProfileNameFragment;
    private SettingAboutFragment aboutFragment;
    private SettingCalendarPreferenceFragment calendarPreferenceFragment;
    private SettingDefaultAlertFragment defaultCalendarFragment;
    private SettingImportCalendarUnimelbFragment importCalendarUnimelbFragment;
    private SettingNotificationFragment notificationFragment;
    private SettingProfileGenderFragment profileGenderFragment;
    private SettingProfileResetPasswordFragment resetPasswordFragment;
    private SettingImportCalendarFragment importCalendarFragment;
    private MyQRCodeFragment myQRCodeFragment;

    private FragmentManager fragmentManager;



    public void forward(){

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fragmentManager = getSupportFragmentManager();

        myProfileFragment = new SettingMyProfileFragment();

        fragmentManager.beginTransaction().
                replace(R.id.setting_activity_framelayout, myProfileFragment)
                .commit();



//        initSettingFragments();
//        show();

    }

//    private void show(){
//        Intent intent = getIntent();
//        int task = intent.getIntExtra(TASK,-100);
//        if (task!=-100){
//            showOnTask(task);
//        }
//    }

//    private void showOnTask(int task){
//        switch (task){
//            case MainSettingsViewModel.TASK_TO_MY_PROFILE:
//                showFragment(myProfileFragment);
//                break;
//            case MainSettingsViewModel.TASK_TO_SCAN_QR_CODE:
//                break;
//            case MainSettingsViewModel.TASK_TO_BLOCK_USER:
//                break;
//            case MainSettingsViewModel.TASK_TO_NOTICIFATION:
//                showFragment(notificationFragment);
//                break;
//            case MainSettingsViewModel.TASK_TO_CALENDAR_PREFERENCE:
//                showFragment(calendarPreferenceFragment);
//                break;
//            case MainSettingsViewModel.TASK_TO_HELP_AND_FEEDBACK:
//                break;
//            case MainSettingsViewModel.TASK_TO_ABOUT:
//                showFragment(aboutFragment);
//        }
//    }
//
//    private void initSettingFragments(){
//        myProfileFragment = new SettingMyProfileFragment();
//        myProfileNameFragment = new SettingMyProfileNameFragment();
//        aboutFragment = new SettingAboutFragment();
//        calendarPreferenceFragment = new SettingCalendarPreferenceFragment();
//        defaultCalendarFragment = new SettingDefaultAlertFragment();
//        importCalendarUnimelbFragment = new SettingImportCalendarUnimelbFragment();
//        notificationFragment = new SettingNotificationFragment();
//        profileGenderFragment = new SettingProfileGenderFragment();
//        resetPasswordFragment = new SettingProfileResetPasswordFragment();
//        importCalendarFragment = new SettingImportCalendarFragment();
//        myQRCodeFragment = new MyQRCodeFragment();
//
//
//        hideFragment(myProfileFragment);
//        hideFragment(myProfileNameFragment);
//        hideFragment(aboutFragment);
//        hideFragment(calendarPreferenceFragment);
//        hideFragment(defaultCalendarFragment);
//        hideFragment(importCalendarUnimelbFragment);
//        hideFragment(notificationFragment);
//        hideFragment(profileGenderFragment);
//        hideFragment(resetPasswordFragment);
//        hideFragment(importCalendarFragment);
//        hideFragment(myQRCodeFragment);
//    }
//
//    private void hideFragment(BaseUiFragment fragment){
//        getSupportFragmentManager().beginTransaction().add(R.id.setting_activity_framelayout, fragment, fragment.getClassName()).hide(fragment).commit();
//    }
//
//    private void showFragment(BaseUiFragment fragment){
//        getSupportFragmentManager().beginTransaction().show(fragment).commit();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
