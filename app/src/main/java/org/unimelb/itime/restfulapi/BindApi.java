package org.unimelb.itime.restfulapi;

import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.restfulresponse.UserLoginRes;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Qiushuo Huang on 2017/1/6.
 */

public interface BindApi {
    @GET("account/bind")
    Observable<HttpResult<Void>> bindGoogle(@Query("code") String authCode);
}
