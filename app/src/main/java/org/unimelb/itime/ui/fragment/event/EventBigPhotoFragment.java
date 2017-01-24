package org.unimelb.itime.ui.fragment.event;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.databinding.FragmentEventBigphotoBinding;
import org.unimelb.itime.ui.mvpview.event.EventBigPhotoMvpView;
import org.unimelb.itime.ui.presenter.event.EventBigPhotoPresenter;
import org.unimelb.itime.ui.presenter.event.EventPhotoPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.event.EventBigPhotoViewModel;
import org.unimelb.itime.ui.viewmodel.event.PhotoGridViewModel;

import java.util.List;

import me.fesky.library.widget.ios.AlertDialog;

/**
 * Created by Qiushuo Huang on 2017/1/24.
 */

public class EventBigPhotoFragment extends BaseUiAuthFragment<EventBigPhotoMvpView, EventBigPhotoPresenter> implements EventBigPhotoMvpView {
    private ToolbarViewModel toolbarViewModel;
    private FragmentEventBigphotoBinding binding;
    private EventBigPhotoViewModel viewModel;
    private List<PhotoUrl> photos;
    private int position;
    private boolean editable;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_bigphoto, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new EventBigPhotoViewModel(getPresenter());
        initToolbar();
        binding.setViewModel(viewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public void onStart(){
        super.onStart();
        loadData();
    }

    private void loadData(){
        viewModel.setPhotos(photos);
        viewModel.setPosition(position);
        viewModel.setSize(photos.size());
        viewModel.setPageChangeListener(getOnPageChangeListener());
        toolbarViewModel.setTitleStr(getTitleString());
    }

    public ViewPager.OnPageChangeListener getOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPosition(position);
                toolbarViewModel.setTitleStr(getTitleString());
                viewModel.setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    private String getTitleString(){
        return (position+1)+"/"+photos.size();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadData();
    }

    private void initToolbar() {
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftDrawable(getContext().getResources().getDrawable(R.drawable.ic_back_arrow));
        toolbarViewModel.setTitleStr(getString(R.string.photo));
        if(isEditable()) {
            toolbarViewModel.setRightClickable(true);
            toolbarViewModel.setRightDrawable(getResources().getDrawable(R.drawable.trash));
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setPhotos(List<PhotoUrl> photos) {
        this.photos = photos;
    }

    @Override
    public EventBigPhotoPresenter createPresenter() {
        return new EventBigPhotoPresenter(getActivity());
    }

    @Override
    public void onBack() {
        getBaseActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        openAlertDialog();
    }

    private void openAlertDialog(){
        new AlertDialog(getActivity())
                .builder()
                .setTitle(getString(R.string.delete_this_photo))
                .setPositiveButton(getString(R.string.delete), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePhoto();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }

    private void deletePhoto(){
        photos.remove(position);
        if(photos.size()==0){
            getBaseActivity().onBackPressed();
        }else {
            if (position == photos.size()) {
                position = position - 1;
            }
            toolbarViewModel.setTitleStr(getTitleString());
            viewModel.setPosition(position);
            viewModel.setPhotos(photos);
        }
    }
}
