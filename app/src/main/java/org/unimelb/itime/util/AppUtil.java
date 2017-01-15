package org.unimelb.itime.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import org.unimelb.itime.base.C;
import org.unimelb.itime.service.RemoteService;

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
        if (pgBar==null || !pgBar.isShowing()){
            pgBar = ProgressDialog.show(context, title, subtitle);
        }
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
        return AppUtil.getTokenSaver(context).getString(C.spkey.EVENT_LIST_SYNC_TOKEN,"");
    }

    /**
     * save the event sync token into sp
     * @param context
     * @param syncToken
     */
    public static void saveEventSyncToken(Context context, String syncToken){
        SharedPreferences sp = AppUtil.getTokenSaver(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, syncToken);
        editor.apply();
    }

    public static void stopRemoteService(Context context){
        Intent serviceI = new Intent(context, RemoteService.class);
        context.stopService(serviceI);
    }

    public static String getDefaultAlertStr(int key){
        switch (key){
            case -1:
                return "None";
            case 0:
                return "At the time of the event";
            case 5:
                return "5 minutes before";
            case 15:
                return "15 minutes before";
            case 30:
                return "30 minutes before";
            case 60:
                return "1 hour before";
            case 120:
                return "2 hours before";
            case 2*24*60:
                return "2 days before";
            case 7*24*60:
                return "1 week before";
            default:
                return "N/A";

        }
    }

    public static String getGenderStr(String gCode){
        switch (gCode){
            case "0":
                return "Female";
            case "1":
                return "Male";
            case "2":
                return "Undefined";
            default:
                return "N/A";
        }
    }
}
