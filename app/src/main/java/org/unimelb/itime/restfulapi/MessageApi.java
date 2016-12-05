package org.unimelb.itime.restfulapi;

import com.alibaba.fastjson.JSON;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yuhaoliu on 1/12/16.
 */
public interface MessageApi {
    @GET("message/list_group")
    Observable<HttpResult<List<Message>>> get();

    @FormUrlEncoded
    @POST("message/read")
    Observable<HttpResult<Void>> read(@Field("messageUids") List<Integer> uids, @Field("isRead") int isRead);
}
