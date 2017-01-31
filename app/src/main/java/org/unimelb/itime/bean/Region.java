package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Paul on 30/1/17.
 */

@Entity
public class Region {
    @Id
    private long locationId;
    private String name;
    private int locationType;
    private long parentId;
    private int isVisible;


    @Generated(hash = 964907970)
    public Region(long locationId, String name, int locationType, long parentId,
            int isVisible) {
        this.locationId = locationId;
        this.name = name;
        this.locationType = locationType;
        this.parentId = parentId;
        this.isVisible = isVisible;
    }

    @Generated(hash = 600106640)
    public Region() {
    }


    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }
}
