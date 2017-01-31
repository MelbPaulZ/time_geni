package org.unimelb.itime.ui.presenter;

import android.content.Context;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import org.unimelb.itime.ui.mvpview.MainTabBarView;


/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarPresenter extends MvpBasePresenter<MainTabBarView>{

    private static final String TAG = "MainTabBar";
    private Context context;

    public MainTabBarPresenter(Context context){
        this.context = context;
    }

    public void showFragmentById(int pageId) {
        MainTabBarView tabBarView = getView();
        if (tabBarView == null){
            return;
        }
        // call back ui
        tabBarView.showFragmentById(pageId);
    }
}
