package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by Qiushuo Huang on 2016/12/23.
 */


public class BlackDotTitleBar extends BlackTitleBar {

    public BlackDotTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDotView();
    }
    private void initDotView(){
        ImageView view = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                SizeUtil.dp2px(getContext(),16),
                SizeUtil.dp2px(getContext(),16));
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_IN_PARENT);
        params.addRule(ALIGN_PARENT_RIGHT);
        params.setMargins(SizeUtil.dp2px(getContext(), 10),
                0,
                SizeUtil.dp2px(getContext(), 20),
                0);
        view.setLayoutParams(params);
        view.setImageResource(R.drawable.ic_ham_white);
        rightView = view;
        setShowRight(getShowRight());
        this.addView(rightView);
    }
}
