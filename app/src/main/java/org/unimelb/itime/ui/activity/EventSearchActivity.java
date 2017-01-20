package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import org.unimelb.itime.vendor.listener.ITimeEventInterface;
import org.unimelb.itime.widget.SearchBar;

import java.util.List;

/**
 * Created by Paul on 6/12/2016.
 */

public class EventSearchActivity extends EmptyActivity{

    private ListView eventSearchList;
    private EventAdapter eventSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        init();
        SearchBar searchBar = (SearchBar) findViewById(R.id.event_search_view);
        searchBar.setSearchListener(new SearchBar.OnEditListener() {
            @Override
            public void onEditing(View view, String text) {
                eventSearchAdapter.getFilter().filter(text);
            }
        });
    }

    private void init(){
        List<? extends ITimeEventInterface> eventList = EventManager.getInstance(getApplicationContext()).getAllEvents();
        eventSearchAdapter = new EventAdapter(getApplicationContext(), R.layout.event_single_search_view, (List<Event>) eventList);
        eventSearchList = (ListView) findViewById(R.id.event_search_result_listview);
        eventSearchList.setAdapter(eventSearchAdapter);
        eventSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(EventSearchActivity.this, EventDetailActivity.class);
                Event event = eventSearchAdapter.getItem(i);
                intent.putExtra("event_uid", event.getEventUid());
                intent.putExtra("start_time", event.getStartTime());
                startActivity(intent);

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
