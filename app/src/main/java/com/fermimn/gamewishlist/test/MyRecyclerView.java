package com.fermimn.gamewishlist.test;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(@NonNull Context context) {
        super(context);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(context, layoutManager.getOrientation());
        addItemDecoration(dividerItemDecoration);

        RecyclerView.Adapter adapter = new GamePreviewListAdapter(context, new GamePreviewList());
        setAdapter(adapter);
    }
}
