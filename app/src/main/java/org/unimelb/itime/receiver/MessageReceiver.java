package org.unimelb.itime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: ac" + intent.getAction());
        Log.i(TAG, "onReceive: data" + intent.getExtras());
    }
}
