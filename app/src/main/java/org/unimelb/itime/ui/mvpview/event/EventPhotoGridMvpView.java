package org.unimelb.itime.ui.mvpview.event;

import android.content.Context;

import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public interface EventPhotoGridMvpView extends TaskBasedMvpView, ItimeCommonMvpView {
    void openCamera();
    void openAlbum();
    void openBigPhoto(int position, List<PhotoUrl> photos);
}
