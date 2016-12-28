package org.unimelb.itime.bean;

/**
 * Created by 37925 on 2016/12/9.
 */

public class RequestFriend extends ITimeUser {
    private String displayStatus;
    private FriendRequest request;

    public FriendRequest getRequest() {
        return request;
    }

    public void setRequest(FriendRequest request) {
        this.request = request;
    }

    public RequestFriend(FriendRequest request){
        super(request.getUserDetail());
        this.request = request;
    }

    public RequestFriend(){}

    public String getDisplayStatus() {
        return request.getDisplayStatus();
    }

    public void setDisplayStatus(String displayStatus) {
        this.request.setDisplayStatus(displayStatus);
    }
}
