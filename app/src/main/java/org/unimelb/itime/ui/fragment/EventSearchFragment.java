package org.unimelb.itime.ui.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventAdapter;
import org.unimelb.itime.base.BaseUiFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.presenter.CommonPresenter;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 30/11/16.
 */
public class EventSearchFragment extends BaseUiFragment implements SearchView.OnQueryTextListener{

    private View root;
    private ListView eventSearchList;
    private EventAdapter eventSearchAdapter;
    private EventSearchFragment self;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_event, container, false);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        self = this;
        init();
        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) root.findViewById(R.id.event_search_view);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);


    }

    private void init(){
        List<? extends ITimeEventInterface> eventList = EventManager.getInstance().getAllEvents();
        eventSearchAdapter = new EventAdapter(getContext(), R.layout.event_single_search_view, (List<Event>) eventList);
        eventSearchList = (ListView) root.findViewById(R.id.event_search_result_listview);
        eventSearchList.setAdapter(eventSearchAdapter);
        eventSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventUtil.startEditEventActivity(getContext(), self.getActivity(), eventSearchAdapter.getItem(i));
            }
        });

        TextView cancelBtn = (TextView) root.findViewById(R.id.event_search_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(self, (MainCalendarFragment)getFrom());
            }
        });
    }

    public void refreshAdapter(){
        List<? extends ITimeEventInterface> interfaceList = EventManager.getInstance().getAllEvents();
        List<Event> eventList = (List<Event>)interfaceList;
        eventSearchAdapter.setEventList(eventList);
        eventSearchAdapter.notifyDataSetChanged();
    }


    @Override
    public MvpPresenter createPresenter() {
        return new CommonPresenter(getContext());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String constrains) {
        eventSearchAdapter.getFilter().filter(constrains);
        return false;
    }
}
