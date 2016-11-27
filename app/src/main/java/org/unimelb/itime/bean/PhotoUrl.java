package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 16/09/2016.
 */
public class PhotoUrl {

    private String url = "";
    private String photoUid = "";
    private String eventUid = "";
    private String filename = "";
    private String localPath = "";
    private int success;

    public PhotoUrl(String url){
        this.url = url;
    }

    public PhotoUrl(){

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





    public String getLocalPath() {
        return this.localPath;
    }



    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }



    public int getSuccess() {
        return this.success;
    }



    public void setSuccess(int success) {
        this.success = success;
    }
}
