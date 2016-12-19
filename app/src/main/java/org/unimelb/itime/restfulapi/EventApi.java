package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 23/09/2016.
 */
public interface EventApi {
    @GET("event/list/{calendarUid}")
    Observable<HttpResult<List<Event>>> list(@Path("calendarUid") String calendarUid, @Query("syncToken") String syncToken);

    @GET("event/get/{calendarUid}/{eventUid}")
    Observable<HttpResult<Event>> get(@Path("calendarUid") String calendarUid);

    @POST("event/insert")
    Observable<HttpResult<Event>> insert(@Body Event event);

    @POST("event/update/{calendarUid}/{eventUid}")
    Observable<HttpResult<List<Event>>> update(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid, @Body Event event, @Query("syncToken") String syncToken);

    @POST("event/delete/{calendarUid}/{eventUid}")
    Observable<HttpResult<Event>> delete(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid);

    @POST("event/confirm/{calendarUid}/{eventUid}/{timeslotUid}")
    Observable<HttpResult<Event>> confirm(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid, @Path("timeslotUid") String timeslotUid,@Body Event event);

    @POST("event/invitee/accept/{eventUid}")
    Observable<HttpResult<Event>> acceptEvent();

    @POST("event/invitee/quit/{eventUid}")
    Observable<HttpResult<Event>> quitEvent();

    @POST("event/timeslot/accept/{calendarUid}/{eventUid}")
    Observable<HttpResult<Event>> acceptTimeslot(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid, @Body HashMap<String, Object> parameters);

    @POST("event/timeslot/reject/{eventUid}")
    Observable<HttpResult<Event>> rejectTimeslot();

    @FormUrlEncoded
    @POST("event/timeslot/recommend")
    Observable<HttpResult<List<Timeslot>>> recommend(@Field("invitee") List<Invitee> inviteeList, @Field("startRecommendTime") long startRecommendTime);
}
