package org.unimelb.itime.ui.viewmodel;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.ui.mvpview.EventCommonMvpView;
import org.unimelb.itime.util.CircleTransform;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.helper.DensityUtil;

import java.util.List;

/**
 * Created by Paul on 18/10/16.
 */
public abstract class CommonViewModel extends AndroidViewModel {
    private EventCommonMvpView mvpView;

    public CommonViewModel(){

    }

    @BindingAdapter({"bind:loadRemoteImg"})
    public static void bindRemoteImg(ImageView imageView, Event event){
////        LinearLayout parent = (LinearLayout) imageView.getParent();
////        int position = parent.indexOfChild(imageView); // get the position
////        if (event==null){
////            return;
////        }
////        // event has url
////        if (event.hasPhoto() && event.getPhoto().size()>= position+1){
////
////            imageView.setVisibility(View.VISIBLE);
////            int size = DensityUtil.dip2px(imageView.getContext(), 40);
////            Picasso.with(imageView.getContext())
////                    .load(event.getPhoto().get(position).getUrl())
////                    .placeholder(R.drawable.invitee_selected_default_picture)
////                    .resize(size, size)
////                    .centerCrop()
////                    .into(imageView);
////        }else{
////            imageView.setVisibility(View.GONE);
////        }
    }
//
//    @BindingAdapter({"android:bindLocalSrc"})
//    public static void bindLocalImg(ImageView imageView, Event event){
//        LinearLayout parent = (LinearLayout) imageView.getParent();
//        int position = parent.indexOfChild(imageView); // get the position
//        if (event==null){
//
//        }else{
//            // event has url
//            if (event.hasPhoto() && event.getPhoto().size()>= position+1){
//                imageView.setVisibility(View.VISIBLE);
//                File f = new File(event.getPhoto().get(position).getLocalPath());
//                if (f.exists()){
//                    int size = DensityUtil.dip2px(imageView.getContext(), 40);
//                    Picasso.with(imageView.getContext())
//                            .load(f)
//                            .placeholder(R.drawable.invitee_selected_default_picture)
//                            .resize(size, size)
//                            .centerCrop()
//                            .into(imageView);
//
//                    parent.requestLayout();
//                }else {
//                    Log.i("fuck", "bindLocalImg: fuck");
//                }
//
//            }else{
//                try {
////                    bindRemoteImg(imageView,event);
//                }catch (Exception e){
////                    imageView.setVisibility(View.GONE);
//                }
//            }
//        }
//    }

    @BindingAdapter({"bind:urls", "bind:index"})
    public static void setImageResource(ImageView imageView, List<Invitee> orgUrls, int index){
        List<Invitee> urls = EventUtil.removeSelfInInvitees(imageView.getContext(), orgUrls);
        if (urls == null){
            return;
        }

        int size = urls.size();
        if (index >= size){
            imageView.setVisibility(View.GONE);
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        imageView.getLayoutParams().width = DensityUtil.dip2px(imageView.getContext(),50);

        if (index < 2){
            Invitee invitee = urls.get(index);
            EventUtil.bindUrlHelper(imageView.getContext(),invitee.getPhoto(),imageView,new CircleTransform());
        }else{
            if (size > 3){
                imageView.getLayoutParams().width = DensityUtil.dip2px(imageView.getContext(),20);
                EventUtil.bindUrlHelper(imageView.getContext(), R.drawable.ic_three_dot,imageView);
            }else{
                Invitee invitee = urls.get(index);
                EventUtil.bindUrlHelper(imageView.getContext(),invitee.getPhoto(),imageView,new CircleTransform());
            }
        }
    }

    @BindingAdapter({"bind:timeslotVisible"})
    public static void setTimeslotVisible(RelativeLayout view, Event event){
        if (EventUtil.hasOtherInviteeExceptSelf(view.getContext(), event)){
            view.setVisibility(view.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }
}
