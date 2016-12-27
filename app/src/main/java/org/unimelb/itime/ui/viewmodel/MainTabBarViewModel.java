package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.presenter.MainTabBarPresenter;

import static android.view.View.GONE;

/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarViewModel extends BaseObservable{

    private MainTabBarPresenter presenter;
    private String unReadNum;
    private int visible;
    private int unReadFriendRequest;

    public MainTabBarViewModel(MainTabBarPresenter presenter){
        this.presenter = presenter;
        presenter.getRequestCount(new RequestNumCallBack());
    }

    public View.OnClickListener onTabBarClick(final int pageId){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.refreshTabStatus(pageId);
                presenter.showFragmentById(pageId);
            }
        };
    }

    @Bindable
    public int getVisible(){
        return visible;
    }


    @Bindable
    public String getUnReadNum() {
        return unReadNum;
    }

    public void setUnReadNum(String unReadNum) {
        this.unReadNum = unReadNum;
        if (unReadNum.equals("0")){
            visible =  View.GONE;
        }else{
            visible = View.VISIBLE;
        }
        notifyPropertyChanged(BR.unReadNum);
        notifyPropertyChanged(BR.visible);
    }

    @Bindable
    public int getUnReadFriendRequest() {
        return unReadFriendRequest;
    }

    public void setUnReadFriendRequest(int unReadFriendRequest) {
        this.unReadFriendRequest = unReadFriendRequest;
        notifyPropertyChanged(BR.unReadFriendRequest);
    }



    public class RequestNumCallBack{
        public void success(int num){
            setUnReadFriendRequest(num);
        }
    }
}
