package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;

/**
 * Created by 37925 on 2016/12/13.
 */

public class ContextPresenter {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ContextPresenter.context = context;
    }
}
