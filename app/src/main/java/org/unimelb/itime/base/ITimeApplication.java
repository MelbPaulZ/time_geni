package org.unimelb.itime.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.unimelb.itime.dao.DaoMaster;
import org.unimelb.itime.dao.DaoSession;

/**
 * Created by yinchuandong on 18/08/2016.
 */
public class ITimeApplication extends Application{
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "itime-db", null);
            SQLiteDatabase db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
        }
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster().newSession();
        }
        return daoSession;
    }
}
