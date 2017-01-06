package org.unimelb.itime.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by 37925 on 2016/12/11.
 */

public class TimeGeniiDialog extends PopupWindow {
    private Context context;
    private LinearLayout dialogLayout;
    private RelativeLayout mainView;
    private TextView rightButton;
    private TextView leftButton;
    private TextView title;
    private TextView explain;
    private int textColor = R.color.textBlue;
    private int titleFontSize = 17;
    private int msgFontSize = 10;
    private int selectFontSize = 17;
    private View contentView;


    public TimeGeniiDialog(Context context, View contentView){
        this.context = context;
        this.contentView = contentView;
        titleFontSize = SizeUtil.px2dp(context,context.getResources().getDimension(R.dimen.font_big));
        msgFontSize = SizeUtil.px2dp(context,context.getResources().getDimension(R.dimen.font_tiny));
        selectFontSize = SizeUtil.px2dp(context,context.getResources().getDimension(R.dimen.font_big));
        initDialog();
        this.setContentView(mainView);
        this.setBackgroundDrawable(new ColorDrawable(0x50000000));
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        dialogLayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = dialogLayout.getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void show(){
        this.showAtLocation(contentView, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initDialog(){
        mainView = new RelativeLayout(context);

        dialogLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams dialogParams = new RelativeLayout.LayoutParams(
                SizeUtil.dp2px(context,300), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        dialogLayout.setLayoutParams(dialogParams);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(SizeUtil.dp2px(context,10));
        drawable.setColor(Color.WHITE);
        dialogLayout.setBackground(drawable);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        mainView.addView(dialogLayout);

        title = new TextView(context);
        title.setTextSize(titleFontSize);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0, SizeUtil.dp2px(context, 20), 0, SizeUtil.dp2px(context, 5));
        titleParams.gravity= Gravity.CENTER_HORIZONTAL;
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(titleParams);
        title.getPaint().setFakeBoldText(true);
        dialogLayout.addView(title);

        explain = new TextView(context);
        explain.setTextSize(msgFontSize);
        explain.setTextColor(context.getResources().getColor(R.color.grey_five));
        LinearLayout.LayoutParams explainParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        explainParams.setMargins(SizeUtil.dp2px(context, 20),0, SizeUtil.dp2px(context, 20), SizeUtil.dp2px(context, 15));
        explainParams.gravity= Gravity.CENTER_HORIZONTAL;
        explain.setLayoutParams(explainParams);
        dialogLayout.addView(explain);

        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, SizeUtil.dp2px(context, 0.5));
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.BLACK);
        dialogLayout.addView(divider);

        LinearLayout buttonLayout = new LinearLayout(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayout.setLayoutParams(buttonParams);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogLayout.addView(buttonLayout);

        leftButton = new TextView(context);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                SizeUtil.dp2px(context, 148), SizeUtil.dp2px(context, 50));
        leftButton.setLayoutParams(cancelParams);
        leftButton.setText("Cancel");
        leftButton.setGravity(Gravity.CENTER);
        leftButton.setTextSize(selectFontSize);
        buttonLayout.addView(leftButton);
        leftButton.setTextColor(context.getResources().getColor(textColor));
        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });


        View verticalDivider = new View(context);
        LinearLayout.LayoutParams verticalDividerParams = new LinearLayout.LayoutParams(
                SizeUtil.dp2px(context, 0.5), ViewGroup.LayoutParams.MATCH_PARENT);
        verticalDivider.setLayoutParams(verticalDividerParams);
        verticalDivider.setBackgroundColor(Color.BLACK);
        buttonLayout.addView(verticalDivider);

        rightButton = new TextView(context);
        LinearLayout.LayoutParams doParams = new LinearLayout.LayoutParams(
                SizeUtil.dp2px(context, 151), SizeUtil.dp2px(context, 50));
        rightButton.setLayoutParams(doParams);
        rightButton.setText("Cancel");
        rightButton.setGravity(Gravity.CENTER);
        rightButton.setTextSize(selectFontSize);
        rightButton.setTextColor(context.getResources().getColor(textColor));
        rightButton.getPaint().setFakeBoldText(true);
        buttonLayout.addView(rightButton);
    }

    public TimeGeniiDialog setTitle(String str){
        title.setText(str);
        return this;
    }

    public TimeGeniiDialog setExplain(String str){
        explain.setText(str);
        return this;
    }

    public TimeGeniiDialog setRightOnClickListener(final OnButtonClickListener listener){
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
                TimeGeniiDialog.this.dismiss();
            }
        });
        return this;
    }

    public TimeGeniiDialog setLeftOnClickListener(final OnButtonClickListener listener){
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
                TimeGeniiDialog.this.dismiss();
            }
        });
        return this;
    }

    public TimeGeniiDialog setRightText(String str){
        rightButton.setText(str);
        return this;
    }

    public TimeGeniiDialog setLeftText(String str){
        rightButton.setText(str);
        return this;
    }

    public static class OnButtonClickListener{
        public void onClick(){

        }
    }

}
