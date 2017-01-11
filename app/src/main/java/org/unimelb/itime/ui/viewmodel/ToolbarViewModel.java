package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;



/**
 * Created by Paul on 11/1/17.
 */

public class ToolbarViewModel<V extends ItimeCommonMvpView> extends CommonViewModel {
    private String leftTitleStr, titleStr, rightTitleStr;
    private V mvpView;

    public ToolbarViewModel(V view){
        this.mvpView = view;
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
}
