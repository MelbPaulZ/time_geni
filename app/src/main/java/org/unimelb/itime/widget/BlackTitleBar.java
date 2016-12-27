package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
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
    private LinearLayout backLayout;
    private int fontSize = SizeCollector.FONT_EXTR;
    private String title;
    private TextView titleTextView;
    private View.OnClickListener backOnClickListener;
    protected View rightView;
    protected View.OnClickListener rightOnClickListener;
    private boolean showLeft = true;
    private boolean showRight = true;

    protected int getFontSize(){
        return fontSize;
    }

    public BlackTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InviteeGroupView);
        title = array.getString(R.styleable.BaseTitleBar_title);
        showRight = array.getBoolean(R.styleable.BaseTitleBar_showRight, true);
        showLeft = array.getBoolean(R.styleable.BaseTitleBar_showLeft, true);
        this.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        this.setBackground(getResources().getDrawable(R.color.Black));
        initBackLayout();
        initTileTextView();
    }

    public void initBackLayout(){
        backLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams backLayoutParams =  new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        backLayoutParams.addRule(CENTER_VERTICAL);
        backLayout.setLayoutParams(backLayoutParams);
        backLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView backIcon = new ImageView(getContext());
        LinearLayout.LayoutParams backIconParams = new LinearLayout.LayoutParams(
                SizeUtil.dp2px(getContext(),25),
                SizeUtil.dp2px(getContext(),25));
        backIconParams.setMargins(SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10),
                SizeUtil.dp2px(getContext(),10));
        backIcon.setLayoutParams(backIconParams);
        backIcon.setImageResource(R.drawable.ic_left_arrow_white);

        TextView backText = new TextView(getContext());
        LinearLayout.LayoutParams backTextParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        backTextParams.gravity = Gravity.CENTER_VERTICAL;
        backText.setLayoutParams(backTextParams);
        backText.setTextColor(getResources().getColor(R.color.white));
        backText.setTextSize(fontSize);
        backText.setText("Back");
        backLayout.addView(backIcon);
        backLayout.addView(backText);

        setShowLeft(showLeft);
        this.addView(backLayout);
    }

    private void initTileTextView(){
        titleTextView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_IN_PARENT);
        titleTextView.setLayoutParams(params);
        titleTextView.setTextColor(getResources().getColor(R.color.white));
        titleTextView.setText(title);
        titleTextView.setTextSize(fontSize);
        this.addView(titleTextView);
    }

    public void setTitle(String title){
        titleTextView.setText(title);
    }

    public void setBackOnClickListener(View.OnClickListener listener){
        backLayout.setOnClickListener(listener);
    }

    public void setRightOnClickListener(View.OnClickListener listener){
        rightView.setOnClickListener(listener);
    }

    public boolean getShowLeft() {
        return showLeft;
    }

    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
        if(backLayout!=null){
            if(showLeft){
                backLayout.setVisibility(VISIBLE);
            }else{
                backLayout.setVisibility(GONE);
            }
        }
    }

    public boolean getShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
        if(rightView!=null){
            if(showRight){
                rightView.setVisibility(VISIBLE);
            }else{
                rightView.setVisibility(GONE);
            }
        }
    }
}
