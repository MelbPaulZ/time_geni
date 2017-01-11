package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.SizeCollector;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/6.
 */

public class WideArrowButton extends RelativeLayout {

    private int IMG_W = SizeUtil.dp2px(getContext(),37);
    private int IMG_H = SizeUtil.dp2px(getContext(),37);
    private ImageView iconImageView;
    private TextView textView;
    private ImageView arrowImageView;
    private String text;
    private int iconWidth;
    private int iconHeight;
    private int icon;
    private int FONT_SIZE = SizeUtil.px2dp(getContext(), getContext().getResources().getDimension(R.dimen.font_big));
    private int textColor = R.color.grey_one;
    private RelativeLayout contentView;


    public WideArrowButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(getResources().getColor(R.color.white));
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WideArrowButton);
        text = array.getString(R.styleable.WideArrowButton_text);
        icon = array.getResourceId(R.styleable.WideArrowButton_image,0);
        iconWidth = array.getDimensionPixelSize(R.styleable.WideArrowButton_imageWidth, IMG_W);
        iconHeight = array.getDimensionPixelSize(R.styleable.WideArrowButton_imageHeight, IMG_H);

        contentView = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtil.dp2px(getContext(),55.5));
        contentView.setLayoutParams(params);
        this.addView(contentView);
        initArrow();
        initIcon();
        initText();
    }

    public ImageView getArrowView(){
        return arrowImageView;
    }

    public TextView getTextView(){
        return textView;
    }

    public ImageView getIconView(){
        return iconImageView;
    }

    public void setOnClick(OnClickListener listener){
            setOnClickListener(listener);
    }

    public void setImage(int image){
        this.icon = image;
        iconImageView.setImageResource(image);
    }

    public void setText(String str){
        this.text = str;
        textView.setText(text);
    }

    private void initIcon(){
        iconImageView = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(iconWidth,
                iconHeight);
        params.setMargins(SizeUtil.dp2px(getContext(),17),
                SizeUtil.dp2px(getContext(),9),
                SizeUtil.dp2px(getContext(),11),
                SizeUtil.dp2px(getContext(),9));
        params.addRule(ALIGN_PARENT_LEFT);
        params.addRule(CENTER_VERTICAL);
        iconImageView.setLayoutParams(params);
        iconImageView.setId(View.generateViewId());
        if(icon!=0) {
            iconImageView.setImageResource(icon);
        }
        contentView.addView(iconImageView);
    }

    private void initArrow(){
        arrowImageView = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(SizeUtil.dp2px(getContext(),8),
                SizeUtil.dp2px(getContext(),13.5));
        params.setMargins(0, 0, SizeUtil.dp2px(getContext(),12.5), 0);
        params.addRule(ALIGN_PARENT_RIGHT);
        params.addRule(CENTER_VERTICAL);
        arrowImageView.setLayoutParams(params);
        arrowImageView.setImageResource(R.drawable.arrow_icon);
        arrowImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        contentView.addView(arrowImageView);
    }

    private void initText(){
        textView = new TextView(getContext());
        RelativeLayout.LayoutParams params = new LayoutParams(SizeUtil.dp2px(getContext(),250),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RIGHT_OF,iconImageView.getId());
        params.addRule(CENTER_VERTICAL);
        textView.setLayoutParams(params);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine(true);
        textView.setTextSize(FONT_SIZE);
        textView.setTextColor(getResources().getColor(textColor));
        textView.setText(text);
        contentView.addView(textView);
    }
}
