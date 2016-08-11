package org.unimelb.itime.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * provide some common methods and initialise parameters
 */
public abstract class BaseUiFragment extends Fragment{

    protected View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    /**
     * use to replace onCreateView
     * @return
     */
    protected int getLayoutId(){
        return 0;
    }

    /** -------- basic tool methods -------------- **/

    public void forward(Class<?> classObj) {
        forward(classObj, null);
    }

    /**
     * start a new activity and finish the current one
     * @param classObj
     * @param bundle
     */
    public void forward(Class<?> classObj, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), classObj);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
        getActivity().finish();
    }

    public void overlay(Class<?> classObj) {
        overlay(classObj, null);
    }

    /**
     * start a new activity and do not finish the current one
     * @param classObj
     * @param bundle
     */
    public void overlay(Class<?> classObj, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), classObj);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        this.startActivity(intent);
    }
}
