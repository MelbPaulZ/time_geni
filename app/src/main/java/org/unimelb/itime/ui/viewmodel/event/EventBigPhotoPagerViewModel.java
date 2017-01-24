package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.PhotoUrl;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventBigPhotoPagerViewModel extends BaseObservable{
    private PhotoUrl photo;

    @Bindable
    public PhotoUrl getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoUrl photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }
}
