package org.unimelb.itime.restfulapi;

import com.alibaba.fastjson.JSON;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Observable<HttpResult<Void>> read(@Body HashMap<String, Object> uidHashMap);

    @POST("message/delete")
    Observable<HttpResult<Void>> delete(@Body HashMap<String, Object> uidHashMap);
}
