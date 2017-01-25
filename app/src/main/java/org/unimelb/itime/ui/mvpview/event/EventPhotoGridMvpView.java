package org.unimelb.itime.ui.mvpview.event;

import android.databinding.ObservableList;

import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public interface EventPhotoGridMvpView extends TaskBasedMvpView, ItimeCommonMvpView {
    void openCamera();
    void openAlbum();
    void openBigPhoto(int position, ObservableList<PhotoUrl> photos);
}
