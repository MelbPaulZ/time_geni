package org.unimelb.itime.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ContactPopupWindowBinding;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by Qiushuo Huang on 2017/1/5.
 */
public class TimeGeniiPopupWindow extends PopupWindow{
    private Context context;
    private TextView cancelButton;
    private LinearLayout itemsLayout;
    private ContactPopupWindowBinding binding;
    private int FONT_SIZE = 17;
    private View popupWindow;
    private View contentView;

    public TimeGeniiPopupWindow(Context context, View contentView) {
        this.context = context;
        this.contentView = contentView;
        builder();
    }

    private void builder() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.contact_popup_window, null, false);
        popupWindow = binding.getRoot();
        itemsLayout = binding.itemsLayout;

        cancelButton = binding.cancelButton;
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });

        this.setContentView(popupWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPopup);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popupWindow.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = binding.popupLayout.getTop();
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

    public TimeGeniiPopupWindow addSheetItem(String strItem, int color, TimeGeniiPopupWindow.OnSheetItemClickListener listener) {
        addItemView(new TimeGeniiPopupWindow.SheetItem(strItem, color, listener));
        return this;
    }

    private void addItemView(final SheetItem item){
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                SizeUtil.dp2px(context,50));
        params.setMargins(0,SizeUtil.dp2px(context,5),0,0);
        textView.setLayoutParams(params);
        textView.setTextColor(item.color);
        textView.setTextSize(FONT_SIZE);
        textView.setText(item.name);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(context.getResources().getDrawable(R.drawable.corner_border));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.itemClickListener.onClick();
                TimeGeniiPopupWindow.this.dismiss();
            }
        });
        itemsLayout.addView(textView);

    }

    public void show() {
        this.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    public class SheetItem {
        String name;
        TimeGeniiPopupWindow.OnSheetItemClickListener itemClickListener;
       int color;

        public SheetItem(String name, int color, TimeGeniiPopupWindow.OnSheetItemClickListener itemClickListener) {
            this.name = name;
            this.color = color;
            this.itemClickListener = itemClickListener;
        }
    }

    public interface OnSheetItemClickListener {
        void onClick();
    }
}
