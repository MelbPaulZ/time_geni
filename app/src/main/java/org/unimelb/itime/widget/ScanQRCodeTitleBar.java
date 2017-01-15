package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by Qiushuo Huang on 2016/12/23.
 */

public class ScanQRCodeTitleBar extends BlackTitleBar {

    public ScanQRCodeTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initRightTextView();
    }

    private void initRightTextView(){
        TextView doneTextView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_VERTICAL);
        params.setMargins(SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10),
                SizeUtil.dp2px(getContext(), 10));
        doneTextView.setLayoutParams(params);
        doneTextView.setTextColor(getResources().getColor(R.color.white));
        doneTextView.setTextSize(getFontSize());
        doneTextView.setText(getContext().getString(R.string.album));
        rightView = doneTextView;
        setShowRight(getShowRight());
        setRightView(rightView);
    }
}
