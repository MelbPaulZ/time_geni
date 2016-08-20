package org.unimelb.itime.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.unimelb.itime.base.C;
import org.unimelb.itime.dao.DaoMaster;
import org.unimelb.itime.dao.DaoSession;

/**
 * Created by yinchuandong on 20/08/2016.
 */
public class GreenDaoUtil {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;


    public static void init(Context context){
        getDaoMaster(context);
        getDaoSession(context);
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, C.dao.SCHEMA_NAME, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            daoSession = getDaoMaster(context).newSession();
        }
        return daoSession;
    }
}
