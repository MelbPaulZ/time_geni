package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;

import java.util.List;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    @GET("event/fetch")
    Observable<List<Event>> fetch(String calendarId);

}
