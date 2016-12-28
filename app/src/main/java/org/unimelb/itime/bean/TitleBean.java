package org.unimelb.itime.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;

/**
 * Created by 37925 on 2016/12/4.
 */

public class TitleBean extends BaseObservable {
    private String title;
    private View.OnClickListener backOnClickListener;
    private View.OnClickListener doneOnClickListener;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public View.OnClickListener getBackOnClickListener() {
        return backOnClickListener;
    }

    public void setBackOnClickListener(View.OnClickListener backOnClickListener) {
        this.backOnClickListener = backOnClickListener;
    }

    @Bindable
    public View.OnClickListener getDoneOnClickListener() {
        return doneOnClickListener;
    }

    public void setDoneOnClickListener(View.OnClickListener doneOnClickListener) {
        this.doneOnClickListener = doneOnClickListener;
    }
}
