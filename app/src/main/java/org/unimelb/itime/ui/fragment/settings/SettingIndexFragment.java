package org.unimelb.itime.ui.fragment.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.unimelb.itime.R;
import org.unimelb.itime.base.C;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentSettingBinding;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.managers.SettingManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.LoginActivity;
import org.unimelb.itime.ui.activity.ProfileActivityContact;
import org.unimelb.itime.ui.activity.SettingActivity;
import org.unimelb.itime.ui.mvpview.ItimeCommonMvpView;
import org.unimelb.itime.ui.mvpview.SettingCommonMvpView;
import org.unimelb.itime.ui.presenter.SettingCommonPresenter;
import org.unimelb.itime.ui.viewmodel.MainSettingsViewModel;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.util.AuthUtil;
import org.unimelb.itime.widget.QRCode.CaptureActivityContact;
import org.unimelb.itime.util.UserUtil;

import me.fesky.library.widget.ios.ActionSheetDialog;

/**
 * Created by Paul on 25/12/2016.
 */

public class SettingIndexFragment extends SettingBaseFragment<SettingCommonMvpView, SettingCommonPresenter<SettingCommonMvpView>>
implements SettingCommonMvpView{

    private FragmentSettingBinding binding;
    private final String TAG = "SettingIndexFragment";


    @Override
    public SettingCommonPresenter<SettingCommonMvpView> createPresenter() {
        return new SettingCommonPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setSettingVM(viewModel);

    }


    @Subscribe
    public void logout(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.LOGOUT){
            clearAccount();
            Intent i = new Intent(getContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            getActivity().finish();
        }
    }


    private void stopRemoteService(){
        Intent serviceI = new Intent(getContext(), RemoteService.class);
        getActivity().stopService(serviceI);
    }

    private void clearAccount(){
        UserUtil user = UserUtil.getInstance(getContext());
        user.logout();

        SettingManager stManager = SettingManager.getInstance(getContext());
        stManager.clear();

        AuthUtil.clearJwtToken(getContext());
        SharedPreferences sp = AppUtil.getTokenSaver(getContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(C.spkey.MESSAGE_LIST_SYNC_TOKEN, "");
        editor.putString(C.spkey.EVENT_LIST_SYNC_TOKEN, "");
        editor.apply();

        Log.i(TAG, "clearAccount: " + "clear DB manager");
        DBManager.getInstance(getContext()).deleteAllMessages();
        DBManager.getInstance(getContext()).clearDB();
        Log.i(TAG, "clearAccount: " + "clear Event manager");
        EventManager.getInstance(getContext()).clearManager();
    }

    /**
     * popup dialog for logout
     */
    private void popupDialog(){
        ActionSheetDialog actionSheetDialog= new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("Log Out", null,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                stopRemoteService();
                            }
                        });
                actionSheetDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        // when setting activity finish, this will get called to update viewmodel data
        viewModel.setSetting(SettingManager.getInstance(getContext()).getSetting());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Override
    public void onViewChange(int task, boolean isSave) {
        if (task == MainSettingsViewModel.TASK_LOGOUT){
            popupDialog();
        }else if (task == MainSettingsViewModel.TASK_TO_SCAN_QR_CODE ){
            Intent intent = new Intent(getActivity(), CaptureActivityContact.class);
            intent.putExtra(SettingActivity.TASK, task);
            startActivityForResult(intent, task);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(
                task == MainSettingsViewModel.TASK_TO_BLOCK_USER ||
                task == MainSettingsViewModel.TASK_TO_HELP_AND_FEEDBACK){
            Toast.makeText(getContext(), "todo",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            intent.putExtra(SettingActivity.TASK, task);
            startActivityForResult(intent, task);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                presenter.findFriend(result,new FindFriendCallBack());
            }
        }
    }

    public void gotoProfile(Contact contact){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProfileActivityContact.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProfileActivityContact.USER,contact);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void setLeftTitleStringToVM() {

    }

    @Override
    public void setTitleStringToVM() {
        toolbarViewModel.setTitleStr(getString(R.string.tab_setting));
    }

    @Override
    public void setRightTitleStringToVM() {

    }

    @Override
    public ToolbarViewModel<? extends ItimeCommonMvpView> getToolBarViewModel() {
        return new ToolbarViewModel<>(this);
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onNext() {

    }

    public class FindFriendCallBack{
        public void success(Contact contact){
            gotoProfile(contact);
        }

        public void failed(){
            Toast.makeText(getContext(), "Can't find this user!", Toast.LENGTH_SHORT);
        }
    }


}
