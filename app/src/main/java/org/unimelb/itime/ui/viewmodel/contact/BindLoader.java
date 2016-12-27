package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.widget.BaseTitleBar;
import org.unimelb.itime.widget.SearchBar;
import org.unimelb.itime.widget.WideBadgeArrowButton;

/**
 * Created by 37925 on 2016/12/15.
 */

public class BindLoader extends BaseObservable {

    @BindingAdapter("bind:searchListener")
    public static void initSearchBar(SearchBar searchBar, SearchBar.OnEditListener listener){
        searchBar.setOnEditListener(listener);
    }

    @BindingAdapter("bind:img")
    public static void loadAvartar(ImageView iv, String img) {
        try {
            Picasso.with(iv.getContext()).load(img).into(iv);
        }catch (Exception e){}
    }

    @BindingAdapter("bind:img")
    public static void loadAvartar(ImageView iv, int img) {
        Picasso.with(iv.getContext()).load(img).resize(100,100).centerCrop().into(iv);
    }

    @BindingAdapter("bind:qrcode")
    public static void loadQRCode(ImageView iv, Bitmap img) {
        iv.setImageBitmap(img);
    }

    @BindingAdapter("bind:smallAvatar")
    public static void loadSmallAvartar(ImageView iv, String img) {
        Picasso.with(iv.getContext()).load(img).resize(100,100).centerCrop().into(iv);
    }

    @BindingAdapter("bind:avatar")
    public static void bindAvatar(ImageView view, String img){
        Picasso.with(view.getContext()).load(img).into(view);
    }

    @BindingAdapter("bind:avatar")
    public static void bindAvatar(ImageView view, int img){
        Picasso.with(view.getContext()).load(img).into(view);
    }

    @BindingAdapter("bind:titleBackListener")
    public static void bindTitileBack(BaseTitleBar titleBar, View.OnClickListener backListener){
        titleBar.setBackOnClickListener(backListener);
    }

    @BindingAdapter("bind:titleRightListener")
    public static void bindTitileRight(BaseTitleBar titleBar, View.OnClickListener rightListener){
        titleBar.setRightOnClickListener(rightListener);
    }

    @BindingAdapter("bind:badgeCount")
    public static void setBadgeCount(WideBadgeArrowButton view, int count){
        view.setBadgeCount(count);
    }
}
