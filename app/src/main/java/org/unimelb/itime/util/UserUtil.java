package org.unimelb.itime.util;

import android.content.Context;

/**
 * Created by yinchuandong on 20/06/2016.
 */
public class UserUtil {
    private static UserUtil instance;

    private UserUtil(){
    }

    public static UserUtil getInstance(){
        if(instance == null){
            instance = new UserUtil();
        }
        return instance;
    }

    public void login(Context ctx){

    }

    public void logot(Context ctx){

    }


    public int getUserUid(){
        return 1;
    }

}
