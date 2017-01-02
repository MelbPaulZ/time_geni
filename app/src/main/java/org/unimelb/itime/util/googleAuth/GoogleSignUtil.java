package org.unimelb.itime.util.googleAuth;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Qiushuo Huang on 2017/1/2.
 */

public class GoogleSignUtil {
    private static GoogleSignUtil instance=null;
    private String authCode = null;

    private GoogleSignUtil(){}

    public static GoogleSignUtil getInstance(){
        if(instance==null){
            instance = new GoogleSignUtil();
        }
        return instance;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void signIn(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity, GoogleAuthActivity.class);
        activity.startActivity(intent);
    }
}
