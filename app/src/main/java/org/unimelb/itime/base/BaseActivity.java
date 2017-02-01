package org.unimelb.itime.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;

/**
 * use afinal: https://github.com/yangfuhai/afinal
 */
public abstract class BaseActivity<V extends MvpView, P extends MvpBasePresenter<V>> extends MvpActivity<V, P> {
    public final static String TASK = "task";
    @Deprecated
    public final static int TASK_INVITE_OTHER_CREATE_EVENT = 1;
    @Deprecated
    public final static int TASK_SELF_CREATE_EVENT  = 2;
    @Deprecated
    public final static int TASK_SELF_DETAIL_EVENT  = 3;

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }



    protected int getFragmentContainerId(){
        return R.id.setting_activity_framelayout;
    }

    public void clearFragmentStack(){
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void openFragment(Fragment fragment) {
        openFragment(fragment, null, true);
    }

    public void openFragment(Fragment fragment, Bundle bundle){
        openFragment(fragment, bundle, true);
    }

    public void openFragment(Fragment fragment, Bundle bundle, boolean isAddedToStack){
        fragmentManager = getSupportFragmentManager();
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        t.replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName());

        if(isAddedToStack){
            t.addToBackStack(fragment.getClass().getSimpleName());
        }
        t.commit();
        fragmentManager.executePendingTransactions();
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
//        t.addToBackStack(null);
        t.commit();
        fragmentManager.executePendingTransactions();
    }
}
