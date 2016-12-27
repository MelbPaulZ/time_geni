package org.unimelb.itime.bean;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2016/12/25.
 */

public class FriendRequestResult {
    List<FriendRequest> receive;
    List<FriendRequest> send;

    public List<FriendRequest> getReceive() {
        return receive;
    }

    public void setReceive(List<FriendRequest> receive) {
        this.receive = receive;
    }

    public List<FriendRequest> getSend() {
        return send;
    }

    public void setSend(List<FriendRequest> send) {
        this.send = send;
    }
}

