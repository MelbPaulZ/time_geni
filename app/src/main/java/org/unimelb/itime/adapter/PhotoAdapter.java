package org.unimelb.itime.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.databinding.PhotoSingleLineBinding;
import org.unimelb.itime.ui.viewmodel.EventCreateNewVIewModel;

import java.util.List;

/**
 * Created by Paul on 14/10/16.
 */
public class PhotoAdapter extends ArrayAdapter<PhotoUrl> {
    private PhotoSingleLineBinding binding;
    private LayoutInflater inflater;
    private EventCreateNewVIewModel viewmodel;
    private int position;
    private Context context;
    private List<PhotoUrl> photoUrlList;

    public PhotoAdapter(Context context, int resource, List<PhotoUrl> objects, EventCreateNewVIewModel viewModel) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.viewmodel = viewModel;
        this.photoUrlList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.photo_single_line, parent, false);
        }
        this.position = position;
        binding.setPosition(position);
        binding.setVm(viewmodel);
        return binding.getRoot();
    }

    @BindingAdapter("imageResource")
    public void setImageResource(ImageView imageView, String url){
        Picasso.with(context).load(url).into(imageView);
    }
}
