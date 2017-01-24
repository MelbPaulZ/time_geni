package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;

import java.io.File;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/23.
 */

public class PhotoGridView extends ViewGroup {

    private static final int DEFAULT_SPACING = 3;
    // Horizontal space between child views.
    private float horizontalSpace ;
    // Vertical space between child views.
    private float verticalSpace ;

    private int imageSize;

    private List<PhotoUrl> photos;
    private int maxnum = Integer.MAX_VALUE;
    private boolean addable = true;
    private View.OnClickListener addMoreOnClickListener;
    private ItemOnClickListener itemOnClickListener;

    public View.OnClickListener getAddMoreOnClickListener() {
        return addMoreOnClickListener;
    }

    public void setAddMoreOnClickListener(View.OnClickListener addMoreOnClickListener) {
        this.addMoreOnClickListener = addMoreOnClickListener;
    }

    public ItemOnClickListener getItemOnClickListener() {
        return itemOnClickListener;
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }


    public PhotoGridView(Context context) {
        super(context);
    }

    public PhotoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDeclaredStyle(context, attrs, 0);
    }

    public PhotoGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDeclaredStyle(context, attrs, defStyleAttr);
    }

    private void initDeclaredStyle(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PhotoGridView, defStyleAttr, 0);
        try {
            horizontalSpace = arr.getDimension(R.styleable.PhotoGridView_spacing, DEFAULT_SPACING);
            verticalSpace = arr.getDimension(R.styleable.PhotoGridView_spacing, DEFAULT_SPACING);
            addable = arr.getBoolean(R.styleable.PhotoGridView_editable, true);
            maxnum = arr.getInteger(R.styleable.PhotoGridView_maxnum, Integer.MAX_VALUE);
        } finally {
            arr.recycle();
        }
    }

    public void setEditable(boolean editable){
        addable = editable;
    }

    public void setMaxnum(int maxnum){
        this.maxnum = maxnum;
    }

    public void setPhotos(List<PhotoUrl> photos){
        this.photos = photos;
        this.removeAllViews();

        for(int i=0;i<photos.size();i++){
            ImageView photo = getPhotoView(photos.get(i));
            if(photo!=null){
                photo.setTag(i);
                this.addView(photo);
            }
        }

        if(addable&&photos.size()< maxnum){
            this.addView(getAddMoreView());
        }
    }

    public ImageView getPhotoView(PhotoUrl url){
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(imageSize, imageSize);
        imageView.setLayoutParams(params);

        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getItemOnClickListener()!=null){
                    getItemOnClickListener().onClick((Integer) view.getTag(), view);
                }
            }
        });

        if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                Picasso.with(getContext()).load(new File(url.getLocalPath())).placeholder(R.drawable.ic_photo_loading)
                        .error(R.drawable.ic_photo_loading).resize(200,200).centerCrop().into(imageView);
                return imageView;
            }
        }

        if (!url.getUrl().equals("")){
                Picasso.with(getContext()).load(url.getUrl()).placeholder(R.drawable.ic_photo_loading)
                        .error(R.drawable.ic_photo_loading).resize(200,200).centerCrop().into(imageView);
            return imageView;
        }

        return null;
    }

    private View getAddMoreView(){
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(imageSize, imageSize);
        imageView.setLayoutParams(params);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getAddMoreOnClickListener()!=null){
                    getAddMoreOnClickListener().onClick(view);
                }
            }
        });
        Picasso.with(getContext()).load(R.drawable.icon_add_more_photos).placeholder(R.drawable.invitee_selected_default_picture)
                .error(R.drawable.ic_photo_loading).resize(200,200).centerCrop().into(imageView);
        return imageView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        imageSize = (int) ((sizeWidth-horizontalSpace*4)/3);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(imageSize, imageSize);
        for(int i=0;i<getChildCount();i++){
            getChildAt(i).setLayoutParams(params);
        }

        int width = 0;
        int height = 0;
        int count = getChildCount();
        if(count!=0){
            int hCount = ((count-1)/3)+1;
            width = (int) (imageSize*3 + horizontalSpace*2);
            height = (int) (imageSize*hCount+verticalSpace*(hCount+1));
        }

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                :  height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final int childw = child.getLayoutParams().width;
            final int childh = child.getLayoutParams().height;
            int ycount = i/3;
            int xcount = i%3;
            int ypos = (int) (childh * ycount + verticalSpace*(ycount+1));
            int xpos = (int) (childw * xcount + horizontalSpace*(xcount+1));

            child.layout(xpos, ypos, xpos + childw, ypos + childh);
        }
    }

    public interface ItemOnClickListener {
        void onClick(int position, View view);
    }
}
