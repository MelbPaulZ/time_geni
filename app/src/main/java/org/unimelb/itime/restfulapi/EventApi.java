package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    @GET("event/list/{calendarUid}")
    Observable<HttpResult<List<Event>>> list(@Path("calendarUid") String calendarUid);

    @GET("event/get/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> get(@Path("calendarUid") String calendarUid);

    @POST("event/insert")
    Observable<HttpResult<List<Event>>> insert(@Body Event event);

    @POST("event/update/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> update(@Body Event event);

    @POST("event/delete/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> delete();

    @POST("event/confirm/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> confirm();

    @POST("event/invitee/accept/{eventUid}")
    Observable<HttpResult<List<Event>>> acceptEvent();

    @POST("event/invitee/quit/{eventUid}")
    Observable<HttpResult<List<Event>>> quitEvent();

    @POST("event/timeslot/accept/{eventUid}")
    Observable<HttpResult<List<Event>>> acceptTimeslot();

    @POST("event/timeslot/reject/{eventUid}")
    Observable<HttpResult<List<Event>>> rejectTimeslot();

    @FormUrlEncoded
    @POST("event/timeslot/recommend")
    Observable<HttpResult<List<TimeSlot>>> recommend(@Field("invitee") List<Invitee> inviteeList, @Field("startTime") long startTime);


}
