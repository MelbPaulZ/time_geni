package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.presenter.event.EventBigPhotoPresenter;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventBigPhotoViewModel extends BaseObservable {
    private List<PhotoUrl> photos;
    private int position;
    private int size;
    private EventBigPhotoPresenter presenter;
    private PhotoUrl photo;

    public EventBigPhotoViewModel(EventBigPhotoPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public List<PhotoUrl> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoUrl> photos) {
        this.photos = photos;
        notifyPropertyChanged(BR.photos);
    }

    @Bindable
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    @Bindable
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    @Bindable
    public PhotoUrl getPhoto() {
        return photos.get(position);
    }

    public void setPhoto(PhotoUrl photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }
}
