package org.unimelb.itime.ui.viewmodel.event;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventPhotoAdapter;
import org.unimelb.itime.adapter.PhotoAdapter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.fragment.event.EventPhotoFragment;
import org.unimelb.itime.ui.presenter.event.EventPhotoPresenter;
import org.unimelb.itime.widget.PhotoGridView;

import java.util.ArrayList;
import java.util.List;

import me.fesky.library.widget.ios.ActionSheetDialog;

/**
 * Created by Qiushuo Huang on 2017/1/23.
 */

public class PhotoGridViewModel extends BaseObservable{
    private List<PhotoUrl> photos;
    private EventPhotoPresenter presenter;
    private boolean editable;
    private int maxNum;

    @Bindable
    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        notifyPropertyChanged(BR.maxNum);
    }

    @Bindable
    public boolean getEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyPropertyChanged(BR.editable);
    }

    public PhotoGridViewModel (EventPhotoPresenter presenter){
        this.presenter = presenter;
    }

    public void setPhotos(List<PhotoUrl> photos) {
        this.photos = photos;
        notifyPropertyChanged(BR.photos);
    }

    @Bindable
    public List<PhotoUrl> getPhotos(){
        return photos;
    }

    public View.OnClickListener getAddMoreOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActionSheetDialog();
            }
        };
    }

    public PhotoGridView.ItemOnClickListener getItemOnClickListener(){
        return new PhotoGridView.ItemOnClickListener() {
            @Override
            public void onClick(int i, View view) {
                if(presenter.getView()!=null){
                    presenter.getView().openBigPhoto(i, photos);
                }
            }
        };
    }

    private void openActionSheetDialog(){
        if (presenter.getView()!=null) {
             new ActionSheetDialog(presenter.getContext())
                    .builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(presenter.getContext().getResources().getString(R.string.take_photo),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    presenter.getView().openCamera();
                                }
                            })
                    .addSheetItem(presenter.getContext().getResources().getString(R.string.choose_from_photos),
                            null,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    presenter.getView().openAlbum();
                                }
                            }).show();
        }
    }
}
