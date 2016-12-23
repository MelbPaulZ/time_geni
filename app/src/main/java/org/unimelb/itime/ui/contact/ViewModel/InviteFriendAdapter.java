package org.unimelb.itime.ui.contact.ViewModel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.android.databinding.library.baseAdapters.BR;
import org.unimelb.itime.R;

import java.util.List;

/**
 * Created by 37925 on 2016/12/19.
 */

public class InviteFriendAdapter extends BaseAdapter {
    private Context context;
    private List list;
    public InviteFriendAdapter(Context context){
        this.context = context;
    }

    public void setList(List list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding binding = null;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.invitee_friend_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewDataBinding) convertView.getTag();
        }
        binding.setVariable(BR.viewModel, getItem(position));
        return binding.getRoot();
    }
}
