package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;
import org.unimelb.itime.R;
import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class OnBoardingViewModel extends BaseObservable{
    private ObservableList<OnboardingItemViewModel> pages;
    private List<Integer> dotImages;
    private int dotImage;

    @Bindable
    public int getDotImage() {
        return dotImage;
    }

    public void setDotImage(int dotImage) {
        this.dotImage = dotImage;
        notifyPropertyChanged(BR.dotImage);
    }

    public OnBoardingViewModel() {
        initDotImages();
    }

    private void initDotImages(){
        dotImages = new ArrayList<>();
        dotImages.add(R.drawable.onboarding_page1_dots_icon);
        dotImages.add(R.drawable.onboarding_page2_dots_icon);
        dotImages.add(R.drawable.onboarding_page3_dots_icon);
        dotImages.add(R.drawable.onboarding_page4_dots_icon);
    }

    private void initPageViews(){
        pages = new ObservableArrayList<>();
        dotImages.add(R.drawable.onboarding_page1_dots_icon);
        dotImages.add(R.drawable.onboarding_page2_dots_icon);
        dotImages.add(R.drawable.onboarding_page3_dots_icon);
        dotImages.add(R.drawable.onboarding_page4_dots_icon);
    }

    public ObservableList<OnboardingItemViewModel> getPages() {
        return pages;
    }

    public ItemView getItemView() {
        return null;
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDotImage(dotImages.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }
}
