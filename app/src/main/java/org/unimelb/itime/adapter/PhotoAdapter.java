package org.unimelb.itime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;


/**
 * Created by Paul on 1/1/17.
 */

public class PhotoAdapter extends ArrayAdapter<PhotoUrl> {
    private List<PhotoUrl> urls;
    private int res;
    private Context context;


    public PhotoAdapter(Context context, int resource, List<PhotoUrl> urls) {
        super(context, resource, urls);
        this.urls = urls;
        this.res = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.photo_gridview_single, null);
        }
        PhotoUrl url = urls.get(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.gridview_image);
        imageView.setOnClickListener(onImageClick());

       boolean localValid = false;

        if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                localValid = true;
            }
        }

        if (localValid){
            Picasso.with(context).load(new File(url.getLocalPath())).placeholder(R.drawable.invitee_selected_default_picture).error(R.drawable.ic_photo_loading).resize(120,120).centerCrop().into(imageView);
        }else{
            if (!url.getUrl().equals("")){
                Picasso.with(context).load(url.getUrl()).placeholder(R.drawable.invitee_selected_default_picture).error(R.drawable.ic_photo_loading).resize(120,120).centerCrop().into(imageView);
            }
        }
        return convertView;
    }

    public View.OnClickListener onImageClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }
}
