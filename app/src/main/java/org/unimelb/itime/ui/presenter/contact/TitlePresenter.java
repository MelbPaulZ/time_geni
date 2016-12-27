package org.unimelb.itime.ui.presenter.contact;

import android.app.Activity;
import android.view.View;

/**
 * Created by 37925 on 2016/12/13.
 */

public class TitlePresenter {
    private String title;
    private boolean showRight;
    private boolean showBack;
    private Activity activity;
    private View.OnClickListener backOnClickListener;
    private View.OnClickListener rightOnClickListener;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
    }

    public boolean isShowBack() {
        return showBack;
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public View.OnClickListener getBackOnClickListener() {
        return backOnClickListener;
    }

    public void setBackOnClickListener(View.OnClickListener backOnClickListener) {
        this.backOnClickListener = backOnClickListener;
    }

    public View.OnClickListener getRightOnClickListener() {
        return rightOnClickListener;
    }

    public void setRightOnClickListener(View.OnClickListener rightOnClickListener) {
        this.rightOnClickListener = rightOnClickListener;
    }
}
