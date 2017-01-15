package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/8.
 */

public class PlusTitleBar extends BaseTitleBar{

    public PlusTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPlusTextView();
    }

    private void initPlusTextView(){
        ImageView plusTextView = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                SizeUtil.dp2px(getContext(),16),
                SizeUtil.dp2px(getContext(),16));
        params.addRule(CENTER_VERTICAL);
        params.setMargins(SizeUtil.dp2px(getContext(), 10),
                0,
                SizeUtil.dp2px(getContext(), 10),
                0);
        plusTextView.setLayoutParams(params);
        plusTextView.setScaleType(ImageView.ScaleType.FIT_XY);
        plusTextView.setImageResource(R.drawable.icon_plus_bold);
        rightView = plusTextView;
        setRightView(rightView);
    }

}
