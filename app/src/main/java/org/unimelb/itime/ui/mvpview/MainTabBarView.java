package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by yinchuandong on 16/08/2016.
 */
public interface MainTabBarView extends MvpView{

    void showFragmentById(int pageId);
    void refreshTabStatus(int pageId);
}
