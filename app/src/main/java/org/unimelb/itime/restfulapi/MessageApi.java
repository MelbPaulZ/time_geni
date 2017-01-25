package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Message;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public interface MessageApi {
    @GET("message/list")
    Observable<HttpResult<List<Message>>> get(@Query("syncToken") String syncToken);

    @POST("message/read")
    Observable<HttpResult<List<Message>>> read(@Body HashMap<String, Object> uidHashMap, @Query("syncToken") String syncToken);

    @POST("message/delete")
    Observable<HttpResult<List<Message>>> delete(@Body HashMap<String, Object> uidHashMap, @Query("syncToken") String syncToken);

    @POST("message/clear")
    Observable<HttpResult<List<Message>>> clear(@Query("syncToken") String syncToken);
}
