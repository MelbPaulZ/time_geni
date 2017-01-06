package org.unimelb.itime.adapter;

import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by yuhaoliu on 6/01/2017.
 */

public abstract class BindingAdapter {
    @android.databinding.BindingAdapter("android:textStyle")
    public static void setTextStyle(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }
}
