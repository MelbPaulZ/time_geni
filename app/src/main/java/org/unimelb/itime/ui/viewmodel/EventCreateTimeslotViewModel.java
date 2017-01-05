package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.EventCreateNewTimeSlotMvpView;
import org.unimelb.itime.ui.presenter.TimeslotCommonPresenter;
import org.unimelb.itime.vendor.BR;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.MyCalendar;
import org.unimelb.itime.vendor.weekview.WeekView;

import java.util.Calendar;

/**
 * Created by Paul on 27/08/2016.
 */
public class EventCreateTimeslotViewModel extends CommonViewModel {

    private String titleString;
    private TimeslotCommonPresenter<EventCreateNewTimeSlotMvpView> presenter;
    private Event event;
    private String tag;
    private ObservableField<Boolean> isChangeDuration = new ObservableField<>(false);
    private String durationTimeString = "1 hour";
    private EventCreateNewTimeSlotMvpView mvpView;

    public EventCreateTimeslotViewModel(TimeslotCommonPresenter<EventCreateNewTimeSlotMvpView> presenter){
        this.presenter = presenter;
        titleString = initToolBarTitle();
        event= EventManager.getInstance(getContext()).getCurrentEvent();
        mvpView = presenter.getView();
    }

    private Context getContext(){
        return presenter.getContext();
    }


    public View.OnClickListener onClickBack(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickBack();
                }
            }
        };
    }

    public View.OnClickListener onClickDone(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mvpView!=null){
                    mvpView.onClickDone();
                }
            }
        };
    }

    public WeekView.OnHeaderListener onWeekViewChange(){
       return new WeekView.OnHeaderListener() {
           @Override
           public void onMonthChanged(MyCalendar myCalendar) {
               setTitleString((EventUtil.getMonth(presenter.getContext(), myCalendar.getMonth()) + " "  + myCalendar.getYear()));
               Calendar calendar = Calendar.getInstance();
               calendar.set(myCalendar.getYear(), myCalendar.getMonth(), myCalendar.getDay(), 0, 0,0 );
               long weekStartTime = calendar.getTimeInMillis();
               presenter.getTimeSlots(event, weekStartTime);
           }
       };
    }

    @Bindable
    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
        notifyPropertyChanged(BR.titleString);
    }

    public String initToolBarTitle(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int delta = -(calendar.get(Calendar.DAY_OF_WEEK)-1);
        calendar.add(Calendar.DATE,delta);
        return EventUtil.getMonth(presenter.getContext(), calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(LinearLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(RelativeLayout view, float height)
    {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)height;
        view.setLayoutParams(layoutParams);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        notifyPropertyChanged(BR.event);
    }

    @Bindable
    public Boolean getIsChangeDuration() {
        return isChangeDuration.get();
    }

    public void setIsChangeDuration(Boolean isChangeDuration) {
        this.isChangeDuration.set(isChangeDuration);
        notifyPropertyChanged(BR.isChangeDuration);
    }

    @Bindable
    public String getDurationTimeString() {
        return durationTimeString;
    }

    public void setDurationTimeString(String durationTimeString) {
        this.durationTimeString = durationTimeString;
        notifyPropertyChanged(BR.durationTimeString);
    }

}
