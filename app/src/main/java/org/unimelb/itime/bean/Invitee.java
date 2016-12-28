package org.unimelb.itime.bean;

import android.support.annotation.Nullable;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.unimelb.itime.vendor.listener.ITimeInviteeInterface;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yuhaoliu on 10/09/2016.
 */
public class Invitee implements ITimeInviteeInterface, Serializable {
    public final static String STATUS_NEEDSACTION = "needsAction";
    public final static String STATUS_ACCEPTED = "accepted";
    public final static String STATUS_DECLINED = "declined";
    public final static String USER_STATUS_ACTIVATED = "activated";
    public final static String USER_STATUS_UNACTIVATED = "unactivated";


    private String eventUid = "";
    private String inviteeUid = "";
    private String userUid = "";
    private String userId = "";
    private String aliasName = "";
    private String aliasPhoto = "";
    private String status = "";
    private String reason = "";
    private int isHost;
    private String userStatus = "";

    @Override
    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Convert(converter = Invitee.SlotResponseConverter.class , columnType = String.class)
    private List<SlotResponse> inviteeTimeslot = new ArrayList<>();

    public static class SlotResponseConverter implements PropertyConverter<List<SlotResponse>,String> {
        Gson gson = new Gson();

        @Override
        public List<SlotResponse> convertToEntityProperty(String databaseValue) {
            Type listType = new TypeToken<List<SlotResponse>>() {}.getType();
            return gson.fromJson(databaseValue, listType);
        }

        @Override
        public String convertToDatabaseValue(List<SlotResponse> entityProperty) {
            return gson.toJson(entityProperty);
        }
    }

    public List<SlotResponse> getSlotResponses() {
        return inviteeTimeslot;
    }

    public void setSlotResponses(ArrayList<SlotResponse> slotResponses) {
        this.inviteeTimeslot = slotResponses;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Nullable
    @Override
    public String getPhoto() {
        return this.getAliasPhoto();
    }

    @Override
    public String getName() {
        return getAliasName();
    }

    @Override
    public String getInviteeUid() {
        return inviteeUid;
    }

    public void setInviteeUid(String inviteeUid) {
        this.inviteeUid = inviteeUid;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasPhoto() {
        return aliasPhoto;
    }

    public void setAliasPhoto(String aliasPhoto) {
        this.aliasPhoto = aliasPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsHost() {
        return isHost;
    }

    public void setIsHost(int isHost) {
        this.isHost = isHost;
    }


}
