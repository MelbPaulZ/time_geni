package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 37925 on 2016/12/7.
 */

public class PureEditText extends EditText {

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
    }


}
