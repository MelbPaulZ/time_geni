package org.unimelb.itime.ui.presenter.contact;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.ui.mvpview.contact.EditContactMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditContactPresenter extends MvpBasePresenter<EditContactMvpView> {
    Context context;

    public EditContactPresenter(Context context) {
        this.context = context;
    }

    public void editAlias(Contact contact){
        if(getView()!=null){
            EventBus.getDefault().post(new MessageEditContact(contact));
            updateEdit(contact);
            getView().getActivity().onBackPressed();
        }
    }

    private void updateEdit(Contact contact){
        DBManager.getInstance(context).updateContact(contact);
    }
}
