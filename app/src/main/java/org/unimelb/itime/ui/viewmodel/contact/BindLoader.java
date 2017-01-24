package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.widget.BaseTitleBar;
import org.unimelb.itime.widget.SearchBar;
import org.unimelb.itime.widget.WideBadgeArrowButton;

import java.io.File;

/**
 * Created by 37925 on 2016/12/15.
 */

public class BindLoader extends BaseObservable {

    @BindingAdapter("bind:searchListener")
    public static void initSearchBar(SearchBar searchBar, SearchBar.OnEditListener listener){
        searchBar.setSearchListener(listener);
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

    @BindingAdapter("bind:onItemClickListener")
    public static void setOnItemClickListener(ListView view, AdapterView.OnItemClickListener listener){
        view.setOnItemClickListener(listener);
    }

    @BindingAdapter("bind:onPageChangeListener")
    public static void setOnItemClickListener(ViewPager view, ViewPager.OnPageChangeListener listener){
        view.setOnPageChangeListener(listener);
    }

    @BindingAdapter("bind:photo")
    public static void bindPhoto(ImageView view, PhotoUrl url){
        if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                Picasso.with(view.getContext()).load(new File(url.getLocalPath())).error(R.drawable.ic_photo_loading).fit().into(view);
            }
        }else if (!url.getUrl().equals("")){
            Picasso.with(view.getContext()).load(url.getUrl()).error(R.drawable.ic_photo_loading).fit().into(view);
        }
    }
}
