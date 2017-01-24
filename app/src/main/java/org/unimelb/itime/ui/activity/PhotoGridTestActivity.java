package org.unimelb.itime.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiAuthFragment;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.ui.fragment.contact.AddFriendsFragment;
import org.unimelb.itime.ui.fragment.event.EventPhotoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/23.
 */

public class PhotoGridTestActivity extends EmptyActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_contacts);
        fragmentManager = getSupportFragmentManager();
        EventPhotoFragment fragment = new EventPhotoFragment();
        List<PhotoUrl> photos = new ArrayList<>();
        PhotoUrl p1 = new PhotoUrl();
        p1.setUrl("http://static.open-open.com/lib/uploadImg/20150705/20150705190525_630.png");
        photos.add(p1);

        PhotoUrl p2 = new PhotoUrl();
        p2.setUrl("http://static.open-open.com/lib/uploadImg/20150705/20150705190525_630.png");
        photos.add(p2);

        PhotoUrl p3 = new PhotoUrl();
        p3.setUrl("http://static.open-open.com/lib/uploadImg/20150705/20150705190525_630.png");
        photos.add(p3);

        //fragment.setPhotos(photos);

        openFragment(fragment, null, false);
    }

    @Override
    protected int getFragmentContainerId(){
        return R.id.contentFrameLayout;
    }
}
