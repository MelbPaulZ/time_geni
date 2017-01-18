package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;

import me.fesky.library.widget.ios.ActionSheetDialog;
import me.fesky.library.widget.ios.AlertDialog;


/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragmentViewModel extends BaseObservable {
    private AlertDialog blockDialog;
    private AlertDialog deleteDialog;
    private ActionSheetDialog popupWindow;
    private Contact friend;
    private FriendRequest request;
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private boolean showAdd = false;
    private boolean showSent = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showAccept = false;
    private boolean showAccepted = false;
    private boolean showEdit = false;
    private boolean showRealName = false;
    private ProfilePresenter presenter;

    @Bindable
    public boolean getShowTitileBack() {
        return showTitileBack;
    }

    public void setShowTitileBack(boolean showTitileBack) {
        this.showTitileBack = showTitileBack;
        notifyPropertyChanged(BR.showTitileBack);
    }

    @Bindable
    public boolean getShowRealName() {
        return showRealName;
    }

    public void setShowRealName(boolean showRealName) {
        this.showRealName = showRealName;
        notifyPropertyChanged(BR.showRealName);
    }

    @Bindable
    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public void setShowTitleRight(boolean showTitleRight) {
        this.showTitleRight = showTitleRight;
        notifyPropertyChanged(BR.showTitleRight);
    }

    public ProfileFragmentViewModel(ProfilePresenter presenter){
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
                presenter.addUser(friend);
            }
        };
    }

    @Bindable
    public View.OnClickListener getAcceptListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptRequest(request);
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
        notifyPropertyChanged(BR.realName);

        if(friend.getRelationship()>0 && friend.getStatus().equals(Contact.ACTIVATED)){
            setShowTitleRight(true);
            setShowAdd(false);
            setShowEmail(true);
            setShowPhone(true);
            setShowSent(false);
            setShowAccept(false);
            setShowAccepted(false);
            setShowEdit(true);
            setShowRealName(true);
        }else if(friend.getStatus().equals(FriendRequest.DISPLAY_STATUS_ACCEPT)){
            setShowTitleRight(false);
            setShowSent(false);
            setShowAdd(false);
            setShowEmail(true);
            setShowPhone(true);
            setShowAccept(true);
            setShowAccepted(false);
            if(friend.getUserDetail().getPhone().equals("")){
                setShowPhone(false);
            }
            if(friend.getUserDetail().getEmail().equals("")){
                setShowEmail(false);
            }
            setShowEdit(false);
            setShowRealName(false);
        }else{
            setShowAccept(false);
            setShowTitleRight(false);
            setShowSent(false);
            setShowAdd(true);
            setShowEmail(true);
            setShowPhone(true);
            setShowAccepted(false);
            if(friend.getUserDetail().getPhone().equals("")){
                setShowPhone(false);
            }
            if(friend.getUserDetail().getEmail().equals("")){
                setShowEmail(false);
            }
            setShowEdit(false);
            setShowRealName(false);
        }
    }

    public View.OnClickListener getEditAliasListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoEditAlias(friend);
            }
        };
    }


    @Bindable
    public boolean getShowGender(){
        return !friend.getUserDetail().getGender().equals(User.UNDEFINED);
    }


    @Bindable
    public boolean getIsMale(){
        return friend.getUserDetail().getGender().equals(User.MALE);
    }

    @Bindable
    public String getName(){
        return friend.getAliasName();
    }

    @Bindable
    public String getRealName(){
        return friend.getUserDetail().getPersonalAlias();
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

    public void onRightClicked(){
                if(popupWindow==null){
                    initPopupWindow();
                }
                popupWindow.show();
    }

    public void initPopupWindow(){
        if(presenter.getView()==null){
            return;
        }
        popupWindow= new ActionSheetDialog(presenter.getView().getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true);
        if(getBlocked()) {
            popupWindow.addSheetItem(presenter.getContext().getString(R.string.unblock_contact),
                    null, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int i) {
                            if(!getBlocked()) {
                                if (blockDialog == null) {
                                    initBlockDialog();
                                }
                                blockDialog.show();
                            }else{
                                unblockFriend();
                            }
                        }
                    });
        }else{
            popupWindow.addSheetItem(presenter.getContext().getString(R.string.block_contact),
                    null, new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int i) {
                            if(!getBlocked()) {
                                if (blockDialog == null) {
                                    initBlockDialog();
                                }
                                blockDialog.show();
                            }else{
                                unblockFriend();
                            }
                        }
                    });
        }

        popupWindow.addSheetItem(presenter.getContext().getString(R.string.delete_contact),
               null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int i) {
                        if(deleteDialog==null){
                            initDeleteDialog();
                        }
                        deleteDialog.show();
                    }
                });
    }

    private void initDeleteDialog(){
        if(presenter.getView()==null){
            return;
        }
        deleteDialog = new AlertDialog(presenter.getView().getActivity())
                .builder()
                .setTitle("Delete this contact?")
                .setMsg("Invitation from this contact will not be shown in your calendar,"
                        +" but will be shown in your message box")
                .setPositiveButton("Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFriend();
                    }
                })
               .setNegativeButton("Cancel", new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                   }
               });
    }

    private void initBlockDialog(){
        if(presenter.getView()==null){
            return;
        }
        blockDialog = new AlertDialog(presenter.getView().getActivity())
                .builder()
                .setTitle("Block this contact?")
                .setMsg("Block this contact from sending you invitations")
                .setPositiveButton("Block", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        blockFriend();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }



    private void unblockFriend(){
        presenter.unblockUser(friend);
    }

    private void blockFriend(){
        presenter.blockUser(friend);
    }

    private void deleteFriend(){
        presenter.deleteUser(friend);
    }

    public void blockSuccess(){
        friend.setBlockLevel(1);
        notifyPropertyChanged(BR.blocked);
        initPopupWindow();
    }

    public void unblockSuccess(){
        friend.setBlockLevel(0);
        notifyPropertyChanged(BR.blocked);
        initPopupWindow();
    }

    @Bindable
    public boolean getShowAccept() {
        return showAccept;
    }

    public void setShowAccept(boolean showAccept) {
        this.showAccept = showAccept;
        notifyPropertyChanged(BR.showAccept);
    }

    @Bindable
    public boolean getShowAccepted() {
        return showAccepted;
    }

    public void setShowAccepted(boolean showAccepted) {
        this.showAccepted = showAccepted;
        notifyPropertyChanged(BR.showAccepted);
    }

    public void setRequest(FriendRequest request){
        this.request = request;
    }

    @Bindable
    public boolean getShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
        notifyPropertyChanged(BR.showEdit);
    }


}
