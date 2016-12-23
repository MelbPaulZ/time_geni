package org.unimelb.itime.ui.contact.Beans;

import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;

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
        super(request.getContact());
        this.request = request;
    }

    public RequestFriend(){}

    public boolean getAccepted() {
        return request.getStatus().equals(FriendRequest.CONFIRMED);
    }

    public void accept(){
        request.setStatus(FriendRequest.CONFIRMED);
    }
}
