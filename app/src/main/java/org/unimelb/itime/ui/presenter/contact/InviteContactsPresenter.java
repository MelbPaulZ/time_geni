package org.unimelb.itime.ui.presenter.contact;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.BaseContact;
import org.unimelb.itime.util.MobileContactUtil;
import org.unimelb.itime.ui.mvpview.contact.InviteContactsMvpView;
import org.unimelb.itime.widget.SideBarListView;

import java.util.List;

/**
 * Created by 37925 on 2016/12/16.
 */

public class InviteContactsPresenter extends MvpBasePresenter<InviteContactsMvpView> {
    private List<BaseContact> googleContacts;
    private List<BaseContact> facebookContacts;
    private List<BaseContact> mobileContacts;

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
           // googleContacts = dao.getFriends();
        }
        return googleContacts;
    }

    public List<BaseContact> getMobileContacts(){
        if(mobileContacts==null){
            mobileContacts = MobileContactUtil.getAllPhoneContacts(getView().getActivity());
        }
        return mobileContacts;
    }

    public List<BaseContact> getFacebookContacts(){
        if(facebookContacts==null){
           // facebookContacts = dao.getFriends();
        }
        return facebookContacts;
    }

    public SideBarListView getSideBarListView() {
        return getView().getSideBarListView();
    }
}
