package org.unimelb.itime.ui.fragment.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.managers.DBManager;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.fragment.event.EventCreateDetailBeforeSendingFragment;
import org.unimelb.itime.ui.fragment.event.EventCreateNewFragment;
import org.unimelb.itime.ui.fragment.event.EventTimeSlotViewFragment;
import org.unimelb.itime.ui.fragment.event.EventDetailTimeSlotFragment;
import org.unimelb.itime.ui.fragment.event.EventEditFragment;
import org.unimelb.itime.ui.presenter.InviteePresenter;
import org.unimelb.itime.util.AppUtil;
import org.unimelb.itime.vendor.contact.SortAdapter;
import org.unimelb.itime.vendor.contact.helper.CharacterParser;
import org.unimelb.itime.vendor.contact.helper.ClearEditText;
import org.unimelb.itime.vendor.contact.helper.PinyinComparator;
import org.unimelb.itime.vendor.contact.helper.PublicEntity;
import org.unimelb.itime.vendor.contact.widgets.SideBar;
import org.unimelb.itime.vendor.contact.widgets.SortModel;
import org.unimelb.itime.vendor.helper.LoadImgHelper;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InviteeFragment extends BaseUiFragment {
    private static final String TAG = "MyAPP";

    public Map<ITimeContactInterface, ImageView> contacts_list = new HashMap<>();
    private Map<String, ITimeContactInterface> contacts = new HashMap<>();//id contact

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;
    private View root;
    private Context context;
    private InviteeFragment self;
    private Event event;
    private String tag;
    private View thePublicView;
    private EventManager eventManager;

    private LinearLayout ll_checkedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        root = inflater.inflate(R.layout.fragment_event_attendee_picker, container, false);
        self = this;
        return root;
    }

    @Override
    public MvpPresenter createPresenter() {
        return new InviteePresenter(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eventManager = EventManager.getInstance(context);
        initView();
        initData();
        initListener();
    }

    public List<ITimeContactInterface> getAllSelectedContacts(){
        ArrayList<ITimeContactInterface> result = new ArrayList<>();

        Iterator it = this.contacts_list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if (pair.getKey() instanceof PublicEntity){
                result.clear();
                result.add((ITimeContactInterface)pair.getKey());
                return result;
            }
            result.add((ITimeContactInterface)pair.getKey());
        }

        return result;
    }
    private void initView() {

        sideBar = (SideBar) root.findViewById(R.id.sidrbar);
        dialog = (TextView) root.findViewById(R.id.dialog);
        ll_checkedList = (LinearLayout) root.findViewById(R.id.checked_contacts_list);

        sortListView = (ListView) root.findViewById(R.id.sortlist);
    }

    private void initData() {
        //set body
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                int nearestPreChar = adapter.findNearestPreMatch(s.toUpperCase().charAt(0));
                int position = adapter.getPositionForSection(nearestPreChar);
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        new ContactsAsyncTask().execute(0);

    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private class ContactsAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            int result = -1;
            //load contacts info
            List<? extends ITimeContactInterface> contact_models = loadContacts();

            for (ITimeContactInterface contact_model :contact_models
                    ) {
                contacts.put(contact_model.getContactUid(), contact_model);
            }

            result = 1;

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                SourceDateList = filledData(contacts);

                Collections.sort(SourceDateList, pinyinComparator);

                adapter = new SortAdapter(getActivity().getApplicationContext(), SourceDateList, contacts);
                sortListView.setAdapter(adapter);

                mClearEditText = (ClearEditText) root
                        .findViewById(R.id.filter_edit);
                mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View arg0, boolean arg1) {
                        mClearEditText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

                    }
                });
                mClearEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        filterData(s.toString().toLowerCase());
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                //connect checked list
                adapter.setCircleCheckOnClickListener(new SortAdapter.CircleCheckOnClickListener() {
                    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                    int margin = width/40;
                    @Override
                    public void synCheckedContactsList(ITimeContactInterface contact, boolean add) {
                        if (add) {
                            ImageView img_v = new ImageView(context);
                            img_v.setOnClickListener(new ContactViewTouchListener());
                            img_v.setTag(contact);

                            int imgParam = (int) context.getResources().getDimension(R.dimen.invitee_icon_width);
                            LinearLayout.LayoutParams params =
                                    new LinearLayout.LayoutParams(imgParam, imgParam);
                            params.setMargins(margin, margin / 2, 0, margin / 2);
                            params.gravity = Gravity.CENTER_VERTICAL;
                            img_v.setLayoutParams(params);

                            if (!(contact instanceof PublicEntity)) {
                                LoadImgHelper.getInstance().bindContactWithImageView(
                                        context, contact, img_v);
                            }else {
                                //if The Public is selected
                                int count = ll_checkedList.getChildCount();
                                for (int i = 0; i < count; i++) {
                                    ll_checkedList.getChildAt(i).setVisibility(View.GONE);
                                }
                                img_v.setImageResource(R.drawable.invitee_selected_default_picture);
                                adapter.updateListView(new ArrayList<SortModel>());
                                mClearEditText.setVisibility(View.GONE);
                            }
                            contacts_list.put(contact, img_v);
                            ll_checkedList.addView(img_v);
                            ll_checkedList.invalidate();
                        } else {
                            if (!(contact instanceof PublicEntity)){

                            }else {
                                int count = ll_checkedList.getChildCount();
                                for (int i = 0; i < count; i++) {
                                    ll_checkedList.getChildAt(i).setVisibility(View.VISIBLE);
                                }
                                mClearEditText.setVisibility(View.VISIBLE);
                                filterData("");
                            }
                            ll_checkedList.removeView(contacts_list.get(contact));
                            contacts_list.remove(contact);
                            ll_checkedList.invalidate();
                        }
                    }

                    @Override
                    public Map getMapInContactsList() {
                        return contacts_list;
                    }
                });
