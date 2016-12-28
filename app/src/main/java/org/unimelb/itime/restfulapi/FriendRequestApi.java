package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestResult;
import org.unimelb.itime.bean.RequestFriend;
import org.unimelb.itime.bean.RequestReadBody;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Qiushuo Huang on 2016/12/21.
 */

public interface FriendRequestApi {

//    Description: Get all friend requests that is STATUS_SENT TO THIS USER. (Other Users->This User)
    @GET("contact/friend_request/list")
    Observable<HttpResult<FriendRequestResult>> list();

//    Description: Send a friend request to a particular user.
//
//    Parameters:
//    freqUserUid: The user_uid which this user wants to send friend request to.
//            source: Now only supports 'itime'. It may supports gmail/mobile/facebook in the future.
    @POST("contact/friend_request/send/{freqUserUid}/{source}")
    Observable<HttpResult<FriendRequest>> send(@Path("freqUserUid") String freqUserUid, @Path("source") String source);

//    Description: Mark the already read requests as 'is_read = 1' in DB.
    @POST("contact/friend_request/read")
    Observable<HttpResult<Void>> read(@Body RequestReadBody body);

//    Description: Confirm a friend request.
//    Parameter: "freqUid", NOT "freqUserUid".
    @POST("contact/friend_request/confirm/{freqUid}")
    Observable<HttpResult<List<FriendRequest>>> confirm(@Path("freqUid") String freqUid);


}
