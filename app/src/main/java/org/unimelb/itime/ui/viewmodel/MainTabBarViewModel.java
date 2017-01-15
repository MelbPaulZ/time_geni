package org.unimelb.itime.ui.viewmodel;

import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.presenter.MainTabBarPresenter;


/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarViewModel extends CommonViewModel{

    private MainTabBarPresenter presenter;
    private String unReadNum;
    private int visible;
    private int unReadFriendRequest;

    private boolean[] tabSelectedArr = {true, false, false, false};

    public MainTabBarViewModel(MainTabBarPresenter presenter){
        this.presenter = presenter;
        presenter.getRequestCount();
    }

    public View.OnClickListener onTabBarClick(final int pageId){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < tabSelectedArr.length; i++){
                    if(pageId == i){
                        tabSelectedArr[i] = true;
                    }else{
                        tabSelectedArr[i] = false;
                    }
                }
                notifyPropertyChanged(BR.tabSelectedArr);
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


    @Bindable
    public boolean[] getTabSelectedArr() {
        return tabSelectedArr;
    }

    public void setTabSelectedArr(boolean[] tabSelectedArr) {
        this.tabSelectedArr = tabSelectedArr;
        notifyPropertyChanged(BR.tabSelectedArr);
    }
}
