package org.unimelb.itime.ui.contact.Model;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by 37925 on 2016/12/3.
 */

public class ViewFactory {
    public static ViewFactory viewFactory = null;
    private Context context = null;
    private ViewFactory(Context context){
        this.context = context;
    }

    public ViewFactory getInstance(Context context){
        if(viewFactory == null){
            viewFactory = new ViewFactory(context);
        }
        return viewFactory;
    }
//
//    public View getInviteContactsView(int icon, String text){
//        LinearLayout layout = new LinearLayout(context);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layout.setLayoutParams(params);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        ImageView iconView = new ImageView(context);
//        Picasso.with(context)
//                .load(icon)
//                .into(iconView);
//        LinearLayout.LayoutParams iconViewParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        iconViewParams.setMargins(SizeHelper.dp2px(context, 20),0, 0, 0);
//        iconView.setLayoutParams(iconViewParams);
//
//
//        TextView textView = new TextView(context);
//        textView.setText(text);
//        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        textViewParams.setMargins(SizeHelper.dp2px(context, 15),0, 0, 0);
//        textView.setLayoutParams(textViewParams);
//
//        ImageView arrowView = new ImageView(context);
//        Picasso.with(context)
//                .load(R.mipmap.icon_general_arrow_right)
//                .into(iconView);
//        LinearLayout.LayoutParams arrowViewParams = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        arrowViewParams.setMargins(0, 0, SizeHelper.dp2px(context, 20), 0);
//        arrowViewParams.gravity = Gravity.RIGHT;
//        arrowView.setLayoutParams(arrowViewParams);
//
//        layout.addView(iconView);
//        layout.addView(textView);
//        layout.addView(arrowView);
//        return layout;
//    }

}
