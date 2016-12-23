package org.unimelb.itime.ui.contact.Model;

import android.content.Context;

/**
 * Created by 37925 on 2016/12/3.
 */

public class SizeHelper {
    public static int dp2px(Context context, double dipValue){
        float scale=context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
    }

    public static int px2dp(Context context, double pxValue){
        float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int)((pxValue*160)/scale+0.5f);
    }
}
