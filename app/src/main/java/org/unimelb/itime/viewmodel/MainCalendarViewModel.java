package org.unimelb.itime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.base.C;
import org.unimelb.itime.model.User;
import org.unimelb.itime.restfulapi.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yinchuandong on 9/08/2016.
 */
public class MainCalendarViewModel extends BaseObservable{
    public final static String TAG = "MainCalendarViewModel";
    private User user;

    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public View.OnClickListener testClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                user.setUsername("yin-changed");
                notifyPropertyChanged(BR.user);

                testHttp();
            }
        };
    }


    private void testHttp(){
        Log.d(TAG, "testHttp: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.api.BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi service = retrofit.create(UserApi.class);
        Call<String> call = service.test();
        call.enqueue(new Callback<String>(){
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + call.toString());
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: ");

            }
        });
    }
}
