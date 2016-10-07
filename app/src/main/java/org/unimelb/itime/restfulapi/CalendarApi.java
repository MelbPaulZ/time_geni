package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import retrofit2.http.GET;

/**
 * Created by Paul on 24/09/2016.
 */
public interface CalendarApi {
    @GET("calendar/list")
    Observable<HttpResult<List<Calendar>>> fetch();

    @GET("calendar/get/{calendarUid}")
    Observable<HttpResult<Calendar>> get(@Query("calendarUid") String calendarUid);

    @POST("calendar/insert")
    Observable<HttpResult<Void>> insert(Event event);

    @POST("calendar/update/{calendarUid}")
    Observable<HttpResult<Void>> update(@Query("calendarUid") String calendarUid);

    @POST("calendar/delete/{calendarUid}")
    Observable<HttpResult<Void>> delete(@Query("calendarUid") String calendarUid);

    @POST("calendar/clear/{calendarUid}")
    Observable<HttpResult<Void>> clear(@Query("calendarUid") String calendarUid);

    @POST("calendar/share/")
    Observable<HttpResult<Void>> share();
}
