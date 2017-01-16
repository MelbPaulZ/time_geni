package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */
@Entity
public class Block {
    private int status;
    @Id
    private String blockUid;
    private String userUid;
    private String blockUserUid;
    private int blockLevel;
    private String createdAt;
    private String updatedAt;

    @Convert(converter = User.UserConverter.class , columnType = String.class)
    private User userDetail;

    @Generated(hash = 996069551)
    public Block(int status, String blockUid, String userUid, String blockUserUid, int blockLevel, String createdAt,
            String updatedAt, User userDetail) {
        this.status = status;
        this.blockUid = blockUid;
        this.userUid = userUid;
        this.blockUserUid = blockUserUid;
        this.blockLevel = blockLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userDetail = userDetail;
    }

    public Block(){}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getBlockUid() {
        return blockUid;
    }

    public void setBlockUid(String blockUid) {
        this.blockUid = blockUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getBlockUserUid() {
        return blockUserUid;
    }

    public void setBlockUserUid(String blockUserUid) {
        this.blockUserUid = blockUserUid;
    }

    public int getBlockLevel() {
        return blockLevel;
    }

    public void setBlockLevel(int blockLevel) {
        this.blockLevel = blockLevel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }
}
