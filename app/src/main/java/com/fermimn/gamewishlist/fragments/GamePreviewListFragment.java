package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

public class GamePreviewListFragment extends Fragment {

    private static final String TAG = GamePreviewListFragment.class.getSimpleName();

    private Context mContext;
    private GamePreviewList mGamePreviews;

    public GamePreviewListFragment(Context context, GamePreviewList gamePreviewList) {
        mContext = context;
        mGamePreviews = gamePreviewList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_preview_list, container, false);
        ListView listView = view.findViewById(R.id.game_list);

        GamePreviewListAdapter adapter = new GamePreviewListAdapter(mContext, mGamePreviews);
        listView.setAdapter(adapter);

        return view;
    }

}
