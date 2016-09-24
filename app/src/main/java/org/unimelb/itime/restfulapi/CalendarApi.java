package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Calendar;

import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Paul on 24/09/2016.
 */
public interface CalendarApi {
    @GET("calendar/fetch")
    Observable<Calendar[]> fetch();
}
