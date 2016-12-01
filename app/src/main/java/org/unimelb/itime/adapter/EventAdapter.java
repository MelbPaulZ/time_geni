package org.unimelb.itime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.managers.EventManager;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.vendor.listener.ITimeEventInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Paul on 30/11/16.
 */
public class EventAdapter extends ArrayAdapter<Event> implements Filterable {

    private int resource;
    private List<Event> eventList;
    private List<Event> searchList;
    private EventFilter eventFilter;

    public EventAdapter(Context context, int resource, List<Event> eventList) {
        super(context, resource, eventList);
        this.resource = resource;
        this.eventList = eventList;
        getFilter();
    }

    public void setEventList(List<Event> eventList){
        this.eventList = eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resource,null);
        TextView summary = (TextView) view.findViewById(R.id.event_summary);
        summary.setText(event.getTitle());

        TextView location = (TextView) view.findViewById(R.id.event_location);
        location.setText(event.getLocation());

        TextView startTime = (TextView) view.findViewById(R.id.event_starttime);
        startTime.setText(getEventStartString(event.getStartTime()));

        TextView duration = (TextView) view.findViewById(R.id.event_duration);
        duration.setText(getDurationString(event.getStartTime(), event.getEndTime()));

        TextView gapHour = (TextView) view.findViewById(R.id.gap_hour);
        gapHour.setText(getGapHourString(event.getStartTime(), event.getEndTime()));
        return view;
    }

    private String getEventStartString(long starttime){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(starttime);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        String amOrPm = calendar.get(Calendar.HOUR_OF_DAY) > 12? "PM" : "AM";
        String h = hour < 10? "0"+hour : hour+"";
        String m = min < 10? "0" + min : min + "";
        return h +":" + m  + " " + amOrPm;
    }

    private String getDurationString(long startTime, long endTime){
        int hour = (int) ((endTime - startTime)/1000/3600);
        int min = (int) (((endTime - startTime)/1000/60)%60);
        String h = hour>0? hour + "hour " : "";
        String m = min  + "min";
        return h + m;
    }

    private String getGapHourString(long startTime, long endTime){
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        if (currentTime > startTime){
            if (currentTime > endTime ) {
                return "past";
            }else{
                return "now";
            }
        }else if (currentTime < startTime){
            int hour = (int) (((startTime - currentTime)/1000)/3600);
            if (hour == 0){
                int min = (int) ((startTime - currentTime)/1000/60);
                return "In "+min+" mins";
            }else{
                return "In "+ hour + " hrs";
            }
        }else
            return "error";

    }

    @Override
    public int getCount() {
        if (searchList!=null){
            return searchList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Event getItem(int i) {
        if (searchList!=null)
            return searchList.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public Filter getFilter() {
        if (eventFilter == null){
            eventFilter = new EventFilter();
        }
        return eventFilter ;

    }

    private class EventFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constrains) {
            FilterResults filterResults = new FilterResults();
            if (constrains!=null && constrains.length()>0){
                ArrayList<Event> matchList = new ArrayList<>();
                for (Event event : eventList){
                    if (event.getSummary().toLowerCase().contains(constrains.toString().toLowerCase())){
                        matchList.add(event);
                    }else if (event.getDescription().toLowerCase().contains(constrains.toString().toLowerCase())){
                        matchList.add(event);
                    }
                }

                filterResults.values = matchList;
                filterResults.count = matchList.size();
            }else{
                filterResults.values = null;
                filterResults.count = 0;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            searchList = (List<Event>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
