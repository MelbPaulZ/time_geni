package org.unimelb.itime.ui.contact.Presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.MvpView.AddContactsMvpView;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/15.
 */

public class AddContactsPresenter extends MvpBasePresenter<AddContactsMvpView> {
    private List<BaseContact> googleContacts;
    private List<BaseContact> facebookContacts;
    private InviteeDao dao = new InviteeDao();

    public void onBackPressed() {
        if(getView()!=null){
            getView().getActivity().onBackPressed();
        }
    }

    public int getMatchColor(){
        if(getView()!=null){
            return getView().getActivity().getResources().getColor(R.color.matchColor);
        }
        return -1;
    }

    public List<BaseContact> getGoogleContacts(){
        if(googleContacts==null){
            googleContacts = dao.getGoogleContacts();
        }
        return googleContacts;
    }

    public List<BaseContact> getMobileContacts(){
        if(googleContacts==null){
            googleContacts = dao.getGoogleContacts();
        }
        return googleContacts;
    }

    public List<BaseContact> getFacebookContacts(){
        if(facebookContacts==null){
            facebookContacts = dao.getGoogleContacts();
        }
        return facebookContacts;
    }

    public SideBarListView getSideBarListView() {
        return getView().getSideBarListView();
    }
}
