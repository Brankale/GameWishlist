package com.fermimn.gamewishlist.test;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

public class MyRecyclerView extends RecyclerView {

    private final RecyclerView.Adapter mAdapter;
    private final GamePreviewList mDataSet;

    public MyRecyclerView(@NonNull Context context) {
        super(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(context, layoutManager.getOrientation());
        addItemDecoration(dividerItemDecoration);

        mDataSet = new GamePreviewList();
        mAdapter = new GamePreviewListAdapter(context, mDataSet);
        setAdapter(mAdapter);
    }

    public void setDataSet(GamePreviewList dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        mAdapter.notifyDataSetChanged();
    }
}
