package org.unimelb.itime.ui.contact.Presenter;

import org.unimelb.itime.ui.contact.Widget.SearchBar;

/**
 * Created by 37925 on 2016/12/13.
 */

public class SearchBarPresenter {
    private SearchBar searchBar;
    private SearchBar.OnEditListener onEditListener;

    public SearchBar.OnEditListener getOnEditListener() {
        return onEditListener;
    }

    public void setOnEditListener(SearchBar.OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }
}
