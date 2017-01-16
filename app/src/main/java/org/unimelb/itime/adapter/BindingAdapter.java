package org.unimelb.itime.adapter;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;

/**
 * Created by yuhaoliu on 6/01/2017.
 */

public abstract class BindingAdapter {
    @android.databinding.BindingAdapter("android:textStyle")
    public static void setTextStyle(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }

    @android.databinding.BindingAdapter({"android:bindRemoteSrc"})
    public static void bindRemoteImg(ImageView imageView, Event event){
//        LinearLayout parent = (LinearLayout) imageView.getParent();
//        int position = parent.indexOfChild(imageView); // get the position
//        if (event==null){
//            return;
//        }
//        // event has url
//        if (event.hasPhoto() && event.getPhoto().size()>= position+1){
//
//            imageView.setVisibility(View.VISIBLE);
//            int size = DensityUtil.dip2px(imageView.getContext(), 40);
//            Picasso.with(imageView.getContext())
//                    .load(event.getPhoto().get(position).getUrl())
//                    .placeholder(R.drawable.invitee_selected_default_picture)
//                    .resize(size, size)
//                    .centerCrop()
//                    .into(imageView);
//        }else{
//            imageView.setVisibility(View.GONE);
//        }
    }

    @android.databinding.BindingAdapter({"android:bindLocalSrc"})
    public static void bindLocalImg(final ImageView imageView, Event event){
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.invitee_selected_default_picture));
            }
        },5000);
        LinearLayout parent = (LinearLayout) imageView.getParent();
        int position = parent.indexOfChild(imageView); // get the position
        if (event==null){

        }else{
            // event has url
            if (event.hasPhoto() && event.getPhoto().size()>= position+1){
                imageView.setVisibility(View.VISIBLE);
                File f = new File(event.getPhoto().get(position).getLocalPath());
                if (f.exists()){
                    int size = DensityUtil.dip2px(imageView.getContext(), 40);
                    imageView.setImageDrawable(imageView.getResources().getDrawable(R.drawable.icon_event_host_selected));

//                    Picasso.with(imageView.getContext())
//                            .load(f)
//                            .placeholder(R.drawable.invitee_selected_default_picture)
//                            .resize(size, size)
//                            .centerCrop()
//                            .into(imageView);
                }else {
                    Log.i("fuck", "bindLocalImg: fuck");
                }

            }else{
                try {
//                    bindRemoteImg(imageView,event);
                }catch (Exception e){
//                    imageView.setVisibility(View.GONE);
                }
            }
        }
    }
}
