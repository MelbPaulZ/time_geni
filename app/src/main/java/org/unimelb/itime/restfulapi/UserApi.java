package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;
import org.unimelb.itime.restfulresponse.ValidateRes;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yinchuandong on 10/08/2016.
 */
public interface UserApi {

    @GET("user/test")
    Observable<HttpResult<List<User>>> test();

    @FormUrlEncoded
    @POST("user/signin")
    Observable<HttpResult<UserLoginRes>> login(@Field("userId") String userId, @Field("password") String password);

    // only refreshToken needs to be original retrofit version
    @GET("user/refresh_token")
    Call<JwtToken> refreshToken(@Query("token") String token);

    @GET("user/list")
    Observable<User> list();

    //    Description: Search the phone/email of a user. It only returns the user information with precise match of the query.
//    Parameter:{query} is the query the user search for.If search query is Email, then api doesn't return its phone;
//      If search query is phone number, then api doesn't return its email, in order to protect the privacy of users.
//            Example: user/search/abc@gmail.com
    @POST("user/search/{query}")
    Observable<HttpResult<List<User>>> search(@Path("query") String query);

//    Description: Get all block record of this user.
//    "blockLevel" > 0 represents this user is blocked; "blockLevel"==0 represents this user is not blocked.
    @GET("user/block/list")
    Observable<HttpResult<List<Block>>> listBlock();


//  Description: Block a user.
    @POST("user/block/{blockUserUid}")
    Observable<HttpResult<Block>> block(@Path("blockUserUid") int blockUserUid);


    @POST("user/unblock/{blockUserUid}")
    Observable<HttpResult<Block>> unblock(@Path("blockUserUid") int blockUserUid);


    @POST("user/signup")
    Observable<HttpResult<User>> signup(@Body HashMap<String, Object> params);

    @POST("user/profile/update")
    Observable<HttpResult<User>> updateProfile(@Body User user);

    @POST("user/password/update")
    Observable<HttpResult<User>> updatePassword(@Body HashMap<String, Object> params);

    @POST("user/validate")
    Observable<HttpResult<ValidateRes>> validate(@Body HashMap<String, String> params);


}
