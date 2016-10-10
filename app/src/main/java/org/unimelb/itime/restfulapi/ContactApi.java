package org.unimelb.itime.restfulapi;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.restfulresponse.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Paul on 10/10/16.
 */
public interface ContactApi {
    @GET("contact/list")
    Observable<HttpResult<List<Contact>>> list();

    @GET("contact/get/{contactUid}")
    Observable<HttpResult<Contact>> get(@Path("contactUid") String contactUid);

    @POST("contact/insert")
    Observable<HttpResult<Void>> insert(Event event);

    @POST("contact/update/{contactUid}")
    Observable<HttpResult<Void>> update(@Path("contactUid") String contactUid);

    @POST("contact/delete/{contactUid}")
    Observable<HttpResult<Void>> delete(@Path("contactUid") String contactUid);

    @POST("contact/clear/{contactUid}")
    Observable<HttpResult<Void>> clear(@Path("contactUid") String contactUid);

}