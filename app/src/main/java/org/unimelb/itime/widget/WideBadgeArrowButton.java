package org.unimelb.itime.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/8.
 */

public class WideBadgeArrowButton extends WideArrowButton {
    private TextView badgeView;
    private BadgeView badge;

    public WideBadgeArrowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBadgeView();
    }

    private void initBadgeView(){
        getArrowView().setId(View.generateViewId());
        badgeView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(SizeUtil.dp2px(getContext(),20),
                SizeUtil.dp2px(getContext(),20));
        params.addRule(LEFT_OF,getArrowView().getId());
        params.addRule(CENTER_VERTICAL);
        badgeView.setGravity(Gravity.CENTER);
        badgeView.setLayoutParams(params);
        badgeView.setBackgroundResource(R.drawable.ic_notice_number);
        badgeView.setTextColor(Color.WHITE);
        badgeView.setTextSize(10);
        this.addView(badgeView);
    }

    public void setBadgeCount(int count){
        badgeView.setText(count+"");
        if(count==0){
            badgeView.setVisibility(INVISIBLE);
        }else{
            badgeView.setVisibility(VISIBLE);
        }

    }

}
