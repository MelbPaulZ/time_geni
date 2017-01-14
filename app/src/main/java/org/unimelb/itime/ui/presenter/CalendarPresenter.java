package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.dao.CalendarDao;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulapi.SettingApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPresenter<V extends TaskBasedMvpView<Calendar>> extends MvpBasePresenter<V>{
    private static final String TAG = "CalendarPresenter";
    private static final int TASK_CALENDAR_UPDATE = 0;
    private static final int TASK_CALENDAR_DELETE = 1;
    private static final int TASK_CALENDAR_INSERT = 2;
    private Context context;
    private CalendarApi calendarApi;

    public CalendarPresenter(Context context){
        this.context = context;
        this.calendarApi = HttpUtil.createService(context, CalendarApi.class);
    }

    public List<Calendar> loadCalendarFromDB(){
        AbstractDao queryDao = DBManager.getInstance(context).getQueryDao(Calendar.class);
        return queryDao.queryBuilder().where(CalendarDao.Properties.DeleteLevel.le(0)).list();
    }

    public void update(final Calendar calendar){
        if(getView() != null){
            getView().onTaskStart(TASK_CALENDAR_UPDATE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi.update(calendar.getCalendarUid(),calendar);
        Subscriber<HttpResult<Calendar>> subscriber = new Subscriber<HttpResult<Calendar>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_CALENDAR_UPDATE);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                Calendar oldCal =  DBManager.getInstance(context).find(
                        Calendar.class, "calendarUid",calendar.getCalendarUid()).get(0);
                oldCal.delete();
                DBManager.getInstance(context).insert(Arrays.asList(calendarHttpResult.getData()));
            }

        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void delete(final Calendar calendar){
        if(getView() != null){
            getView().onTaskStart(TASK_CALENDAR_DELETE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi.delete(calendar.getCalendarUid());
        Subscriber<HttpResult<Calendar>> subscriber = new Subscriber<HttpResult<Calendar>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_CALENDAR_DELETE);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                Calendar oldCal =  DBManager.getInstance(context).find(
                        Calendar.class, "calendarUid",calendar.getCalendarUid()).get(0);
                oldCal.delete();

                if(getView() != null){
                    getView().onTaskSuccess(TASK_CALENDAR_DELETE,calendarHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void insert(final Calendar calendar){
        if(getView() != null){
            getView().onTaskStart(TASK_CALENDAR_INSERT);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi.insert(calendar);
        Subscriber<HttpResult<Calendar>> subscriber = new Subscriber<HttpResult<Calendar>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_CALENDAR_INSERT);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                DBManager.getInstance(context).insert(Arrays.asList(calendarHttpResult.getData()));
                if(getView() != null){
                    getView().onTaskSuccess(TASK_CALENDAR_DELETE,calendarHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}