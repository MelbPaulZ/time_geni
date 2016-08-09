package org.unimelb.itime.fragment;


import android.util.Log;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

/**
 * required login, need to extend BaseUiAuthFragment
 */
public class MainCalendarFragment extends BaseUiAuthFragment{

    private final static String TAG = "MainCalendarFragment";

    String url = "http://itime.demo.com/api/";

    public MainCalendarFragment() {
        
        testHttp();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calendar;
    }


    private void testHttp(){
        Log.d(TAG, "testHttp: ");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ITimeService service = retrofit.create(ITimeService.class);
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


    interface ITimeService{
        @GET("users/test")
        Call<String> test();
    }

}
