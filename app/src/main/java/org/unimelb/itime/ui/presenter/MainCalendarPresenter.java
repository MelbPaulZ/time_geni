package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import org.unimelb.itime.base.C;
import org.unimelb.itime.restfulapi.UserApi;
import org.unimelb.itime.ui.mvpview.MainCalendarMvpView;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by yinchuandong on 11/08/2016.
 */
public class MainCalendarPresenter extends MvpBasePresenter<MainCalendarMvpView>{
    private static final String TAG = "LoginPresenter";
    private Context context;

    public MainCalendarPresenter(Context context){
        this.context = context;
    }

    public void testHttp(){
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


    public void gotoEditEventActivity(ITimeEventInterface iTimeEventInterface){
        MainCalendarMvpView mvpView = getView();
        if (mvpView!=null){
            mvpView.startEditEventActivity(iTimeEventInterface);
        }
    }

    public void gotoCreateEventActivity(){
//        EventBus.getDefault().post(new MessageEvent("createNewActivity"));
        MainCalendarMvpView view = getView();
        if (view!=null){
            view.startCreateEventActivity();
        }
    }



}
