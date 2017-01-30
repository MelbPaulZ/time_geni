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
    private Contact contact;
    private String requestId;
    private String email = "";
    private String gender = "2";
    private String location = "";
    private String realName = "";
    private String name = "";
    private String phone = "";
    private String photo = "";
    private boolean blocked;

    private boolean showAdd = false;
    private boolean showSent = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
    private boolean showAccept = false;
    private boolean showAccepted = false;
    private boolean showEdit = false;
    private boolean showRealName = false;
    private boolean showGender = false;
    private ProfilePresenter presenter;
    private int genderIcon;

    public void setShowGender(boolean showGender) {
        this.showGender = showGender;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
        notifyPropertyChanged(BR.blocked);
    }

    @Bindable
    public boolean getShowRealName() {
        return showRealName;
    }

    public void setShowRealName(boolean showRealName) {
        this.showRealName = showRealName;
        notifyPropertyChanged(BR.showRealName);
    }


    public ProfileFragmentViewModel(ProfilePresenter presenter){
        this.presenter = presenter;
    }

    @Bindable
    public View.OnClickListener getInvitenListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.inviteUser();
            }
        };
    }

    @Bindable
    public View.OnClickListener getAddListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addUser(contact);
            }
        };
    }

    @Bindable
    public View.OnClickListener getAcceptListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptRequest(requestId, contact.getUserDetail());
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

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.emailText);
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }

    public void setRealName(String realName) {
        this.realName = realName;
        notifyPropertyChanged(BR.realName);
        notifyPropertyChanged(BR.realNameText);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    @Bindable
    public int getGenderIcon() {
        return genderIcon;
    }

    public void setGenderIcon(String gender) {
        switch (gender){
            case User.FEMALE:
                genderIcon = R.drawable.female_icon;
                break;
            case User.MALE:
                genderIcon = R.drawable.male_icon;
                break;
        }
        notifyPropertyChanged(BR.genderIcon);
    }

    @Bindable
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        if(contact ==null){
            return;
        }
        this.contact = contact;
        setLocation(contact.getUserDetail().getLocation());
        setEmail(contact.getUserDetail().getEmail());
        setPhoto(contact.getAliasPhoto());
        setPhone(contact.getUserDetail().getPhone());
        setGender(contact.getUserDetail().getGender());
        setName(contact.getAliasName());
        setRealName(contact.getUserDetail().getPersonalAlias());
        setBlocked(contact.getBlockLevel()!=0);
        setShowGender(!contact.getUserDetail().getGender().equals(User.UNDEFINED));
        setGenderIcon(contact.getUserDetail().getGender());
    }

    @Bindable
    public String getPhoto(){
        return photo;
    }

    public void contactMode(){
        setShowAdd(false);
        setShowEmail(true);
        setShowPhone(true);
        setShowSent(false);
        setShowAccept(false);
        setShowAccepted(false);
        setShowEdit(true);
        setShowRealName(true);
    }

    public void requestMode(){
        setShowSent(false);
        setShowAdd(false);
        setShowEmail(true);
        setShowPhone(true);
        setShowAccept(true);
        setShowAccepted(false);
        if(contact.getUserDetail().getPhone().equals("")){
            setShowPhone(false);
        }
        if(contact.getUserDetail().getEmail().equals("")){
            setShowEmail(false);
        }
        setShowEdit(false);
        setShowRealName(false);
    }

    public void strangerMode(){
        setShowAccept(false);
        setShowSent(false);
        setShowAdd(true);
        setShowEmail(true);
        setShowPhone(true);
        setShowAccepted(false);
        if(contact.getUserDetail().getPhone().equals("")){
            setShowPhone(false);
        }
        if(contact.getUserDetail().getEmail().equals("")){
            setShowEmail(false);
        }
        setShowEdit(false);
        setShowRealName(false);
    }

    public View.OnClickListener getEditAliasListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.gotoEditAlias();
            }
        };
    }


    @Bindable
    public boolean getShowGender(){
        return !gender.equals(User.UNDEFINED);
    }


    @Bindable
    public String getName(){
        return name;
    }

    @Bindable
    public String getRealName(){
        return realName;
    }

    @Bindable
    public String getLocation(){
        return location;
    }
    @Bindable
    public String getPhone(){
        return phone;
    }

    @Bindable
    public String getEmail(){
        return email;
    }

    @Bindable
    public String getEmailText(){
        return  presenter.getContext().getString(R.string.email)+": " + email;
    }

    @Bindable
    public String getRealNameText(){
        return presenter.getContext().getString(R.string.name)+": " + realName;
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
        return blocked;
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
        presenter.unblockUser(contact);
    }

    private void blockFriend(){
        presenter.blockUser(contact);
    }

    private void deleteFriend(){
        presenter.deleteUser(contact);
    }

    public void blockSuccess(){
        contact.setBlockLevel(1);
        setBlocked(true);
        initPopupWindow();
    }

    public void unblockSuccess(){
        contact.setBlockLevel(0);
        setBlocked(false);
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

    public void setRequestId(String requestId){
        this.requestId = requestId;
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
