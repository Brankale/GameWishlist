package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;

public class GamePreviewListFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = GamePreviewListFragment.class.getSimpleName();

    private Context mContext;

    private GamePreviewList mGamePreviewList;
    private int mPaddingTop = 0;
    private int mPaddingBottom = 0;

    public GamePreviewListFragment(GamePreviewList gamePreviewList) {
        mGamePreviewList = gamePreviewList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_preview_list, container, false);
        ListView listView = view.findViewById(R.id.game_list);

        listView.setPadding(0, mPaddingTop, 0 , mPaddingBottom);

        GamePreviewListAdapter adapter = new GamePreviewListAdapter(mContext, mGamePreviewList);
        listView.setAdapter(adapter);

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

    /**
     * Set padding top and padding bottom of the ListView.
     * The unite of measure is dp.
     * @param top padding top
     * @param bottom padding bottom
     */
    public void setPadding(int top, int bottom) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        mPaddingBottom = (int) (bottom * scale + 0.5f);
        mPaddingTop = (int) (top * scale + 0.5f);
    }

}
