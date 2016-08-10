package org.unimelb.itime.restfulapi;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by yinchuandong on 10/08/2016.
 */
public interface UserApi {

    @GET("users/test")
    Call<String> test();
}
