package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;

/**
 * Created by Qiushuo Huang on 2016/12/22.
 */
public class Block {
    private int status;
    private int id;
    private String blockUid;
    private String userUid;
    private String blockUserUid;
    private int blockLevel;
    private String createdAt;
    private String updatedAt;

    public Block(int status, int id, String blockUid, String userUid, String blockUserUid, int blockLevel, String createdAt, String updatedAt) {
        this.status = status;
        this.id = id;
        this.blockUid = blockUid;
        this.userUid = userUid;
        this.blockUserUid = blockUserUid;
        this.blockLevel = blockLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Block(){}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
