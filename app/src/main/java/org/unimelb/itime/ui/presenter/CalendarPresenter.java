package org.unimelb.itime.ui.presenter;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.AbstractDao;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.dao.CalendarDao;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.restfulapi.CalendarApi;
import org.unimelb.itime.restfulresponse.HttpResult;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.util.HttpUtil;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yinchuandong on 11/1/17.
 */

public class CalendarPresenter<V extends TaskBasedMvpView<Calendar>> extends MvpBasePresenter<V>{
    public static final String TAG = "CalendarPresenter";
    public static final int TASK_CALENDAR_UPDATE = 0;
    public static final int TASK_CALENDAR_DELETE = 1;
    public static final int TASK_CALENDAR_INSERT = 2;

    private Context context;
    private CalendarApi calendarApi;

    public CalendarPresenter(Context context){
        this.context = context;
        this.calendarApi = HttpUtil.createService(context, CalendarApi.class);
    }

    public Context getContext() {
        return context;
    }

    private void reloadLocalCalendars(Calendar calendar){
        Calendar oldCal =  DBManager.getInstance(context).find(
                Calendar.class, "calendarUid", calendar.getCalendarUid()).get(0);
        oldCal.delete();
        DBManager.getInstance(context).insertOrReplace(Arrays.asList(calendar));

        EventManager.getInstance(context).refreshEventManager();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
    }

    public void update(final Calendar calendar){
        if(getView() != null){
            getView().onTaskStart(TASK_CALENDAR_UPDATE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi
                .update(calendar.getCalendarUid(),calendar)
                .map(new Func1<HttpResult<Calendar>, HttpResult<Calendar>>() {
                    @Override
                    public HttpResult<Calendar> call(HttpResult<Calendar> calendarHttpResult) {
                        if(calendarHttpResult.getStatus() == 1){
                            reloadLocalCalendars(calendarHttpResult.getData());
                        }

                        return calendarHttpResult;
                    }
                });

        Subscriber<HttpResult<Calendar>> subscriber = new Subscriber<HttpResult<Calendar>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_CALENDAR_UPDATE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {

                if(getView() != null){
                    getView().onTaskSuccess(TASK_CALENDAR_UPDATE,calendarHttpResult.getData());
                }
            }

        };
        HttpUtil.subscribe(observable, subscriber);
    }

    public void delete(final Calendar calendar){
        if(getView() != null){
            getView().onTaskStart(TASK_CALENDAR_DELETE);
        }

        Observable<HttpResult<Calendar>> observable = calendarApi
                .delete(calendar.getCalendarUid())
                .map(new Func1<HttpResult<Calendar>, HttpResult<Calendar>>() {
                    @Override
                    public HttpResult<Calendar> call(HttpResult<Calendar> calendarHttpResult) {
                        if(calendarHttpResult.getStatus() == 1){
                            reloadLocalCalendars(calendarHttpResult.getData());
                        }
                        return calendarHttpResult;
                    }
                });
        Subscriber<HttpResult<Calendar>> subscriber = new Subscriber<HttpResult<Calendar>>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");
                if(getView() != null){
                    getView().onTaskError(TASK_CALENDAR_DELETE, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
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
                    getView().onTaskError(TASK_CALENDAR_INSERT, null);
                }
            }

            @Override
            public void onNext(HttpResult<Calendar> calendarHttpResult) {
                DBManager.getInstance(context).insertOrReplace(Arrays.asList(calendarHttpResult.getData()));
                if(getView() != null){
                    getView().onTaskSuccess(TASK_CALENDAR_INSERT,calendarHttpResult.getData());
                }
            }
        };
        HttpUtil.subscribe(observable, subscriber);
    }
}
