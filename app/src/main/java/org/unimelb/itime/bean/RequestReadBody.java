package org.unimelb.itime.bean;

/**
 * Created by Qiushuo Huang on 2016/12/23.
 */

public class RequestReadBody {
    private String[] friend_requests;

    public RequestReadBody(String[] friend_requests) {
        this.friend_requests = friend_requests;
    }

    public String[] getFriend_requests() {
        return friend_requests;
    }

    public void setFriend_requests(String[] friend_requests) {
        this.friend_requests = friend_requests;
    }
}
