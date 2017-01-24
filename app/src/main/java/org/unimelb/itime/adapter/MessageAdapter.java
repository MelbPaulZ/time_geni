package org.unimelb.itime.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.ui.presenter.MainInboxPresenter;
import org.unimelb.itime.ui.viewmodel.InboxViewModel.ItemViewModel;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.bindingcollectionadapter.ItemView;


/**
 * Created by Paul on 1/12/16.
 */
public class MessageAdapter extends BaseAdapter{
    private List<ItemViewModel> data;
    private Context context;
    private MainInboxPresenter presenter;
    private LayoutInflater inflater;

    private int layouts[];

    public MessageAdapter(Context context, MainInboxPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        this.data = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.layouts = new int[]{
                R.layout.listview_inbox_invitee_normal,
                R.layout.listview_inbox_invitee_deleted,
                R.layout.listview_inbox_host_confirmed_normal,
                R.layout.listview_inbox_host_confirmed_deleted,
                R.layout.listview_inbox_host_unconfirmed_normal,
                R.layout.listview_inbox_host_unconfirmed_deleted
        };
    }

    public void setData(List<ItemViewModel> data){
        this.data = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public int getViewTypeCount() {
        return layouts.length;
    }

    /**
     * not reuse any convertView, because the view in each cell might be different
     */
    @Override
    public int getItemViewType(int position) {
        ItemViewModel vm = data.get(position);
        switch (vm.getMessage().getTemplate()){
            case Message.TPL_INVITEE_NORMAL:
                return 0;
            case Message.TPL_INVITEE_DELETED:
                return 1;
            case Message.TPL_HOST_CONFIRMED_NORMAL:
                return 2;
            case Message.TPL_HOST_CONFIRMED_DELETED:
                return 3;
            case Message.TPL_HOST_UNCONFIRMED_NORMAL:
                return 4;
            case Message.TPL_HOST_UNCONFIRMED_DELETED:
                return 5;
        }
        throw new RuntimeException("Message type not found : " + vm.getMessage().getTemplate());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ItemViewModel viewModel = data.get(position);
        ViewDataBinding binding;
        int layoutRes = layouts[getItemViewType(position)];

        if(convertView == null){
            binding = DataBindingUtil.inflate(inflater, layoutRes, viewGroup, false);
        }else{
            binding = DataBindingUtil.getBinding(convertView);
        }

        bindVariable(binding, BR.vm, layoutRes, viewModel);
        return binding.getRoot();
    }

    private void bindVariable(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutRes, ItemViewModel item) {
        if (bindingVariable != ItemView.BINDING_VARIABLE_NONE) {
            boolean result = binding.setVariable(bindingVariable, item);
            if (!result) {
                throw new RuntimeException("data binding: can not find this variable");
            }
            binding.executePendingBindings();
        }
    }

}
