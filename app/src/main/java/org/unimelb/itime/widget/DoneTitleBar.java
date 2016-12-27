package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;


/**
 * Created by 37925 on 2016/12/6.
 */

public class DoneTitleBar extends BaseTitleBar{

    public DoneTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDoneTextView();
    }

    private void initDoneTextView(){
        TextView doneTextView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_IN_PARENT);
        params.addRule(ALIGN_PARENT_RIGHT);
        params.setMargins(SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10));
        doneTextView.setLayoutParams(params);
        doneTextView.setTextColor(getResources().getColor(R.color.orange));
        doneTextView.setTextSize(getFontSize());
        doneTextView.setText("Done");
        rightView = doneTextView;
        setShowRight(getShowRight());
        this.addView(rightView);
    }
}
