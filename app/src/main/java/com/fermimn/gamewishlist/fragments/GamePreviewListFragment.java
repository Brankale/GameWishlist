package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.activities.MainActivity;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.Game;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.Gamestop;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // get clicked gamePreview
                GamePreview gamePreview = (GamePreview) parent.getItemAtPosition(position);

                // download the game and shows the result
                DownloadingGame task = new DownloadingGame();
                task.execute( gamePreview.getId() );
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

    // TODO: add documentation
    private class DownloadingGame extends AsyncTask<String, Integer, Game> {

        @Override
        protected Game doInBackground(String... strings) {
            String id = strings[0];
            Gamestop gamestop = new Gamestop();
            return gamestop.downloadGame(id);
        }

        @Override
        protected void onPostExecute(Game game) {
            showGamePage(game);
        }

    }

    // TODO: add documentation
    private void showGamePage(Game game) {
        GamePageFragment gamePageFragment = new GamePageFragment(game);

        // TODO: casting getActivity() to MainActivity is awful
        ((MainActivity) getActivity()).showGamePageFragment(gamePageFragment);
    }

}
