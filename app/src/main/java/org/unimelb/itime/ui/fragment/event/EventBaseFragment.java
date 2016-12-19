package org.unimelb.itime.ui.fragment.event;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.BaseUiFragment;

/**
 * provide some common methods and initialise parameters
 */
public abstract class EventBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P>{

    private Fragment from;
    private Fragment to;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    public String getClassName(){
        return getClass().getSimpleName();
    }


    public Fragment getFrom() {
        return from;
    }

    public void setFrom(BaseUiFragment from){
        this.from = from;
    }


    public void switchFragment(BaseUiFragment<V, P> from, BaseUiFragment<? extends MvpView, ? extends MvpPresenter> to){
        to.setFrom(from);
        from.onLeave();
        to.onEnter();
        // switch
        getFragmentManager().beginTransaction().hide(from).show(to).commit();
    }

    public <T> void switchFragment(BaseUiFragment<V, P> from, BaseUiFragment<? extends MvpView, ? extends MvpPresenter> to, T t){

    }

    // to is only use for forcing page go to which fragment, use when only necessary, i.e. before sending page has two entrance on timeslot view
    public void setTo(BaseUiFragment to){
        this.to = to;
    }

    public Fragment getTo(){
        return to;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        if (!hidden) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    public void onEnter(){

    }

    public void onLeave(){

    }


}
