package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.bean.SyncToken;
import org.unimelb.itime.dao.SyncTokenDao;
import org.unimelb.itime.managers.DBManager;

import java.util.List;

/**
 * Created by yuhaoliu on 20/01/2017.
 */

public class TokenUtil {
    private static TokenUtil instance;
    private Context context;
    private DBManager dbManager;

    private TokenUtil(Context context) {
        this.context = context;
        this.dbManager = DBManager.getInstance(context);
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
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CAL;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (calendarToken.size() == 0){
            return "";
        }
        return calendarToken.get(0).getValue();
    }

    public void setCalendarToken(String userUid, String token){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CAL;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getEventToken(String userUid, String calenderUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_EVENT + "_" + calenderUid;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)).list();
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
        String tokenKey = userUid + "_" + SyncToken.PREFIX_EVENT + "_" + calenderUid;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getMessageToken(String userUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_MESSAGE;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> messageToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (messageToken.size() == 0){
            return "";
        }
        return messageToken.get(0).getValue();
    }

    public void setMessageToken(String userUid, String token){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_MESSAGE;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

}
