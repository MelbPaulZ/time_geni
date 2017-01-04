package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.fragment.contact.ContactDialog;
import org.unimelb.itime.ui.presenter.contact.ProfileFragmentPresenter;
import org.unimelb.itime.widget.ContactPopupWindow;


/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragmentViewModel extends BaseObservable {
    private ContactDialog blockDialog;
    private ContactDialog deleteDialog;
    private ContactPopupWindow popupWindow;
    private Contact friend;
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private boolean showGender = true;
    private boolean showAdd = false;
    private boolean showSent = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showAccept = false;
    private ProfileFragmentPresenter presenter;
    private String title = "Profile";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public boolean getShowTitileBack() {
        return showTitileBack;
    }

    public void setShowTitileBack(boolean showTitileBack) {
        this.showTitileBack = showTitileBack;
        notifyPropertyChanged(BR.showTitileBack);
    }

    @Bindable
    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public void setShowTitleRight(boolean showTitleRight) {
        this.showTitleRight = showTitleRight;
        notifyPropertyChanged(BR.showTitleRight);
    }

    public ProfileFragmentViewModel(ProfileFragmentPresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public View.OnClickListener getInvitenListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.inviteUser(friend);
            }
        };
    }

    @Bindable
    public View.OnClickListener getAddListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addUser(friend, new AddCallBack());
            }
        };
    }

    @Bindable
    public boolean getShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
        notifyPropertyChanged(BR.showEmail);
    }

    @Bindable
    public boolean getShowPhone() {
        return showPhone;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
        notifyPropertyChanged(BR.showPhone);
    }

    @Bindable
    public Contact getFriend() {
        return friend;
    }

    public void setFriend(Contact friend) {
        this.friend = friend;
        notifyPropertyChanged(BR.friend);
        notifyPropertyChanged(BR.location);
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.phone);
        notifyPropertyChanged(BR.isMale);
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.blocked);
        notifyPropertyChanged(BR.showGender);

        if(friend.getRelationship()>0 && friend.getStatus().equals(Contact.ACTIVATED)){
            setShowTitleRight(true);
            setShowAdd(false);
            setShowEmail(true);
            setShowPhone(true);
            setShowSent(false);
            setShowAccept(false);
        }else if(friend.getStatus().equals(FriendRequest.DISPLAY_STATUS_ACCEPT)){
            setShowTitleRight(false);
            setShowSent(false);
            setShowAdd(false);
            setShowEmail(true);
            setShowPhone(true);
            setShowAccept(true);
            if(friend.getUserDetail().getPhone().equals("")){
                setShowPhone(false);
            }
            if(friend.getUserDetail().getEmail().equals("")){
                setShowEmail(false);
            }
        }else{
            setShowAccept(false);
            setShowTitleRight(false);
            setShowSent(false);
            setShowAdd(true);
            setShowEmail(true);
            setShowPhone(true);
            if(friend.getUserDetail().getPhone().equals("")){
                setShowPhone(false);
            }
            if(friend.getUserDetail().getEmail().equals("")){
                setShowEmail(false);
            }
        }
    }

    @Bindable
    public boolean getShowGender(){
        return !"".equals(friend.getUserDetail().getGender());
    }


    @Bindable
    public boolean getIsMale(){
        return friend.getUserDetail().getGender().equals(User.MALE);
    }

    @Bindable
    public String getName(){
        return friend.getName();
    }
    @Bindable
    public String getLocation(){
        return friend.getUserDetail().getLocation();
    }
    @Bindable
    public String getPhone(){
        return "Phone: "+friend.getUserDetail().getPhone();
    }

    @Bindable
    public String getEmail(){
        return friend.getUserDetail().getEmail();
    }

    @Bindable
    public boolean getShowAdd() {
        return showAdd;
    }

    public void setShowAdd(boolean showAdd) {
        this.showAdd = showAdd;
        notifyPropertyChanged(BR.showAdd);
    }

    @Bindable
    public boolean getShowSent() {
        return showSent;
    }

    public void setShowSent(boolean showSent) {
        this.showSent = showSent;
        notifyPropertyChanged(BR.showSent);
    }

    @Bindable
    public boolean getBlocked(){
        return friend.getBlockLevel()!=0;
    }

    public View.OnClickListener getTitleRightListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow==null){
                    initPopupWindow();
                }
                popupWindow.showAtLocation(presenter.getView().getContentView(), Gravity.CENTER, 0, 0);
            }
        };
    }

    public View.OnClickListener getTitleBackListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackPressed();
            }
        };
    }

    private void initPopupWindow(){
        popupWindow = new ContactPopupWindow(presenter.getView().getActivity());
        if(getBlocked()) {
            popupWindow.setFirstButtonText(presenter.getContext().getString(R.string.unblock_contact));
        }else{
            popupWindow.setFirstButtonText(presenter.getContext().getString(R.string.block_contact));
        }
        popupWindow.setFirstButtonLisenter(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getBlocked()) {
                    if (blockDialog == null) {
                        initBlockDialog();
                    }
                    blockDialog.showAtLocation(presenter.getView().getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }else{
                    unblockFriend();
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setSecondButtonText(presenter.getContext().getString(R.string.delete_contact));
        popupWindow.setSecondButtonLisenter( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(deleteDialog==null){
                    initDeleteDialog();
                }
                deleteDialog.showAtLocation(presenter.getView().getContentView(), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }

    private void initDeleteDialog(){
        deleteDialog = new ContactDialog(presenter.getView().getActivity());
        deleteDialog.setTitle("Delete this contact?");
        deleteDialog.setExplain("Invitation from this contact will not be shown in your calendar,"
                +" but will be shown in your message box");
        deleteDialog.setRightText("Delete");
        deleteDialog.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFriend();
                deleteDialog.dismiss();
            }
        });
    }

    private void initBlockDialog(){
        blockDialog = new ContactDialog(presenter.getView().getActivity());
        blockDialog.setTitle("Block this contact?");
        blockDialog.setExplain("Block this contact from sending you invitations");
        blockDialog.setRightText("Block");
        blockDialog.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blockFriend();
                blockDialog.dismiss();
            }
        });
    }

    private void unblockFriend(){
        presenter.unblockUser(friend, new UnblockCallBack());
    }

    private void blockFriend(){
        presenter.blockUser(friend, new BlockCallBack());
    }

    private void deleteFriend(){
        presenter.deleteUser(friend, new DeleteCallBack());
    }

    public class BlockCallBack{
        public void success(){
            friend.setBlockLevel(1);
            notifyPropertyChanged(BR.blocked);
            popupWindow.setFirstButtonText(presenter.getContext().getString(R.string.unblock_contact));
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.block_fail), Toast.LENGTH_SHORT);
        }
    }

    public class DeleteCallBack{
        public void success(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.delete_success), Toast.LENGTH_SHORT);
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.delete_fail), Toast.LENGTH_SHORT);
        }
    }

    public class UnblockCallBack{
        public void success(){
            friend.setBlockLevel(0);
            notifyPropertyChanged(BR.blocked);
            popupWindow.setFirstButtonText(presenter.getContext().getString(R.string.block_contact));
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.unblock_fail), Toast.LENGTH_SHORT);
        }
    }

    public class AddCallBack{
        public void success(){
            setShowAdd(false);
            setShowSent(true);
        }

        public void fail(){
            Toast.makeText(presenter.getContext(), presenter.getContext().getString(R.string.add_fail), Toast.LENGTH_SHORT);
        }
    }

    @Bindable
    public boolean getShowAccept() {
        return showAccept;
    }

    public void setShowAccept(boolean showAccept) {
        this.showAccept = showAccept;
        notifyPropertyChanged(BR.showAccept);
    }
}
