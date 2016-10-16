package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.restfulresponse.HttpResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Paul on 15/10/16.
 */
public interface PhotoApi {
    @Multipart
    @POST("event/photo/upload")
    Observable<HttpResult<PhotoUrl>> uploadPhoto(@Part("photoUid") RequestBody photoUid, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("event/photo/update/{calendarUid}/{eventUid}/{photoUid}")
    Observable<HttpResult<Event>> updatePhoto(@Path("calendarUid") String calendarUid, @Path("eventUid") String eventUid, @Path("photoUid") String photoUid, @Field("url") String url);

}
