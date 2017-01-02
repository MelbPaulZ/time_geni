package org.unimelb.itime.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Qiushuo Huang on 2016/12/30.
 */

public class DefaultPhotoUtil{
    private static DefaultPhotoUtil instance = null;

    private DefaultPhotoUtil (){

    }

    public static DefaultPhotoUtil getInstance(){
        if(instance==null){
            instance = new DefaultPhotoUtil();
        }
        return instance;
    }

    public Bitmap getPhoto(Context context, String name){
        String text = getText(name);
        if(text == null){
            return null;
        }
        View view = generatePhoto(context, text);

        return convertViewToBitmap(view);
    }

    public View getView(Context context, String name){
        String text = getText(name.trim());
        if(text == null){
            return null;
        }
        View view = generatePhoto(context, text);
        return view;
    }

    private View generatePhoto(Context context, String text){
        RelativeLayout layout = new RelativeLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtil.dp2px(context,400), SizeUtil.dp2px(context,400));
        layout.setLayoutParams(params);
        layout.setBackgroundColor(getColor(context));
        layout.setBackgroundColor(getColor(context));

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        TextView textView = new TextView(context);
        textView.setLayoutParams(textParams);
        textView.setTextColor(Color.WHITE);
        textView.setText(text);
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if(text.length()==1) {
            textView.setTextSize(300);
        }else{
            textView.setTextSize(170);
        }
        layout.addView(textView);
        return layout;
    }

    private int getColor(Context context){
        return Color.BLACK;
    }

    private Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
    private String getText(String name){
        String text = "";
        String[] tokens = name.split(" ");
        int length = tokens.length;
        if(length==0){
            return null;
        }

        if(length == 1){
            String str = tokens[0];
            if(str.length()==1){
                text = str.toUpperCase();
            }else if(str.length() > 1){
                text = String.valueOf(Character.toUpperCase(str.charAt(0))) + String.valueOf(Character.toLowerCase(str.charAt(1)));
            }
        }else{
            String first = tokens[0];
            String second = tokens[length-1];
            text = String.valueOf(first.toUpperCase().charAt(0)) + String.valueOf(second.toUpperCase().charAt(0));
        }

        return text;
    }
}
