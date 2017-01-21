package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/9.
 */

public class DotTitleBar extends BaseTitleBar {

    public DotTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDotView();
    }
    private void initDotView(){
        ImageView view = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                SizeUtil.dp2px(getContext(),16),
                SizeUtil.dp2px(getContext(),16));
        params.addRule(CENTER_VERTICAL);
        params.setMargins(SizeUtil.dp2px(getContext(), 10),
                0,
                SizeUtil.dp2px(getContext(), 20),
                0);
        view.setLayoutParams(params);
        view.setImageResource(R.drawable.hamburger);
        rightView = view;
        setShowRight(getShowRight());
        setRightView(rightView);
    }
}
