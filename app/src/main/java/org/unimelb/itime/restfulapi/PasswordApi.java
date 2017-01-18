package org.unimelb.itime.restfulapi;

import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.HashMap;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Paul on 23/12/2016.
 */

public interface PasswordApi {

    @POST("password/send_reset_link")
    public Observable<HttpResult<Void>> sendResetLink(@Body HashMap<String, Object> parameters);

}
