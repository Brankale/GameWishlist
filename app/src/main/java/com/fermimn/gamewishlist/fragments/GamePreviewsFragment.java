package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewsAdapter;
import com.fermimn.gamewishlist.data_types.GamePreviews;

public class GamePreviewsFragment extends Fragment {

    private static final String TAG = GamePreviewsFragment.class.getSimpleName();

    private Context mContext;
    private GamePreviews mGamePreviews;

    public GamePreviewsFragment(Context context, GamePreviews gamePreviews) {
        mContext = context;
        mGamePreviews = gamePreviews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_previews, container, false);
        ListView listView = view.findViewById(R.id.game_list);

        GamePreviewsAdapter adapter = new GamePreviewsAdapter(mContext, mGamePreviews);
        listView.setAdapter(adapter);

        return view;
    }

}
