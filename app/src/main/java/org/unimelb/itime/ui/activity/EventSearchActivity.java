package org.unimelb.itime.ui.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.adapter.EventAdapter;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.List;

/**
 * Created by Paul on 6/12/2016.
 */

public class EventSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView eventSearchList;
    private EventAdapter eventSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        init();
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.event_search_view);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
    }

    private void init(){
        List<? extends ITimeEventInterface> eventList = EventManager.getInstance(getApplicationContext()).getAllEvents();
        eventSearchAdapter = new EventAdapter(getApplicationContext(), R.layout.event_single_search_view, (List<Event>) eventList);
        eventSearchList = (ListView) findViewById(R.id.event_search_result_listview);
        eventSearchList.setAdapter(eventSearchAdapter);
        eventSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventUtil.startEditEventActivity(getApplicationContext(), EventSearchActivity.this, eventSearchAdapter.getItem(i));
            }
        });

        TextView cancelBtn = (TextView) findViewById(R.id.event_search_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void refreshAdapter(){
        List<? extends ITimeEventInterface> interfaceList = EventManager.getInstance(getApplicationContext()).getAllEvents();
        List<Event> eventList = (List<Event>)interfaceList;
        eventSearchAdapter.setEventList(eventList);
        eventSearchAdapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSearchAdapter(MessageEvent messageEvent){
        if (messageEvent.task == MessageEvent.RELOAD_EVENT){
            refreshAdapter();
        }
    }




}
