package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Size;
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

public class BaseTitleBar  extends RelativeLayout {
    private LinearLayout backLayout;
    private int fontSize = SizeUtil.px2dp(getContext(), getContext().getResources().getDimension(R.dimen.font_big));
    private String title;
    protected TextView titleTextView;
    protected View.OnClickListener backOnClickListener;
    protected View rightView;
    protected View.OnClickListener rightOnClickListener;
    protected ImageView backIcon;
    protected TextView backText;
    private RelativeLayout contentView;
    protected View leftView;
    private RelativeLayout leftButton;
    private RelativeLayout rightButton;
    private boolean showLeft = true;
    private boolean showRight = true;

    protected int getFontSize(){
        return fontSize;
    }

    public BaseTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.InviteeGroupView);
        title = array.getString(R.styleable.BaseTitleBar_title);
        showRight = array.getBoolean(R.styleable.BaseTitleBar_showRight, true);
        showLeft = array.getBoolean(R.styleable.BaseTitleBar_showLeft, true);
        this.setBackground(getResources().getDrawable(R.color.white));

        initContentView();
        initBackLayout();
        initTileTextView();
    }

    public void initContentView(){
        contentView = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                SizeUtil.dp2px(getContext(), 44));
        contentView.setLayoutParams(params);

        rightButton = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                SizeUtil.dp2px(getContext(), 44));
        rightParams.addRule(ALIGN_PARENT_RIGHT);
        rightButton.setLayoutParams(rightParams);

        leftButton = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                SizeUtil.dp2px(getContext(), 44));
        leftParams.addRule(ALIGN_PARENT_LEFT);
        leftButton.setLayoutParams(leftParams);

        this.addView(contentView);
        contentView.addView(rightButton);
        contentView.addView(leftButton);
    }

    public void initBackLayout(){
        backLayout = new LinearLayout(getContext());
        RelativeLayout.LayoutParams backLayoutParams =  new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        backLayoutParams.addRule(CENTER_VERTICAL);
        backLayout.setLayoutParams(backLayoutParams);
        backLayout.setOrientation(LinearLayout.HORIZONTAL);

        backIcon = new ImageView(getContext());
        LinearLayout.LayoutParams backIconParams = new LinearLayout.LayoutParams(
                SizeUtil.dp2px(getContext(),12.5),
                SizeUtil.dp2px(getContext(),21));
        backIconParams.setMargins(SizeUtil.dp2px(getContext(),8),
                0,
                SizeUtil.dp2px(getContext(),6),
                0);
        backIconParams.gravity = Gravity.CENTER_VERTICAL;
        backIcon.setLayoutParams(backIconParams);
        backIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        backIcon.setImageResource(R.drawable.icon_general_arrow_back);


        backText = new TextView(getContext());
        LinearLayout.LayoutParams backTextParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        backTextParams.gravity = Gravity.CENTER_VERTICAL;
        backText.setLayoutParams(backTextParams);
        backText.setTextColor(getResources().getColor(R.color.grey_one));
        backText.setTextSize(fontSize);
        backText.setText("Back");
        backLayout.addView(backIcon);
        backLayout.addView(backText);

        setShowLeft(showLeft);
        leftButton.addView(backLayout);
    }

    private void initTileTextView(){
        titleTextView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_IN_PARENT);
        titleTextView.setLayoutParams(params);
        titleTextView.setText(title);
        titleTextView.setTextSize(fontSize);
        titleTextView.getPaint().setFakeBoldText(true);
        titleTextView.setTextColor(getResources().getColor(R.color.grey_one));
        contentView.addView(titleTextView);
    }

    public void setTitle(String title){
        titleTextView.setText(title);
    }

    public void setBackOnClickListener(View.OnClickListener listener){
        leftButton.setOnClickListener(listener);
    }

    public void setRightOnClickListener(View.OnClickListener listener){
        rightButton.setOnClickListener(listener);
    }

    public boolean getShowLeft() {
        return showLeft;
    }

    public void setShowLeft(boolean showLeft) {
        this.showLeft = showLeft;
        if(leftButton!=null){
            if(showLeft){
                leftButton.setVisibility(VISIBLE);
            }else{
                leftButton.setVisibility(GONE);
            }
        }
    }

    public boolean getShowRight() {
        return showRight;
    }

    public void setShowRight(boolean showRight) {
        this.showRight = showRight;
        if(rightButton!=null){
            if(showRight){
                rightButton.setVisibility(VISIBLE);
            }else{
                rightButton.setVisibility(GONE);
            }
        }
    }

    public View getRightView(){
        return  rightView;
    }

    public View getLeftView(){
        return leftView;
    }

    public void setRightView(View rightView){
        rightButton.addView(rightView);
    }

    public void setLeftView(View leftView){
        leftButton.addView(leftView);
    }
}
