package org.unimelb.itime.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.SideBarListViewBinding;
import org.unimelb.itime.ui.viewmodel.contact.SideBarListViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import me.tatarka.bindingcollectionadapter.ItemView;

/**
 * Created by 37925 on 2016/12/15.
 */

public class SideBarListView extends FrameLayout {
    private ListView listView;
    private SideBar sideBar;
    private TextView dialog;
    private SideBarListViewBinding binding;
    private Map<String, Integer> positionMap = new HashMap<>();
    private ObservableList<ContactItem> items;
    private SideBarListViewModel viewModel;

    public SideBarListView(Context context) {
        super(context);
        init();
    }

    public SideBarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBarListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setShowSideBar(boolean showSideBar){
        if(showSideBar){
            sideBar.setVisibility(VISIBLE);
        }else{
            sideBar.setVisibility(GONE);
        }
    }

    public void setData(ObservableList list, ItemView itemView){

        items = list;
        viewModel.setItemView(itemView);
        viewModel.setItems(items);
    }

    public void setItems(ObservableList list){
        items = list;
        sort(items);
        updatePositionMap(items);
        viewModel.setItems(items);
    }

    public void setItemView(ItemView itemView){
        viewModel.setItemView(itemView);
    }

    public void updateList(){
        sort(items);
        updatePositionMap(items);
    }

    private void updatePositionMap(ObservableList<ContactItem> list){
        if(positionMap==null){
            positionMap = new HashMap<>();
        }
        positionMap.clear();
        for(int i=0;i<list.size();i++){
            ContactItem item = list.get(i);
            String letter =item.getContact().getSortLetters();
            if(positionMap.containsKey(letter)){
                item.setShowFirstLetter(false);
                continue;
            }else{
                positionMap.put(letter, i);
                item.setShowFirstLetter(true);
            }
        }
    }

    public void init(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.side_bar_list_view, null, false);
        dialog = binding.dialog;
        sideBar = binding.sidrbar;
        sideBar.setTextView(dialog);
        listView = binding.sortedContactListView;
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (positionMap.containsKey(s)) {
                    listView.setSelection(positionMap.get(s));
                }
            }
        });
        viewModel = new SideBarListViewModel();
        binding.setViewModel(viewModel);
        this.addView(binding.getRoot());
    }

    private void sort(ObservableList<ContactItem> list){
        if(list!=null) {
            Collections.sort(list, new Comparator<ContactItem>() {
                @Override
                public int compare(ContactItem t1, ContactItem t2) {
                    return t1.getContact().compareTo(t2.getContact());
                }
            });
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        listView.setOnItemClickListener(listener);
    }

    public void hideSideBar() {
        sideBar.setVisibility(GONE);
    }

}
