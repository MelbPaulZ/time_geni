package org.unimelb.itime.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.restfulapi.UserApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yinchuandong on 12/08/2016.
 */
public class HttpUtil {

    public static <S> S createServiceWithoutToken(final Context context, Class<S> serviceClass){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(C.api.BASE)
                .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient client = httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = retrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

    /**
     * create a http request api service
     * @param context
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createService(final Context context, Class<S> serviceClass) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(C.api.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        // called before sending every request
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String authToken = AuthUtil.getJwtToken(context);
                //String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjEsImlzcyI6Imh0dHA6XC9cL2Rldi50aW1lZ2VuaWkuY29tXC9hcGlcL3VzZXJcL3NpZ25pbiIsImlhdCI6MTQ4MjMxMTIzMiwiZXhwIjoxNDg0OTAzMjMyLCJuYmYiOjE0ODIzMTEyMzIsImp0aSI6ImM1NTZlZjhiNWU2ZDUxMGM0Yzc0MDQ0OGY0OTViYTI5In0.QS0kswULdd-UWVLdG6PJzu2_5gpdW_i6WZap5rynAOs";
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .method(original.method(), original.body());
                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }
        });

        // called if server throws a 500 error, which means token expired
        httpClientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                UserApi userApi = createServiceWithoutToken(context, UserApi.class);
                String authToken = AuthUtil.getJwtToken(context);
                Call<JwtToken> call = userApi.refreshToken(authToken);
                // refresh token needs to be synchronous doing
                retrofit2.Response<JwtToken> refreshRep = call.execute();
                JwtToken jwt = refreshRep.body();
                if (jwt == null){
                    return null;
                }
                AuthUtil.saveJwtToken(context, jwt.getToken());
                return response.request()
                        .newBuilder()
                        .header("Authorization", "Bearer " + jwt.getToken())
                        .build();
            }
        });

        OkHttpClient client = httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS).build();
        Retrofit retrofit = retrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }


    /**
     * for quickly to write an subscriber
     * @param observable
     * @param subscriber
     * @param <T>
     */
    public static <T> void subscribe(Observable<T> observable, Subscriber<T> subscriber){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
