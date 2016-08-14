package org.unimelb.itime.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.unimelb.itime.base.C;

/**
 * Created by yinchuandong on 13/08/2016.
 */
public class AuthUtil {

    public static void saveJwtToken(Context context, String jwtToken){
        SharedPreferences.Editor editor = AppUtil.getTokenSaver(context).edit();
        editor.putString(C.spkey.ITIME_JWT_TOKEN, jwtToken);
        editor.apply();
    }

    public static String getJwtToken(Context context){
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        return sp.getString(C.spkey.ITIME_JWT_TOKEN, "");
    }

    public static void clearJwtToken(Context context){
        SharedPreferences.Editor editor = AppUtil.getTokenSaver(context).edit();
        editor.remove(C.spkey.ITIME_JWT_TOKEN);
        editor.apply();
    }
}
