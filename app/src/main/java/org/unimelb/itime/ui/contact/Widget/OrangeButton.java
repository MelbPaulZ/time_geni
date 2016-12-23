package org.unimelb.itime.ui.contact.Widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;

import org.unimelb.itime.R;

/**
 * Created by 37925 on 2016/12/11.
 */

public class OrangeButton extends Button {
    public OrangeButton(Context context) {
        super(context);
        init();
    }

    public OrangeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrangeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        this.setBackgroundResource(R.drawable.orange_button_selector);
        this.setTextColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
        this.setAllCaps(false);
    }
}
