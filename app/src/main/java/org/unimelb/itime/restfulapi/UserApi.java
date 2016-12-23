package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @POST("user/signup")
    Observable<HttpResult<User>> signup(@Body HashMap<String, Object> params);

}
