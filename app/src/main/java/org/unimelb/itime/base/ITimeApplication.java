package org.unimelb.itime.base;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import org.unimelb.itime.util.GreenDaoUtil;

/**
 * Created by yinchuandong on 18/08/2016.
 */
public class ITimeApplication extends MultiDexApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoUtil.init(this);
    }
}
