package org.unimelb.itime.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;

import java.util.List;


/**
 * Created by Paul on 1/1/17.
 */

public class PhotoAdapter extends ArrayAdapter<String> {
    private List<String> urls;
    private int res;
    private Context context;


    public PhotoAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.urls = objects;
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
        String url = urls.get(position);

        convertView.setBackgroundColor(context.getResources().getColor(R.color.red));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.gridview_image);
        imageView.setOnClickListener(onImageClick());
        Picasso.with(context).load(url).error(R.drawable.ic_photo_loading).resize(120,120).centerCrop().into(imageView);
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
