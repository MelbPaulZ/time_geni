package org.unimelb.itime.bean;

/**
 * Created by 37925 on 2016/12/9.
 */

public class RequestFriend extends ITimeUser {
    private boolean accepted = false;
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

    public boolean getAccepted() {
        return request.getStatus().equals(FriendRequest.STATUS_CONFIRMED);
    }

    public void accept(){
        request.setStatus(FriendRequest.STATUS_CONFIRMED);
    }
}
