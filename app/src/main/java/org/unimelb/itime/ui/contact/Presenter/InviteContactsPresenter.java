package org.unimelb.itime.ui.contact.Presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.Model.ContactHelper;
import org.unimelb.itime.ui.contact.MvpView.InviteContactsMvpView;
import org.unimelb.itime.ui.contact.Widget.SideBarListView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/16.
 */

public class InviteContactsPresenter extends MvpBasePresenter<InviteContactsMvpView> {
    private List<BaseContact> googleContacts;
    private List<BaseContact> facebookContacts;
    private List<BaseContact> mobileContacts;
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
            googleContacts = dao.getFriends();
        }
        return googleContacts;
    }

    public List<BaseContact> getMobileContacts(){
        if(mobileContacts==null){
            mobileContacts = ContactHelper.getAllPhoneContacts(getView().getActivity());
        }
        return mobileContacts;
    }

    public List<BaseContact> getFacebookContacts(){
        if(facebookContacts==null){
            facebookContacts = dao.getFriends();
        }
        return facebookContacts;
    }

    public SideBarListView getSideBarListView() {
        return getView().getSideBarListView();
    }
}
