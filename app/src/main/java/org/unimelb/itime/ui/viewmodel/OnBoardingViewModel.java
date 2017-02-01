package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import org.unimelb.itime.R;
import org.unimelb.itime.ui.viewmodel.contact.BindLoader;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class OnBoardingViewModel extends BaseObservable{
    private ObservableList<OnBoardingItemViewModel> pages;
    private List<Drawable> dotImages;
    private Drawable dotImage;
    private View.OnClickListener skipListener;
    private Context context;
    private boolean showSkip = true;
    private int currentPage = 0;

    @Bindable
    public boolean getShowSkip() {
        return showSkip;
    }

    public void setShowSkip(boolean showSkip) {
        this.showSkip = showSkip;
        notifyPropertyChanged(BR.showSkip);
    }

    public OnBoardingViewModel(Context context) {
        this.context = context;
        initDotImages();
    }

    @Bindable
    public View.OnClickListener getSkipListener() {
        return skipListener;
    }

    public void setSkipListener(View.OnClickListener skipListener) {
        this.skipListener = skipListener;
        notifyPropertyChanged(BR.skipListener);
    }

    @Bindable
    public Drawable getDotImage() {
        return dotImage;
    }

    public void setDotImage(Drawable dotImage) {
        this.dotImage = dotImage;
        notifyPropertyChanged(BR.dotImage);
    }

    private void initDotImages(){
        dotImages = new ArrayList<>();
        dotImages.add(context.getResources().getDrawable(R.drawable.onboarding_page1_dots_icon));
        dotImages.add(context.getResources().getDrawable(R.drawable.onboarding_page2_dots_icon));
        dotImages.add(context.getResources().getDrawable(R.drawable.onboarding_page3_dots_icon));
        dotImages.add(context.getResources().getDrawable(R.drawable.onboarding_page4_dots_icon));
        dotImages.add(null);
        setDotImage(dotImages.get(0));
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setDotImage(dotImages.get(position));
                if(position==dotImages.size()-1){
                    setShowSkip(false);
                }else{
                    setShowSkip(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
