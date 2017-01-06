package org.unimelb.itime.util.googleAuth;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Qiushuo Huang on 2017/1/2.
 */

public class GoogleSignUtil {
    private static GoogleSignUtil instance=null;
    public static final int RESULT_SUCCESS = 1211;
    public static final int RESULT_FAILED = 1212;

    private GoogleSignUtil(){}

    public static GoogleSignUtil getInstance(){
        if(instance==null){
            instance = new GoogleSignUtil();
        }
        return instance;
    }

    public void signIn(Activity activity, int requestCode){
        Intent intent = new Intent();
        intent.setClass(activity, GoogleAuthActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
