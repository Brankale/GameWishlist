package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    private GamePreviewList mGamePreviewList;

    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;

    public GamePreviewListFragment(Context context, GamePreviewList gamePreviewList) {
        mContext = context;
        mGamePreviewList = gamePreviewList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_preview_list, container, false);
        ListView listView = view.findViewById(R.id.game_list);

        listView.setPadding(0, mPaddingTop, 0 , mPaddingBottom);

        GamePreviewListAdapter adapter = new GamePreviewListAdapter(mContext, mGamePreviewList);
        listView.setAdapter(adapter);

        return view;
    }

    /**
     * Set padding top and padding bottom of the ListView.
     * The unite of measure is dp.
     * @param top padding top
     * @param bottom padding bottom
     */
    public void setPadding(int top, int bottom) {

        // TODO: the emulator doesn't want to collaborate
        //       implement this method when the emulator become usable
//        float scale = getResources().getDisplayMetrics().density;
//        mPaddingBottom = (int) (bottom * scale + 0.5f);
//        mPaddingTop = (int) (top * scale + 0.5f);
    }

}
