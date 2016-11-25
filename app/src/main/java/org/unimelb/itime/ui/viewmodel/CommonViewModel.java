package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;

/**
 * Created by Paul on 18/10/16.
 */
public class CommonViewModel extends BaseObservable {


    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, Event event){
        LinearLayout parent = (LinearLayout) imageView.getParent();
        int position = parent.indexOfChild(imageView); // get the position
        if (event==null){

        }else{
            // event has url
            if (event.hasPhoto() && event.getPhoto().size()>= position+1){
                imageView.setVisibility(View.VISIBLE);
                if (event.getPhoto().get(position).getUrl()!=null
                        && event.getPhoto().get(position).getUrl().length()>0) {
//                File f = new File(event.getPhoto().get(position).getLocalPath());
                    int size = DensityUtil.dip2px(imageView.getContext(), 40);
                    Picasso.with(imageView.getContext())
                            .load(event.getPhoto().get(position).getUrl())
                            .resize(size, size)
                            .centerCrop()
                            .into(imageView);
                }else{
                    // event haven't get url yet
                    File f = new File(event.getPhoto().get(position).getLocalPath());
                    int size = DensityUtil.dip2px(imageView.getContext(), 40);
                    Picasso.with(imageView.getContext())
                            .load(f)
                            .resize(size, size)
                            .centerCrop()
                            .into(imageView);
                }
            }else{
                imageView.setVisibility(View.GONE);
            }
        }
    }
}