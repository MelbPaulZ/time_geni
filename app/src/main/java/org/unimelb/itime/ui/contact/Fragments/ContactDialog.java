package org.unimelb.itime.ui.contact.Fragments;

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
import org.unimelb.itime.ui.contact.Model.SizeHelper;

/**
 * Created by 37925 on 2016/12/11.
 */

public class ContactDialog extends PopupWindow {
    Context context;
    LinearLayout dialogLayout;
    RelativeLayout mainView;
    TextView doButton;
    TextView title;
    TextView explain;

    public ContactDialog(Context context){
        this.context = context;
        initDialog();
        this.setContentView(mainView);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
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

    private void initDialog(){
        mainView = new RelativeLayout(context);

        dialogLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams dialogParams = new RelativeLayout.LayoutParams(
                SizeHelper.dp2px(context,300), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        dialogLayout.setLayoutParams(dialogParams);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(SizeHelper.dp2px(context,10));
        drawable.setColor(Color.WHITE);
        dialogLayout.setBackground(drawable);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        mainView.addView(dialogLayout);

        title = new TextView(context);
        title.setTextSize(20);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0,SizeHelper.dp2px(context, 20), 0, SizeHelper.dp2px(context, 5));
        titleParams.gravity= Gravity.CENTER_HORIZONTAL;
        title.setTextColor(Color.BLACK);
        title.setLayoutParams(titleParams);
        dialogLayout.addView(title);

        explain = new TextView(context);
        explain.setTextSize(12);
        LinearLayout.LayoutParams explainParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        explainParams.setMargins(SizeHelper.dp2px(context, 20),0, SizeHelper.dp2px(context, 20), SizeHelper.dp2px(context, 15));
        explainParams.gravity= Gravity.CENTER_HORIZONTAL;
        explain.setLayoutParams(explainParams);
        dialogLayout.addView(explain);

        View divider = new View(context);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, SizeHelper.dp2px(context, 0.5));
        divider.setLayoutParams(dividerParams);
        divider.setBackgroundColor(Color.BLACK);
        dialogLayout.addView(divider);

        LinearLayout buttonLayout = new LinearLayout(context);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayout.setLayoutParams(buttonParams);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogLayout.addView(buttonLayout);

        TextView cancelButton = new TextView(context);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                SizeHelper.dp2px(context, 150), SizeHelper.dp2px(context, 50));
        cancelButton.setLayoutParams(cancelParams);
        cancelButton.setText("Cancel");
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setTextSize(20);
        buttonLayout.addView(cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });


        View verticalDivider = new View(context);
        LinearLayout.LayoutParams verticalDividerParams = new LinearLayout.LayoutParams(
                SizeHelper.dp2px(context, 0.5), ViewGroup.LayoutParams.MATCH_PARENT);
        verticalDivider.setLayoutParams(verticalDividerParams);
        verticalDivider.setBackgroundColor(Color.BLACK);
        buttonLayout.addView(verticalDivider);

        doButton = new TextView(context);
        LinearLayout.LayoutParams doParams = new LinearLayout.LayoutParams(
                SizeHelper.dp2px(context, 149), SizeHelper.dp2px(context, 50));
        doButton.setLayoutParams(doParams);
        doButton.setText("Cancel");
        doButton.setGravity(Gravity.CENTER);
        doButton.setTextSize(20);
        doButton.setTextColor(context.getResources().getColor(R.color.lightRed));
        buttonLayout.addView(doButton);
    }

    public void setTitle(String str){
        title.setText(str);
    }

    public void setExplain(String str){
        explain.setText(str);
    }

    public void setDoOnClickListener(View.OnClickListener listener){
        doButton.setOnClickListener(listener);
    }

    public void setDoText(String str){
        doButton.setText(str);
    }

}
