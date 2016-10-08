package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    @GET("event/list/{calendarUid}")
    Observable<List<Event>> list(@Query("calendarUid") String calendarUid);

}
