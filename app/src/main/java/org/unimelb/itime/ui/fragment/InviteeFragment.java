package org.unimelb.itime.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.messageevent.MessageInvitees;
import org.unimelb.itime.testdb.DBManager;
import org.unimelb.itime.ui.activity.EventCreateActivity;
import org.unimelb.itime.ui.activity.EventDetailActivity;
import org.unimelb.itime.ui.fragment.eventcreate.EventCreateNewFragment;
import org.unimelb.itime.ui.presenter.EmptyPresenter;
import org.unimelb.itime.vendor.contact.SortAdapter;
import org.unimelb.itime.vendor.contact.helper.CharacterParser;
import org.unimelb.itime.vendor.contact.helper.ClearEditText;
import org.unimelb.itime.vendor.contact.helper.PinyinComparator;
import org.unimelb.itime.vendor.contact.widgets.SideBar;
import org.unimelb.itime.vendor.contact.widgets.SortModel;
import org.unimelb.itime.vendor.helper.LoadImgHelper;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        root = inflater.inflate(R.layout.fragment_event_attendee_picker, container, false);
        // Inflate the layout for this fragment
        //set load methods

        self = this;
//		initView();
//		initData();
//		initListener();
        return root;
    }

    @Override
    public MvpPresenter createPresenter() {
        return new EmptyPresenter();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    public ArrayList<ITimeContactInterface> getAllSelectedContacts() {
        return new ArrayList<>(this.contacts_list.keySet());
    }

    private void initView() {

        sideBar = (SideBar) root.findViewById(R.id.sidrbar);
        dialog = (TextView) root.findViewById(R.id.dialog);

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
            for (ITimeContactInterface contact_model : contact_models
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
                        mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

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
                    LinearLayout ll_checkedList = (LinearLayout) root.findViewById(R.id.checked_contacts_list);
                    int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                    int margin = width / 40;

                    @Override
                    public void synCheckedContactsList(ITimeContactInterface contact, boolean add) {
                        if (add) {
                            ImageView img_v = new ImageView(context);
                            img_v.setOnClickListener(new ContactViewTouchListener());
                            img_v.setTag(contact);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width / 8, width / 8);
                            params.setMargins(margin, margin / 2, 0, margin / 2);
                            img_v.setLayoutParams(params);
                            contacts_list.put(contact, img_v);
                            LoadImgHelper.getInstance().bindContactWithImageView(
                                    context, contact, img_v);
                            ll_checkedList.addView(img_v);
                            ll_checkedList.invalidate();
                            Log.i(TAG, "add: ");
                        } else {
                            ll_checkedList.removeView(contacts_list.get(contact));
                            contacts_list.remove(contact);
                            ll_checkedList.invalidate();
                            Log.i(TAG, "remove: ");
                        }
                    }

                    @Override
                    public Map getMapInContactsList() {
                        return contacts_list;
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private List<SortModel> filledData(Map<String, ITimeContactInterface> map) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (Map.Entry<String, ITimeContactInterface> contact : map.entrySet()) {
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

    public void initListener() {
        Button cancelBtn = (Button) root.findViewById(R.id.attendee_picker_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFrom() instanceof EventCreateNewFragment){
                    EventCreateNewFragment eventCreateNewFragment = (EventCreateNewFragment) getFragmentManager().findFragmentByTag(EventCreateNewFragment.class.getSimpleName());
                    eventCreateNewFragment.setFrom(self);
                    switchFragment(self, eventCreateNewFragment);
                }
            }
        });


//        Button nextBtn = (Button) getActivity().findViewById(R.id.attendee_picker_next_btn);
//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (tag == getString(R.string.tag_host_event_edit)){
//                    ArrayList<Invitee> invitees = new ArrayList<Invitee>();
//                    ArrayList<ITimeContactInterface> contacts = getAllSelectedContacts();
//                    for (ITimeContactInterface iTimeContactInterface : contacts) {
//                        Invitee invitee = contactToInvitee((Contact) iTimeContactInterface, event); // convert contact to invitee
//                        invitees.add(invitee);
//                    }
//                    EventBus.getDefault().post(new MessageInvitees(tag, invitees));
//                    ((EventDetailActivity)getActivity()).fromInviteeToEditEvent();
//                }else if (tag == getString(R.string.tag_create_event)){
//                    Bundle bundle = getArguments();
//                    event = (Event) bundle.getSerializable(getString(R.string.new_event));
//                    ArrayList<Invitee> invitees = new ArrayList<Invitee>();
//                    ArrayList<ITimeContactInterface> contacts = getAllSelectedContacts();
//                    for (ITimeContactInterface iTimeContactInterface : contacts) {
//                        Invitee invitee = contactToInvitee((Contact) iTimeContactInterface, event); // convert contact to invitee
//                        invitees.add(invitee);
//                    }
//
//                    event.setInvitee(invitees);
//                    Bundle newBundle = new Bundle();
//                    newBundle.putSerializable(getString(R.string.new_event), event);
//                    ((EventCreateActivity) getActivity()).toTimeSlotView(self, newBundle);
//                }else if (tag == getString(R.string.tag_create_event_before_sending)){
//                    ArrayList<Invitee> invitees = new ArrayList<Invitee>();
//                    ArrayList<ITimeContactInterface> contacts = getAllSelectedContacts();
//                    for (ITimeContactInterface iTimeContactInterface : contacts) {
//                        Invitee invitee = contactToInvitee((Contact) iTimeContactInterface, event); // convert contact to invitee
//                        invitees.add(invitee);
//                    }
//                    EventBus.getDefault().post(new MessageInvitees(tag,invitees));
//                    ((EventCreateActivity)getActivity()).toNewEventDetailBeforeSending(self);
//
//                }
//            }
//        });
//
//        Button cancelBtn = (Button) root.findViewById(R.id.attendee_picker_cancel_btn);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (tag.equals(getString(R.string.tag_host_event_edit))){
//                    ((EventDetailActivity)getActivity()).fromInviteeToEditEvent();
//                }else if (tag.equals(getString(R.string.tag_create_event))){
//                    ((EventCreateActivity)getActivity()).toCreateEventNewFragment(self);
//                }else if (tag.equals(getString(R.string.tag_create_event_before_sending))){
//                    ((EventCreateActivity)getActivity()).toNewEventDetailBeforeSending(self);
//                }
//            }
//        });

    }

    private Invitee contactToInvitee(Contact contact, Event event) {
        Invitee invitee = new Invitee();
        invitee.setEventUid(event.getEventUid());
        invitee.setInviteeUid(contact.getContactUid());
        invitee.setContact(contact);

        return invitee;
    }

    public void setEvent(Event event){
        this.event = event;
    }


    class ContactViewTouchListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            ViewGroup parent = (ViewGroup) view.getParent();

            if (parent != null) {
                parent.removeView(view);
                contacts_list.remove(view.getTag());
                adapter.notifyDataSetChanged();
            }
        }
    }

    /***********************************
     * load contacts
     ***********************************************/
    public List<? extends ITimeContactInterface> loadContacts() {
        return DBManager.getInstance(context).getAllContact();
    }

    private List<ITimeContactInterface> simulateContacts() {
        List<ITimeContactInterface> contacts = new ArrayList<>();
        contacts.add(new Contact(null, "赵普", "1"));
        contacts.add(new Contact(null, "AGE", "2"));
        contacts.add(new Contact(null, "B", "3"));
        contacts.add(new Contact(null, "C", "4"));
        contacts.add(new Contact(null, "D", "5"));
        contacts.add(new Contact(null, "F", "6"));
        contacts.add(new Contact(null, "Crron", "7"));
        contacts.add(new Contact(null, "Bob", "8"));
        contacts.add(new Contact("http://esczx.baixing.com/uploadfile/2016/0427/20160427112336847.jpg", "周二珂", "9"));

        return contacts;
    }

}
