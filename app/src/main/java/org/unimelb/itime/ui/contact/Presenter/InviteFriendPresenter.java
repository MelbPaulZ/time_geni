package org.unimelb.itime.ui.contact.Presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.BaseContact;
import org.unimelb.itime.ui.contact.Dao.InviteeDao;
import org.unimelb.itime.ui.contact.Model.ContactChecker;
import org.unimelb.itime.ui.contact.MvpView.InviteFriendMvpView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/16.
 */

public class InviteFriendPresenter extends MvpBasePresenter<InviteFriendMvpView> {
    private InviteeDao dao = new InviteeDao();
    public List<BaseContact> getFriends() {
        List<BaseContact> result = new ArrayList<BaseContact>( dao.getITimeFriends());
        return result;
    }

    public List<BaseContact> getSearchList(){
        return dao.getFriends();
    }

    public int getMatchColor() {
        return getView().getActivity().getResources().getColor(R.color.matchColor);
    }

    public boolean isEmail(String str){
        return ContactChecker.getInsstance().isEmail(str);
    }

    public boolean isPhone(String str){
        return ContactChecker.getInsstance().isPhone(str);
    }

    public void onBackPress() {
        getView().getActivity().onBackPressed();
    }
}
