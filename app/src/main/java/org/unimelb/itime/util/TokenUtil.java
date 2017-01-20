package org.unimelb.itime.util;

import android.content.Context;

import org.antlr.v4.codegen.model.Sync;
import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.SyncToken;
import org.unimelb.itime.dao.SyncTokenDao;
import org.unimelb.itime.managers.DBManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yuhaoliu on 20/01/2017.
 */

public class TokenUtil {
    private static TokenUtil instance;
    private Context context;

    private TokenUtil(Context context) {
        this.context = context;
    }

    public static TokenUtil getInstance(Context context){
        if (instance == null){
            instance = new TokenUtil(context);
        }

        return instance;
    }

    /**
     * for /calendar/list
     * @param userUid
     * @return
     */
    public String getCalendarToken(String userUid){
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(SyncToken.PREFIX_CAL)
                ).list();
        if (calendarToken.size() == 0){
            return "";
        }
        return calendarToken.get(0).getValue();
    }

    public void setCalendarToken(String userUid, String token){
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(SyncToken.PREFIX_CAL)
        ).list();
        if (calendarToken.size() == 0){
            SyncToken newToken = new SyncToken();
            newToken.setName(SyncToken.PREFIX_CAL);
            newToken.setUserUid(userUid);
            newToken.setValue(token);
            DBManager.getInstance(context).insertOrReplace(Arrays.asList(newToken));
            return;
        }

        calendarToken.get(0).setValue(token);
    }

    public String getEventToken(String userUid, String calenderUid){
        String tokenKey = SyncToken.PREFIX_EVENT + calenderUid;
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (calendarToken.size() == 0){
            return "";
        }

        return calendarToken.get(0).getValue();
    }

    /**
     * for event/list/{-r or calendarUid}
     * @param userUid
     * @param calenderUid
     * @param token
     */
    public void setEventToken(String userUid, String calenderUid, String token){
        String tokenKey = SyncToken.PREFIX_EVENT + calenderUid;
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (calendarToken.size() == 0){
            SyncToken newToken = new SyncToken();
            newToken.setName(tokenKey);
            newToken.setUserUid(userUid);
            newToken.setValue(token);
            DBManager.getInstance(context).insertOrReplace(Arrays.asList(newToken));
            return;
        }

        calendarToken.get(0).setValue(token);
    }

    public String getMessageToken(String userUid){
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> messageToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(SyncToken.PREFIX_MESSAGE)
                ).list();
        if (messageToken.size() == 0){
            return "";
        }
        return messageToken.get(0).getValue();
    }

    public void setMessageToken(String userUid, String token){
        AbstractDao dao = DBManager.getInstance(context).getQueryDao(SyncToken.class);
        List<SyncToken> messageToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.UserUid.eq(userUid),
                SyncTokenDao.Properties.Name.eq(SyncToken.PREFIX_MESSAGE)
                ).list();
        if (messageToken.size() == 0){
            SyncToken newToken = new SyncToken();
            newToken.setName(SyncToken.PREFIX_MESSAGE);
            newToken.setUserUid(userUid);
            newToken.setValue(token);
            DBManager.getInstance(context).insertOrReplace(Arrays.asList(newToken));
            return;
        }

        messageToken.get(0).setValue(token);
    }

}
