package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCalendarMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;
import org.unimelb.itime.bean.Calendar;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by yuhaoliu on 12/01/2017.
 */

public class CalendarViewModel<T extends ItimeCommonMvpView & TaskBasedMvpView<Calendar>> extends CommonViewModel {
    private CalendarPresenter<T> presenter;
    //for creating calendar, others == null
    private Calendar calendar;
    private ToolbarViewModel toolbarViewModel;
    private ItemView calItemView;
    protected SettingCalendarMvpView mvpView;

    private List<CalendarViewModel.CalendarWrapper> calWrapperList = new ArrayList<>();

    public CalendarViewModel(CalendarPresenter presenter){
        this.presenter = presenter;
        if(presenter.getView() instanceof SettingCalendarMvpView){
            this.mvpView = (SettingCalendarMvpView) presenter.getView();
            setCalendars(presenter.loadCalendarFromDB());
        }
    }

    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    public void setToolbarViewModel(ToolbarViewModel toolbarViewModel) {
        this.toolbarViewModel = toolbarViewModel;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public View.OnClickListener onAddCalendarClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvpView.toAddCalendar();
            }
        };
    }

    public void setCalendars(List<Calendar> cals){
       this.calWrapperList.clear();
        for (Calendar cal:cals
             ) {
            this.calWrapperList.add(getWrapper(cal));
        }
    }

    public View.OnClickListener onDeleteCalendarClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toDeleteCalendar(calendar);
                }
            }
        };
    }

    public View.OnClickListener onCreateDoneClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.insert(calendar);
            }
        };
    }

    public View.OnClickListener onEditDoneClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.update(calendar);
            }
        };
    }

    public TextWatcher onEditTextChanged(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (toolbarViewModel != null){
                    int length = s.toString().replace(" ", "").length();
                    toolbarViewModel.setRightClickable(length!=0);
                }
            }
        };
    }

    private CalendarWrapper getWrapper(Calendar cal){
        return new CalendarWrapper(cal, mvpView);
    }

    public AdapterView.OnItemClickListener onCalItemClicked(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < calWrapperList.size()){
                    CalendarViewModel.CalendarWrapper wrapper = calWrapperList.get(i);
                    wrapper.setSelected(!wrapper.isSelected());
                    presenter.update(wrapper.calendar);
                }
            }
        };
    }

    @Bindable
    public List<CalendarViewModel.CalendarWrapper> getCalWrapperList() {
        return calWrapperList;
    }

    public void setCalWrapperList(List<CalendarViewModel.CalendarWrapper> calWrapperList) {
        this.calWrapperList = calWrapperList;
        notifyPropertyChanged(BR.calWrapperList);
    }

    @Bindable
    public ItemView getCalItemView() {
        return calItemView;
    }

    public void setCalItemView(ItemView calItemView) {
        this.calItemView = calItemView;
        notifyPropertyChanged(BR.calItemView);
    }

    public static class CalendarWrapper extends BaseObservable {
        private Calendar calendar;
        private SettingCalendarMvpView mvpView;

        CalendarWrapper(Calendar calendar, SettingCalendarMvpView mvpView){
            this.calendar = calendar;
            this.mvpView = mvpView;
        }

        @Bindable
        public String getName() {
            return calendar.getSummary();
        }

        public void setName(String name) {
            this.calendar.setSummary(name);
            notifyPropertyChanged(BR.name);
        }

        @Bindable
        public boolean isSelected() {
            return this.calendar.getVisibility() == 1;
        }

        public void setSelected(boolean selected) {
            this.calendar.setVisibility(selected?1:0);
            notifyPropertyChanged(BR.selected);
        }

        public View.OnClickListener onMoreInfoClick(){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvpView.toEditCalendar(calendar);
                }
            };
        }
    }
}
