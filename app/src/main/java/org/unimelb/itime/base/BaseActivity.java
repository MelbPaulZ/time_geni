package org.unimelb.itime.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.unimelb.itime.R;

/**
 * use afinal: https://github.com/yangfuhai/afinal
 */
public class BaseActivity extends AppCompatActivity{
    public final static String TASK = "task";
    @Deprecated
    public final static int TASK_INVITE_OTHER_CREATE_EVENT = 1;
    @Deprecated
    public final static int TASK_SELF_CREATE_EVENT  = 2;
    @Deprecated
    public final static int TASK_SELF_DETAIL_EVENT  = 3;

    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getFragmentContainerId(){
        return R.id.setting_activity_framelayout;
    }


    public void openFragment(Fragment fragment) {
        openFragment(fragment, null);
    }

    public void openFragment(Fragment fragment, Bundle bundle){
        fragmentManager = getSupportFragmentManager();
        fragment.setArguments(bundle);
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        t.replace(getFragmentContainerId(), fragment);
        t.addToBackStack(null);
        t.commit();
    }

    public void backFragment(Fragment fragment){
        backFragment(fragment, null);
    }

    public void backFragment(Fragment fragment, Bundle bundle){
        fragmentManager = getSupportFragmentManager();
        fragment.setArguments(bundle);
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        t.replace(getFragmentContainerId(), fragment);
        t.addToBackStack(null);
        t.commit();
    }
}
