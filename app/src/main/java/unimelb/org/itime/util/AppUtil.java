package unimelb.org.itime.util;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

import unimelb.org.itime.base.C;

/**
 * Created by yinchuandong on 1/07/16.
 */
public class AppUtil {

    SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences(C.sp.DEFAULT, Context.MODE_PRIVATE);
    }

    SharedPreferences getSharedPreferences(Service service){
        return service.getSharedPreferences(C.sp.DEFAULT, Context.MODE_PRIVATE);
    }
}
