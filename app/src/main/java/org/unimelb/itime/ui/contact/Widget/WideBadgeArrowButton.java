package org.unimelb.itime.ui.contact.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;

import org.unimelb.itime.R;

/**
 * Created by 37925 on 2016/12/8.
 */

public class WideBadgeArrowButton extends WideArrowButton {
    private TextView badgeView;

    public WideBadgeArrowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBadgeView();
    }

    private void initBadgeView(){
        getArrowView().setId(2);
        badgeView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(LEFT_OF,getArrowView().getId());
        params.addRule(CENTER_VERTICAL);
        badgeView.setLayoutParams(params);
        this.addView(badgeView);
    }

    public void setBadgeCount(int count){
        BadgeView badgeView = new BadgeView(getContext());
        badgeView.setBadgeGravity(Gravity.CENTER);
        badgeView.setBackground(getContext().getResources().getDisplayMetrics().densityDpi,getResources().getColor(R.color.badgeRed));
        badgeView.setBadgeCount(count);
        badgeView.setTargetView(this.badgeView);
    }

}
