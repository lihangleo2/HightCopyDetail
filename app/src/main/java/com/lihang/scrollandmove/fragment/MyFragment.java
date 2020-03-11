package com.lihang.scrollandmove.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lihang.scrollandmove.adapter.MessageAdapter;
import com.lihang.scrollandmove.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by leo
 * on 2020/3/11.
 */
public class MyFragment extends androidx.fragment.app.Fragment {

    private MessageAdapter adapter;
    private ArrayList<Integer> images = new ArrayList<>();
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,null);
        images.add(R.mipmap.pic_2);
        images.add(R.mipmap.pic_3);
        images.add(R.mipmap.pic_4);
        images.add(R.mipmap.pic_1);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MessageAdapter();
        adapter.setDataList(images);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
