package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Setting;
import org.unimelb.itime.restfulresponse.HttpResult;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yinchuandong on 10/08/2016.
 */
public interface SettingApi {

    @GET("setting/get")
    Observable<HttpResult<Setting>> get();

    @POST("setting/update")
    Observable<HttpResult<Setting>> update(@Body Setting setting);


}
