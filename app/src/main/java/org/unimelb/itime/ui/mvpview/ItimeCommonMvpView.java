package org.unimelb.itime.ui.mvpview;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;

/**
 * Created by Paul on 5/1/17.
 */

public interface ItimeCommonMvpView extends MvpView {
    ToolbarViewModel getToolbarViewModel();
    void onBack();
    void onNext();
}
