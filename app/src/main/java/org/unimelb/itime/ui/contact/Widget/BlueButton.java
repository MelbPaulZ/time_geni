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

public class BlueButton extends Button {
    public BlueButton(Context context) {
        super(context);
        init();
    }

    public BlueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        this.setBackgroundResource(R.drawable.blue_button_selector);
        this.setTextColor(Color.WHITE);
        this.setGravity(Gravity.CENTER);
        this.setAllCaps(false);
    }
}
