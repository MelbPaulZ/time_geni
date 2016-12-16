package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.mvpview.CommonMvpView;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Paul on 18/10/16.
 */
public class CommonViewModel extends BaseObservable {
    private final String TAG = "CommonViewModel";
    private CommonPresenter presenter;
    private CommonMvpView mvpView;

    public CommonViewModel(){

    }
    public CommonViewModel(CommonPresenter presenter) {
        this. presenter = presenter;
        mvpView = (CommonMvpView) presenter.getView();
    }

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

    @BindingAdapter({"bind:timeslotVisible"})
    public static void setTimeslotVisible(RelativeLayout view, Event event){
        if (EventUtil.hasOtherInviteeExceptSelf(event)){
            view.setVisibility(view.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }


}
