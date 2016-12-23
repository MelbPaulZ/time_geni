package org.unimelb.itime.ui.contact.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Model.SizeHelper;

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
                SizeHelper.dp2px(getContext(),25),
                SizeHelper.dp2px(getContext(),25));
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_IN_PARENT);
        params.addRule(ALIGN_PARENT_RIGHT);
        params.setMargins(SizeHelper.dp2px(getContext(), 10),
                0,
                SizeHelper.dp2px(getContext(), 20),
                0);
        view.setLayoutParams(params);
        view.setImageResource(R.drawable.hamburger);
        rightView = view;
        setShowRight(getShowRight());
        this.addView(rightView);
    }
}
