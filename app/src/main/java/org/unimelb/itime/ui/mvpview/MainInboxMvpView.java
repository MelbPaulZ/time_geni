package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Message;

import java.util.List;

/**
 * Created by Paul on 1/12/16.
 */
public interface MainInboxMvpView extends TaskBasedMvpView<Object>, ItimeCommonMvpView{

    void onFilterMessage(List<Message> list);
    void toEventDetailPage(Event event);
}
