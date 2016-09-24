package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    @GET("event/fetch/{calendarUid}")
    Observable<List<Event>> fetch(@Query("calendarUid") String calendarUid);

}
