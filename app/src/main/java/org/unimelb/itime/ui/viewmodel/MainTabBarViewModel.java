package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import org.unimelb.itime.ui.presenter.MainTabBarPresenter;

/**
 * Created by yinchuandong on 16/08/2016.
 */
public class MainTabBarViewModel extends BaseObservable{

    private MainTabBarPresenter presenter;

    public MainTabBarViewModel(MainTabBarPresenter presenter){
        this.presenter = presenter;
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


}
