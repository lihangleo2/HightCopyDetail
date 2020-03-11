package com.lihang.scrollandmove.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lihang.nbadapter.BaseAdapter;
import com.lihang.scrollandmove.utils.BaseViewHolder;
import com.lihang.scrollandmove.R;
import com.lihang.scrollandmove.databinding.ItemCommentsBinding;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by leo
 * on 2019/12/5.
 */
public class CommentsAdapter extends BaseAdapter<String> {
    @Override
    public RecyclerView.ViewHolder getViewHolder(ViewGroup viewGroup, int viewType) {
        ItemCommentsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_comments, viewGroup, false);
        return new BaseViewHolder(binding);
    }

    @Override
    public void onBindMyViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }
}
