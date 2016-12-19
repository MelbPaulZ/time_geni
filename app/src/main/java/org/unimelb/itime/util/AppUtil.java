package org.unimelb.itime.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import org.unimelb.itime.base.C;

import java.util.UUID;

/**
 * Created by yinchuandong on 1/07/16.
 */
public class AppUtil {

    private static ProgressDialog pgBar;

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
     * to generate a uuid (random uuid of any)
     * @return
     */
    public static String generateUuid(){
        return UUID.randomUUID().toString();
    }

    public static void showProgressBar(Context context,String title, String subtitle){
        pgBar = ProgressDialog.show(context, title, subtitle);
    }

    public static void hideProgressBar(){
        if(pgBar != null){
            pgBar.dismiss();
        }
    }

    /**
     * read event sync token into
     * @param context
     * @return
     */
    public static String getEventSyncToken(Context context){
        return AppUtil.getSharedPreferences(context).getString(C.spkey.EVENT_LIST_SYNC_TOKEN,"");
    }

    /**
     * save the event sync token into sp
     * @param context
     * @param syncToken
     */
    public static void saveEventSyncToken(Context context, String syncToken){
        SharedPreferences sp = AppUtil.getSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, syncToken);
        editor.apply();
    }
}
