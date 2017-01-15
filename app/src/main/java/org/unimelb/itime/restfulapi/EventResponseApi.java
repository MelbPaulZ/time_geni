package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.EventResponse;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yinchuandong on 15/1/17.
 */

public interface EventResponseApi {

    @GET("response/list/{eventUid}")
    Observable<HttpResult<List<EventResponse>>> list(@Path("eventUid") String eventUid, @Query("syncToken") String syncToken);
}
