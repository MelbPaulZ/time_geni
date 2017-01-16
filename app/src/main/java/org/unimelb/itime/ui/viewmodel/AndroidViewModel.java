package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.vendor.helper.DensityUtil;

/**
 * Created by Paul on 18/10/16.
 */
public class AndroidViewModel extends BaseObservable {
    @BindingAdapter("android:onTextChange")
    public static void setOnFocusChangeListener(EditText view, TextWatcher watcher){
        view.addTextChangedListener(watcher);
    }

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

    @BindingAdapter("android:onCheckChange")
    public static void setOnSwitchChangeListener(Switch view, Switch.OnCheckedChangeListener listener){
        view.setOnCheckedChangeListener(listener);
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


    @BindingAdapter({"bind:remote_url"})
    public static void loadRemoteUrl(ImageView imageView, String url){
            imageView.setVisibility(View.VISIBLE);
            int size = DensityUtil.dip2px(imageView.getContext(), 40);
            Picasso.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.invitee_selected_default_picture)
                    .resize(size, size)
                    .centerCrop()
                    .into(imageView);
    }



}
