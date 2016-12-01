package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;

import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;

import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public class InboxViewModel extends BaseObservable {
    private MainInboxPresenter presenter;
    private List<Message> messages;

    public InboxViewModel(MainInboxPresenter presenter) {
        this.presenter = presenter;
    }

    public void setData(List<Message> messages){
        this.messages = messages;
    }
}
