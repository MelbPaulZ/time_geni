package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;

import retrofit2.Call;
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

    @GET("users/test")
    Call<String> test();

    @FormUrlEncoded
    @POST("users/login")
    Observable<JwtToken> login(@Field("userId") String userId, @Field("password") String password);

    // only refreshToken needs to be original retrofit version
    @GET("users/refresh_token")
    Call<JwtToken> refreshToken(@Query("token") String token);

    @GET("users/list")
    Observable<User> list();

}
