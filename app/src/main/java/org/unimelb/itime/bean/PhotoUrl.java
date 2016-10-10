package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 16/09/2016.
 */
@Entity
public class PhotoUrl {

    private String url;
    private String photoUid;
    private String eventUid;
    private String filename;

    public PhotoUrl(String url){
        this.url = url;
    }

    @Generated(hash = 161510692)
    public PhotoUrl(String url, String photoUid, String eventUid, String filename) {
        this.url = url;
        this.photoUid = photoUid;
        this.eventUid = eventUid;
        this.filename = filename;
    }

    @Generated(hash = 1214604864)
    public PhotoUrl() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhotoUid() {
        return photoUid;
    }

    public void setPhotoUid(String photoUid) {
        this.photoUid = photoUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
