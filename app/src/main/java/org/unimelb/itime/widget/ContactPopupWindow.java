package org.unimelb.itime.widget;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.ContactPopupWindowBinding;


public class ContactPopupWindow extends PopupWindow {

    View popupWindow;
    TextView firstButton;
    TextView secondButton;
    TextView cancelButton;
    ContactPopupWindowBinding binding;

    public ContactPopupWindow(Activity context) {
        super(context);
        binding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.contact_popup_window, null, false);
        popupWindow = binding.getRoot();
        firstButton = binding.firstButton;
        secondButton = binding.secondButton;
        initCancelButton();

        this.setContentView(popupWindow);
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果  
        //this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        popupWindow.setOnTouchListener(new OnTouchListener() {

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

    public void setFirstButtonText(String text){
        firstButton.setText(text);
    }

    public void setFirstButtonLisenter(OnClickListener listener){
        firstButton.setOnClickListener(listener);
    }

    public void setSecondButtonText(String text){
        secondButton.setText(text);
    }

    public void setSecondButtonLisenter(OnClickListener listener){
        secondButton.setOnClickListener(listener);
    }

    private void initCancelButton(){
        cancelButton = binding.cancelButton;
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
    }
}  