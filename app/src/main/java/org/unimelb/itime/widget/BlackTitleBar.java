package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.SizeCollector;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/9.
 */

public class BlackTitleBar  extends BaseTitleBar {

    public BlackTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.BLACK);
        backIcon.setImageResource(R.drawable.ic_left_arrow_white);
        backText.setTextColor(Color.WHITE);
        titleTextView.setTextColor(Color.WHITE);
    }
}
