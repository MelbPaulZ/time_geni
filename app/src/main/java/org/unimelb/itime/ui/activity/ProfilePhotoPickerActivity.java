package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageCropActivity;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.databinding.ActivityProfilePhotopickerBinding;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.widget.TimeGeniiPopupWindow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.fesky.library.widget.ios.ActionSheetDialog;

import static android.R.attr.data;

public class ProfilePhotoPickerActivity extends AppCompatActivity {
    public static final int CHOOSE_FROM_LIBRARY = 0;
    private ImagePicker imagePicker;
    private ActivityProfilePhotopickerBinding binding;
    private ActionSheetDialog popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_photopicker);

        final RelativeLayout mainLayout = binding.gallery;
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width  = mainLayout.getMeasuredWidth();
                int height = mainLayout.getMeasuredHeight();

                binding.profileImage.getLayoutParams().height = width;
                initPopupWindow();
                RelativeLayout btn = binding.choose;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.show();
                    }
                });

                imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
                imagePicker.setMultiMode(false);
                imagePicker.setShowCamera(false);
                imagePicker.setCrop(true);        //允许裁剪（单选才有效）
                imagePicker.setSaveRectangle(true); //是否按矩形区域保存
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                imagePicker.setFocusWidth(width);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                imagePicker.setFocusHeight(width);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
                imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
            }
        });

        initListener();

        // display photo on screen
        String url = SettingManager.getInstance(getApplicationContext()).getSetting().getUser().getPhoto();
        if (!url.equals("")) {
            setPhoto(url);
        }
    }

    private void initPopupWindow(){
        popupWindow =
        new ActionSheetDialog(ProfilePhotoPickerActivity.this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Take Photo", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                ImagePicker.getInstance().takePicture(ProfilePhotoPickerActivity.this,ImagePicker.REQUEST_CODE_TAKE);
//                                                Intent intent = new Intent(ProfilePhotoPickerActivity.this, ImageGridActivity.class);
//                                                startActivityForResult(intent, TAKE_PHOTO);
                            }
                        })
                .addSheetItem("Choose from Photos", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int i) {
                                Intent intent = new Intent(ProfilePhotoPickerActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent, CHOOSE_FROM_LIBRARY);
                            }
                        });
    }

    private void initListener(){
        TextView myProfileBtn = binding.avatarMyProfile;
        myProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSettingActivity();
            }
        });
    }

    private void toSettingActivity(){
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void setPhoto(String url){
        Picasso.with(ProfilePhotoPickerActivity.this).load(url).into(binding.profileImage);
    }

    private void updatePhoto(String localPath) throws IOException {
        String userUid = UserUtil.getInstance(getApplicationContext()).getUser().getUserUid();
        final AVFile file = AVFile.withAbsoluteLocalPath("photo_"+userUid+".png", localPath);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e==null) {
                    SettingManager.getInstance(getApplicationContext()).getSetting().getUser().setPhoto(file.getUrl());
                    // TODO: 28/12/2016 sync user to server


                }else{
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                }

                AppUtil.hideProgressBar();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if return results
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && (requestCode == CHOOSE_FROM_LIBRARY || requestCode == ImagePicker.REQUEST_CODE_CROP)) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (int i = 0; i < images.size(); i++) {
                    Picasso.with(ProfilePhotoPickerActivity.this).load(new File(images.get(i).path)).into(binding.profileImage);
                    // todo update photo to server


                    try {
                        AppUtil.showProgressBar(this, "Waiting", "uploading photo");
                        updatePhoto(images.get(i).path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }else{
            //need to continue operating
            if (data != null) {
                if (resultCode == ImagePicker.RESULT_CODE_BACK) {
//                isOrigin = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                } else {
                    //从拍照界面返回
                    //点击 X , 没有选择照片
                    if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
                        //什么都不做
                    } else {
                        //说明是从裁剪页面过来的数据，直接返回就可以
                        setResult(ImagePicker.RESULT_CODE_ITEMS, data);
                        finish();
                    }
                }
            } else {
                //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
                if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                    //发送广播通知图片增加了
                    ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                    ImageItem imageItem = new ImageItem();
                    imageItem.path = imagePicker.getTakeImageFile().getAbsolutePath();
                    imagePicker.clearSelectedImages();
                    imagePicker.addSelectedImageItem(0, imageItem, true);
                    if (imagePicker.isCrop()) {
                        Intent intent = new Intent(ProfilePhotoPickerActivity.this, ImageCropActivity.class);
                        startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);  //单选需要裁剪，进入裁剪界面
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                        setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
                        finish();
                    }
                }
            }
        }
    }
}
