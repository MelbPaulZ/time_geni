package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yuhaoliu on 20/01/2017.
 */
@Entity
public class SyncToken {
    public final static String PREFIX_CAL = "calendar_list";
    public final static String PREFIX_EVENT = "event_list";
    public final static String PREFIX_MESSAGE = "message_list";

    private String userUid = "";
    private String name = "";
    private String value = "";
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserUid() {
        return this.userUid;
    }
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    @Generated(hash = 2051447372)
    public SyncToken(String userUid, String name, String value) {
        this.userUid = userUid;
        this.name = name;
        this.value = value;
    }
    @Generated(hash = 1307131303)
    public SyncToken() {
    }
}
