package unimelb.org.itime.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * provide some common methods and initialise parameters
 */
public abstract class BaseUiFragment extends Fragment {

    protected View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        return mRootView;
    }

    /**
     * use to replace onCreateView
     * @return
     */
    protected abstract int getLayoutId();

}
