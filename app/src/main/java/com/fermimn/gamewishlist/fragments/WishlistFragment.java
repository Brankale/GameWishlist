package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreview;
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

        View view = inflater.inflate(R.layout.partial_game_preview_list, container, false);
        ListView listView = view.findViewById(R.id.game_list);

        // get wishlist
        WishlistManager wishlistManager = WishlistManager.getInstance(mContext);
        GamePreviewList gamePreviewList = wishlistManager.getWishlist();

        // set adapter
        GamePreviewListAdapter adapter = new GamePreviewListAdapter(mContext, gamePreviewList);
        listView.setAdapter(adapter);

        // set listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GamePreview gamePreview = (GamePreview) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, GamePageActivity.class);
                intent.putExtra("gameID", gamePreview.getId());
                mContext.startActivity(intent);
            }

        });

        return view;
    }

}