//                addPublicHeaderView();

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private void addPublicHeaderView(){
        TextView tvLetter;
        TextView tvTitle;
        ImageView icon;
        ImageView check_circle;

        thePublicView = LayoutInflater.from(context).inflate(org.unimelb.itime.vendor.R.layout.itime_contact_item, null);
        int width = context.getResources().getDisplayMetrics().widthPixels;

        tvTitle = (TextView) thePublicView.findViewById(org.unimelb.itime.vendor.R.id.title);
        tvTitle.setText("The Public");

        tvLetter = (TextView) thePublicView.findViewById(org.unimelb.itime.vendor.R.id.catalog);
        ((ViewGroup) thePublicView).removeView(tvLetter);

        icon = (ImageView) thePublicView.findViewById(org.unimelb.itime.vendor.R.id.icon);
        LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
        icon.setLayoutParams(iconParams);
        icon.setImageResource(R.drawable.invitee_selected_default_picture);

        check_circle = (ImageView) thePublicView.findViewById(org.unimelb.itime.vendor.R.id.check_circle);
        LinearLayout.LayoutParams circleParams = (LinearLayout.LayoutParams) check_circle.getLayoutParams();;
        check_circle.setLayoutParams(circleParams);
        check_circle.setImageResource(R.drawable.invitee_selected_check_circle);

        PublicEntity publicEntity = new PublicEntity();

        SortAdapter.CircleClickListener publicCircleCheckOnClickListener;
        publicCircleCheckOnClickListener = adapter.new CircleClickListener(publicEntity, check_circle);
        thePublicView.setOnClickListener(publicCircleCheckOnClickListener);

        sortListView.addHeaderView(thePublicView);

    }

    private List<SortModel> filledData(Map<String,ITimeContactInterface> map) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(Map.Entry<String,ITimeContactInterface> contact : map.entrySet()){
            SortModel sortModel = new SortModel();
            sortModel.setName(contact.getValue().getName());
            sortModel.setId(contact.getValue().getContactUid());
            String pinyin = characterParser.getSelling(contact.getValue().getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }

        return mSortList;
    }

    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName().toLowerCase();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    class ContactViewTouchListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ViewGroup parent = (ViewGroup) view.getParent();

            if (parent != null){
                parent.removeView(view);
                contacts_list.remove(view.getTag());
                adapter.notifyDataSetChanged();
            }
        }
    }


    public void setSelectInvitees(){
        // get all invitess
        ArrayList<Invitee> invitees = new ArrayList<>();
        ArrayList<ITimeContactInterface> contacts = (ArrayList)getAllSelectedContacts();

        if (hasPublicEntity(contacts)){
            event.setEventType("public");
            event.getInvitee().clear();
        }else{
            for (ITimeContactInterface iTimeContactInterface : contacts) {

                Invitee invitee = contactToInvitee((Contact) iTimeContactInterface, event); // convert contact to invitee
                invitees.add(invitee);
            }
            event.setInvitee(invitees);
        }
    }

    public boolean hasPublicEntity(ArrayList<ITimeContactInterface> contacts){
        for (ITimeContactInterface iTimeContactInterface : contacts) {
            if (iTimeContactInterface instanceof PublicEntity)
                return true;
        }
        return false;
    }

    public void initListener() {
        Button cancelBtn = (Button) root.findViewById(R.id.attendee_picker_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFrom() instanceof EventCreateNewFragment){
                    self.resetAll();
                    EventCreateNewFragment eventCreateNewFragment = (EventCreateNewFragment) getFragmentManager().findFragmentByTag(EventCreateNewFragment.class.getSimpleName());
                    openFragment(self, eventCreateNewFragment);
                }else if (getFrom() instanceof EventTimeSlotViewFragment){
                    EventCreateNewFragment eventCreateNewFragment = (EventCreateNewFragment) getFragmentManager().findFragmentByTag(EventCreateNewFragment.class.getSimpleName());
                    openFragment(self, eventCreateNewFragment);
                } else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment){
                    openFragment(self, (EventCreateDetailBeforeSendingFragment)getFrom());
                }else if (getFrom() instanceof EventEditFragment){
                    openFragment(self, (EventEditFragment)getFrom());
                }
            }
        });

        Button nextBtn = (Button) root.findViewById(R.id.attendee_picker_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectInvitees();
                if(getFrom() instanceof EventCreateNewFragment){
                    if (event.getInvitee().size()>=1) {
                        // pick at least one invitee
                        EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
                        eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, eventTimeSlotViewFragment);
                    }else{
                        EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
                        beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, beforeSendingFragment);
                    }
                }else if (getFrom() instanceof EventTimeSlotViewFragment){
                    if (event.getInvitee().size()>=1) {
                        EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFrom();
                        eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
                        eventTimeSlotViewFragment.resetCalendar(event);
                        openFragment(self, (EventTimeSlotViewFragment) getFrom());
                    }else{
                        EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
                        beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, beforeSendingFragment);
                    }
                }else if (getFrom() instanceof EventCreateDetailBeforeSendingFragment){
                    if (event.getInvitee().size()>=1) {
                        EventTimeSlotViewFragment eventTimeSlotViewFragment = (EventTimeSlotViewFragment) getFragmentManager().findFragmentByTag(EventTimeSlotViewFragment.class.getSimpleName());
                        eventTimeSlotViewFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, eventTimeSlotViewFragment);
                    }else{
                        EventCreateDetailBeforeSendingFragment beforeSendingFragment = (EventCreateDetailBeforeSendingFragment) getFragmentManager().findFragmentByTag(EventCreateDetailBeforeSendingFragment.class.getSimpleName());
                        beforeSendingFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, beforeSendingFragment);
                    }
                }else if (getFrom() instanceof EventEditFragment){
                    if (event.getInvitee().size()>=1) {
                        EventDetailTimeSlotFragment eventDetailTimeSlotFragment = (EventDetailTimeSlotFragment) getFragmentManager().findFragmentByTag(EventDetailTimeSlotFragment.class.getSimpleName());
                        eventDetailTimeSlotFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, eventDetailTimeSlotFragment);
                    }else{
                        EventEditFragment eventEditFragment = (EventEditFragment) getFragmentManager().findFragmentByTag(EventEditFragment.class.getSimpleName());
                        eventEditFragment.setEvent(eventManager.copyCurrentEvent(event));
                        openFragment(self, eventEditFragment);
                    }
                }
            }
        });
    }




    private Invitee contactToInvitee(Contact contact, Event event) {
        Invitee invitee = new Invitee();
        invitee.setEventUid(event.getEventUid());
        // need to check if the contact in the invitee list
        invitee.setInviteeUid(getInviteeUid(contact,event));
        invitee.setUserUid(contact.getContactUid());
        invitee.setUserId(contact.getAliasName());
        invitee.setStatus("needsAction");
        invitee.setAliasPhoto(contact.getPhoto());
        invitee.setAliasName(contact.getName());
        return invitee;
    }

    /** if the contact is already in the event's invitees, then no need of setting up a new InviteeUid,
     * otherwise use its previous inviteeUid
     * */
    private String getInviteeUid(Contact contact,Event event){
        Log.i(TAG, "getInviteeUid: ");
        for (Invitee invitee : event.getInvitee()){
            if(invitee.getUserUid().equals(contact.getContactUid())){
                return invitee.getInviteeUid();
            }
        }
        return AppUtil.generateUuid();
    }

    public void resetAll(){
        this.ll_checkedList.removeAllViews();
        this.contacts_list.clear();
        filterData("");
    }

    public void setEvent(Event event){
        this.event = event;
    }



    /***********************************
     * load contacts
     ***********************************************/
    /*********************************** load contacts ***********************************************/
    public List<? extends ITimeContactInterface> loadContacts() {
        return DBManager.getInstance(getContext()).getAllContact();
    }




}
