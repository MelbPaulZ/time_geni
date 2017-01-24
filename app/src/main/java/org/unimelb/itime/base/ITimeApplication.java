package org.unimelb.itime.base;

import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

import org.unimelb.itime.util.GreenDaoUtil;

/**
 * Created by yinchuandong on 18/08/2016.
 */
public class ITimeApplication extends MultiDexApplication {

    private final static String TAG = "ITimeApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoUtil.init(this);
        registerLeanCloud();
    }

    private void registerLeanCloud(){
        AVOSCloud.initialize(this, "Sk9FQYePVwHdXtXQKQuNfdpr-gzGzoHsz",
                "1PsfeF7pA1S5xI7EmEoQviwT");

//        // 启用崩溃错误统计
//        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
//        AVOSCloud.setLastModifyEnabled(true);
//        AVOSCloud.setDebugLogEnabled(true);
    }
}
