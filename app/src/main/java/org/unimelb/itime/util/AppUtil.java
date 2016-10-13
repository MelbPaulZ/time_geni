package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.unimelb.itime.base.C;

import java.util.UUID;

/**
 * Created by yinchuandong on 1/07/16.
 */
public class AppUtil {

    public static SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences(C.sp.DEFAULT, Context.MODE_PRIVATE);
    }


    /**
     * all tokens are saved in this shared preference
     * @param ctx
     * @return
     */
    public static SharedPreferences getTokenSaver(Context ctx){
        return ctx.getSharedPreferences(C.sp.TOKEN, Context.MODE_PRIVATE);
    }


    /**
     * to generate a uuid
     * @return
     */
    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }
}
