package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;



/**
 * Created by Paul on 11/1/17.
 */

public class ToolbarViewModel<V extends ItimeCommonMvpView> extends CommonViewModel {
    private String leftTitleStr, titleStr, rightTitleStr;
    private int leftColor, rightColor;
    private boolean leftClickable = true, rightClickable = false;
    private Drawable leftDrawable, rightDrawable;

    private V mvpView;

    public ToolbarViewModel(V view){
        this.mvpView = view;
        this.leftColor = this.rightColor = Color.parseColor("#000000");
    }

    public View.OnClickListener onClickLeft(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.onBack();
                }
            }
        };
    }

    public View.OnClickListener onClickRight(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.onNext();
                }
            }
        };
    }

    @Bindable
    public String getLeftTitleStr() {
        return leftTitleStr;
    }

    public void setLeftTitleStr(String leftTitleStr) {
        this.leftTitleStr = leftTitleStr;
    }

    @Bindable
    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    @Bindable
    public String getRightTitleStr() {
        return rightTitleStr;
    }

    public void setRightTitleStr(String rightTitleStr) {
        this.rightTitleStr = rightTitleStr;
    }

    @Bindable
    public int getLeftColor() {
        return leftColor;
    }

    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }

    @Bindable
    public int getRightColor() {
        return rightColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    @Bindable
    public boolean isLeftClickable() {
        return leftClickable;
    }

    public void setLeftClickable(boolean leftClickable) {
        this.leftClickable = leftClickable;
        notifyPropertyChanged(BR.leftClickable);
    }

    @Bindable
    public boolean isRightClickable() {
        return rightClickable;
    }

    public void setRightClickable(boolean rightClickable) {
        this.rightClickable = rightClickable;
        notifyPropertyChanged(BR.rightClickable);
    }

    @Bindable
    public Drawable getLeftDrawable() {
        return leftDrawable;
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        notifyPropertyChanged(BR.leftDrawable);
    }

    @Bindable
    public Drawable getRightDrawable() {
        return rightDrawable;
    }

    public void setRightDrawable(Drawable rightDrawable) {
        this.rightDrawable = rightDrawable;
        notifyPropertyChanged(BR.rightDrawable);
    }

    public V getMvpView() {
        return mvpView;
    }

    public void setMvpView(V mvpView) {
        this.mvpView = mvpView;
    }
}
