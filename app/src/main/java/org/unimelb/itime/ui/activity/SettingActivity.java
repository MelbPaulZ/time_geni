package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.settings.SettingAboutFragment;
import org.unimelb.itime.ui.fragment.settings.SettingBlockContactsFragment;
import org.unimelb.itime.ui.fragment.settings.SettingCalendarPreferenceFragment;
import org.unimelb.itime.ui.fragment.settings.SettingMyProfileFragment;
import org.unimelb.itime.ui.fragment.settings.SettingNotificationFragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingActivity extends EmptyActivity{
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

    public final static String TASK = "task";
    private static final String TAG = "Setting";
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        fragmentManager = getSupportFragmentManager();
        int task = getIntent().getIntExtra(TASK,-100);
        this.fragmentSwitcher(task);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentSwitcher(resultCode);
    }

    private void fragmentSwitcher(int task){
        Class<? extends Fragment> name = null;
        if (task != -100){
            switch (task){
                case TASK_TO_MY_PROFILE:
                    name = SettingMyProfileFragment.class;
                    break;
                case TASK_TO_BLOCK_USER:
                    name = SettingBlockContactsFragment.class;
                    break;
                case TASK_TO_NOTICIFATION:
                    name = SettingNotificationFragment.class;
                    break;
                case TASK_TO_CALENDAR_PREFERENCE:
                    name = SettingCalendarPreferenceFragment.class;
                    break;
                case TASK_TO_HELP_AND_FEEDBACK:
                    break;
                case TASK_TO_ABOUT:
                    name = SettingAboutFragment.class;
                    break;
                default:
                    return;
            }
        }

        try {
            fragmentManager.beginTransaction().
                    replace(getFragmentContainerId(), name.newInstance())
                    .commit();
        }catch (Exception e){
            Log.i(TAG, "error on fragment swicher: ");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
