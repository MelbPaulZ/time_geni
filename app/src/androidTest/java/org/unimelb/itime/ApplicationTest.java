package org.unimelb.itime;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.junit.Test;
import org.unimelb.itime.bean.Event;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


    @Test
    public boolean test1(){
        Event.ArrayOfStringConverter arrayOfStringConverter = new Event.ArrayOfStringConverter();
        String[] strings = {"EXDATE;VALUE=DATE:20161202",
                "RRULE:FREQ=DAILY;UNTIL=20161206;INTERVAL=1"};
        String rst = arrayOfStringConverter.convertToDatabaseValue(strings);
        String[] rst2 = arrayOfStringConverter.convertToEntityProperty(rst);
        Log.i("____________________", "test1: ");
        System.out.println(arrayOfStringConverter.convertToDatabaseValue(strings));
        return true;
    }
}