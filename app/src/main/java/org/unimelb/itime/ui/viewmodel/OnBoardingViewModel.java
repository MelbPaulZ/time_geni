package org.unimelb.itime.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
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
    private List<Integer> dotImages;
    private int dotImage;
    private View.OnClickListener skipListener;
    private Context context;

    public OnBoardingViewModel(Context context) {
        this.context = context;
        initDotImages();
        initPageViews();
    }

    @Bindable
    public View.OnClickListener getSkipListener() {
        return skipListener;
    }

    public void setSkipListener(View.OnClickListener skipListener) {
        this.skipListener = skipListener;

    }

    @Bindable
    public int getDotImage() {
        return dotImage;
    }

    public void setDotImage(int dotImage) {
        this.dotImage = dotImage;
        notifyPropertyChanged(BR.dotImage);
    }

    private void initDotImages(){
        dotImages = new ArrayList<>();
        dotImages.add(R.drawable.onboarding_page1_dots_icon);
        dotImages.add(R.drawable.onboarding_page2_dots_icon);
        dotImages.add(R.drawable.onboarding_page3_dots_icon);
        dotImages.add(R.drawable.onboarding_page4_dots_icon);
        for(int i=0;i<4;i++) {
            ImageView view = new ImageView(context);
            BindLoader.loadAvartar(view, dotImages.get(i));
        }
        setDotImage(dotImages.get(0));
    }

    private void initPageViews(){
        pages = new ObservableArrayList<>();
        OnBoardingItemViewModel page1 = new OnBoardingItemViewModel();
        OnBoardingItemViewModel page2 = new OnBoardingItemViewModel();
        OnBoardingItemViewModel page3 = new OnBoardingItemViewModel();
        OnBoardingItemViewModel page4 = new OnBoardingItemViewModel();
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        pages.add(page4);

        page1.setGraphyImg(R.drawable.onboarding_page1_graphy);
        page1.setWordsImg(R.drawable.onboarding_page1_words);

        page2.setGraphyImg(R.drawable.onboarding_page2_graphy);
        page2.setWordsImg(R.drawable.onboarding_page2_words);

        page3.setGraphyImg(R.drawable.onboarding_page3_graphy);
        page3.setWordsImg(R.drawable.onboarding_page3_words);

        page4.setGraphyImg(R.drawable.onboarding_page4_graphy);
        page4.setWordsImg(R.drawable.onboarding_page4_words);
    }

    public ObservableList<OnBoardingItemViewModel> getPages() {
        return pages;
    }

    public ItemView getItemView() {
        return ItemView.of(BR.viewModel, R.layout.viewpager_onboarding);
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
