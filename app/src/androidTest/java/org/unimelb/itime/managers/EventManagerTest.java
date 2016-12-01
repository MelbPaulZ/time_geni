package org.unimelb.itime.managers;

import android.util.Log;

import org.junit.Test;
import org.unimelb.itime.bean.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Paul on 30/11/16.
 */
public class EventManagerTest {

    @Test
    public boolean test1(){
        List<Event> list1 = new ArrayList<>();
        Event event1 = new Event();
        list1.add(event1);

        List<Event> list2 = new ArrayList<>();
        Event event2 = new Event();
        Event event3 = new Event();
        list2.add(event2);
        list2.add(event3);

        Map<Long, List<Event>> map = new HashMap();
        map.put((long) 1000, list1);
        Map<Long, List<Event>> map2 = new HashMap();
        map.put((long) 2000, list2);

        ArrayList testList = new ArrayList();
        testList.addAll(map.values());
        testList.addAll(map2.values());

        Log.i("test", "test1: ");
        return true;
    }

}