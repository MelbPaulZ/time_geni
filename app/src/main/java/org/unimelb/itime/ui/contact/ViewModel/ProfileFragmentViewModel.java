package org.unimelb.itime.ui.contact.ViewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.contact.Beans.ITimeUser;
import org.unimelb.itime.ui.contact.Fragments.ContactDialog;
import org.unimelb.itime.ui.contact.Presenter.ProfileFragmentPresenter;
import org.unimelb.itime.ui.contact.Widget.ContactPopupWindow;


/**
 * Created by 37925 on 2016/12/9.
 */

public class ProfileFragmentViewModel extends BaseObservable {
    private ContactDialog blockDialog;
    private ContactDialog deleteDialog;
    private ContactPopupWindow popupWindow;
    private ITimeUser friend;
    private boolean showTitileBack = true;
    private boolean showTitleRight = true;
    private boolean showGender = true;
    private boolean showAdd = false;
    private boolean showSent = false;
    private boolean showEmail = true;
    private boolean showPhone = true;
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
    }

    @Bindable
    public boolean getShowTitleRight() {
        return showTitleRight;
    }

    public void setShowTitleRight(boolean showTitleRight) {
        this.showTitleRight = showTitleRight;
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

    public boolean getShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean getShowPhone() {
        return showPhone;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    @Bindable
    public ITimeUser getFriend() {
        return friend;
    }

    public void setFriend(ITimeUser friend) {
        this.friend = friend;
        notifyPropertyChanged(BR.friend);
        notifyPropertyChanged(BR.location);
        notifyPropertyChanged(BR.email);
        notifyPropertyChanged(BR.phone);
        notifyPropertyChanged(BR.isMale);
        notifyPropertyChanged(BR.name);
        notifyPropertyChanged(BR.blocked);
        notifyPropertyChanged(BR.showGender);
    }

    @Bindable
    public boolean getShowGender(){
        return !"".equals(friend.getSex());
    }


    @Bindable
    public boolean getIsMale(){
        return friend.getSex().equals("male");
    }

    @Bindable
    public String getName(){
        return friend.getName();
    }
    @Bindable
    public String getLocation(){
        return friend.getState()+", "+friend.getCountry();
    }
    @Bindable
    public String getPhone(){
        return "Phone: "+friend.getPhone();
    }

    @Bindable
    public String getEmail(){
        return friend.getEmail();
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
        return friend.getBlocked();
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
        deleteDialog.setDoText("Delete");
        deleteDialog.setDoOnClickListener(new View.OnClickListener() {
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
        blockDialog.setDoText("Block");
        blockDialog.setDoOnClickListener(new View.OnClickListener() {
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
            friend.setBlocked(true);
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
            friend.setBlocked(false);
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
}
