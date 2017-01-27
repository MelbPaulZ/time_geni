package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.android.databinding.library.baseAdapters.BR;
/**
 * Created by Qiushuo Huang on 2017/1/27.
 */

public class OnboardingItemViewModel extends BaseObservable{
    private int graphyImg;
    private int wordsImg;

    @Bindable
    public int getGraphyImg() {
        return graphyImg;
    }

    public void setGraphyImg(int graphyImg) {
        this.graphyImg = graphyImg;
        notifyPropertyChanged(BR.graphyImg);
    }

    @Bindable
    public int getWordsImg() {
        return wordsImg;
    }

    public void setWordsImg(int wordsImg) {
        this.wordsImg = wordsImg;
        notifyPropertyChanged(BR.wordsImg);
    }
}
