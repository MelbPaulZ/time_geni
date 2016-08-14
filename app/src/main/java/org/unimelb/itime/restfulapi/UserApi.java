package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.bean.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by yinchuandong on 10/08/2016.
 */
public interface UserApi {

    @GET("users/test")
    Call<String> test();

    @FormUrlEncoded
    @POST("users/login")
    Call<JwtToken> login(@Field("userId") String userId, @Field("password") String password);

    @GET("users/refresh_token")
    Call<JwtToken> refreshToken();

    @GET("users/list")
    Call<User> list();
}
