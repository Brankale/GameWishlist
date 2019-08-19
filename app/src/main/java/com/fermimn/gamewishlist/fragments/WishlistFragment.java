package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.WishlistManager;

public class WishlistFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistFragment.class.getSimpleName();

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wishlist);

        // get wishlist
        WishlistManager wishlistManager = WishlistManager.getInstance(mContext);
        GamePreviewList gamePreviewList = wishlistManager.getWishlist();

        // set RecyclerView adapter - layout manager - divider
        RecyclerView.Adapter adapter = new GamePreviewListAdapter(mContext, gamePreviewList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mContext, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}
