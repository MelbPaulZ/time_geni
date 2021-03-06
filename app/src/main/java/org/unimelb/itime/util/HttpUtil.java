package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.JwtToken;
import org.unimelb.itime.restfulapi.UserApi;

import java.io.IOException;

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

/**
 * Created by yinchuandong on 12/08/2016.
 */
public class HttpUtil {

    public static <S> S createServiceWithoutToken(final Context context, Class<S> serviceClass){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(C.api.BASE)
                .addConverterFactory(GsonConverterFactory.create());
        OkHttpClient client = httpClientBuilder.build();
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

        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String authToken = AuthUtil.getJwtToken(context);
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + authToken)
                        .method(original.method(), original.body());
                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }
        });
        httpClientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                UserApi userApi = createServiceWithoutToken(context, UserApi.class);
                String authToken = AuthUtil.getJwtToken(context);
                Call<JwtToken> call = userApi.refreshToken(authToken);
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

        OkHttpClient client = httpClientBuilder.build();
        Retrofit retrofit = retrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

}
