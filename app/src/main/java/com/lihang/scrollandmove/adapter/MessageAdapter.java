package com.lihang.scrollandmove.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lihang.nbadapter.BaseAdapter;
import com.lihang.scrollandmove.utils.BaseViewHolder;
import com.lihang.scrollandmove.R;
import com.lihang.scrollandmove.databinding.ItemMessageBinding;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by leo
 * on 2019/11/8.
 */
public class MessageAdapter extends BaseAdapter<Integer> {


    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int viewType) {

        ItemMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_message, viewGroup, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
        if (baseViewHolder.binding instanceof ItemMessageBinding) {
            ItemMessageBinding binding = (ItemMessageBinding) baseViewHolder.binding;
            Integer itemBean = dataList.get(i);
            binding.imageMessage.setImageResource(itemBean);

        }

    }
}
