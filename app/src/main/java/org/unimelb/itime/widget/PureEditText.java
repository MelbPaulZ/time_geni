package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by 37925 on 2016/12/7.
 */

public class PureEditText extends EditText {

    private OnFocusChangeListener onFocusChangeListener;

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    public PureEditText(Context context) {
        super(context);
        init();
    }

    public PureEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PureEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        this.setBackground(null);
        this.setPadding(0,0,0,0);
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showKeyBoard();
                }else{
                    closeKeyBoard();
                }
                if(onFocusChangeListener!=null){
                    onFocusChangeListener.onFocusChange(view, hasFocus);
                }
            }
        });
    }

    public void showKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
    }

    public void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
