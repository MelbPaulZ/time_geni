package org.unimelb.itime.ui.contact.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 37925 on 2016/12/7.
 */

public class PureEditText extends EditText {

    public PureEditText(Context context) {
        super(context);
        this.setBackground(null);
    }

    public PureEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackground(null);
    }

    public PureEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackground(null);
    }


}
