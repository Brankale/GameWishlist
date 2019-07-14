package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.Game;

public class GamePageFragment extends Fragment {

    private static final String TAG = GamePageFragment.class.getSimpleName();

    private Context mContext;
    private Game mGame;

    public GamePageFragment(Game game) {
        mGame = game;
    }

    /**
     * This method is called by Android in the lifecycle of the Fragment.
     * The context must be init here and not somewhere else to avoid crashes.
     * @param context app context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_page, container, false);



        return view;
    }


}
