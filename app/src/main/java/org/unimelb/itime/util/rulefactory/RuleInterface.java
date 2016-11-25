package org.unimelb.itime.util.rulefactory;

/**
 * Created by yuhaoliu on 25/11/16.
 */
public interface RuleInterface {
    long getStartTime();
    long getEndTime();
    String[] getRecurrence();
}
