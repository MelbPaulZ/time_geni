package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;

import static org.unimelb.itime.R.dimen.hide;

/**
 * Created by Paul on 18/10/16.
 */
public class AndroidViewModel extends BaseObservable {


    @BindingAdapter("android:onFocusChange")
    public static void setOnFocusChangeListener(View view, View.OnFocusChangeListener onFocusChangeListener){
        view.setOnFocusChangeListener(onFocusChangeListener);
    }

    @BindingAdapter("android:onListItemClick")
    public static void setOnListItemClickListener(View view, AdapterView.OnItemClickListener listener){
        ((ListView)view).setOnItemClickListener(listener);
    }

    @BindingAdapter("android:selected")
    public static void setSelected(View view, boolean isSelected){
        view.setSelected(isSelected);
    }


    public void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void closeKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /** set onTouch Listener for view
     *  @param view
     *  @param listener
     * */
    @BindingAdapter("android:onTouch")
    public static void setOnTouchListener(View view, View.OnTouchListener listener){
        view.setOnTouchListener(listener);
    }

    /** The on
     * */
    public View.OnTouchListener onScrollViewTouch(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    v.requestFocus();
                    closeKeyBoard(v);
                }

                return false;
            }
        };
    }



}
