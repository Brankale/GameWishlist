package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.data_types.GamePreview;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.Connectivity;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class SearchGamesFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = SearchGamesFragment.class.getSimpleName();

    private Context mContext;

    private ProgressBar mProgressBar;
    private ListView mSearchResults;
    private View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search_games, container, false);
        SearchView searchView = mView.findViewById(R.id.search_bar);
        mProgressBar = mView.findViewById(R.id.indeterminateBar);
        mSearchResults = mView.findViewById(R.id.game_list);

        // Set listeners of the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            private Search mRunningTask;

            @Override
            public boolean onQueryTextSubmit(String query) {

                // check if internet is available
                if (!Connectivity.isNetworkAvailable(mContext)) {
                    Toast.makeText(mContext, "Non sei connesso a Internet", Toast.LENGTH_SHORT).show();
                    return false;
                }

                // stop the old task if it exists
                if (mRunningTask != null) {
                    // TODO: implement isCancel() in Search private class
                    mRunningTask.cancel(true);
                    mProgressBar.setVisibility(View.GONE);
                }

                // search the game (Search is a private class)
                // TODO: find a best way to pass parameters
                mRunningTask = new Search(mContext, mView);
                mRunningTask.execute(query);

                // progress bar appears
                mSearchResults.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // stop the old task if it exists
                if (mRunningTask != null) {
                    // TODO: implement isCancel() in Search private class
                    mRunningTask.cancel(true);
                    mProgressBar.setVisibility(View.GONE);
                }

                return false;
            }

        });

        return mView;
    }

    /**
     * Search is a private class used to search the game the user writes in the search bar
     */
    private static class Search extends AsyncTask<String, Integer, GamePreviewList> {

        private final WeakReference<Context> mContext;
        private final WeakReference<View> mSearchGamesFragmentView;

        // TODO: find a best way to pass parameters
        private Search(Context context, View rootView) {
            mContext = new WeakReference<>(context);
            mSearchGamesFragmentView = new WeakReference<>(rootView);
        }

        // TODO: implement isCancel()
        @Override
        protected GamePreviewList doInBackground(String... strings) {

            GamePreviewList searchResults = null;
            String gameSearched = strings[0];

            // retrieve info
            try {
                Log.d(TAG, "Ricerca avviata");
                Store store = new Gamestop();
                searchResults = store.searchGame(gameSearched);
                Log.d(TAG, "Ricerca conclusa");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(GamePreviewList searchResults) {
            onEndSearch(searchResults);
        }

        /**
         * This method is called by the private class "Search" during the onPostExecute().
         * It shows the results on the screen.
         * @param gamePreviewList list of games of the searchResults
         */
        private void onEndSearch(GamePreviewList gamePreviewList) {

            Context context = mContext.get();
            View view = mSearchGamesFragmentView.get();

            ProgressBar progressBar = view.findViewById(R.id.indeterminateBar);
            ListView searchResults = view.findViewById(R.id.game_list);


            if (gamePreviewList != null) {

                GamePreviewListAdapter adapter = new GamePreviewListAdapter(context, gamePreviewList);
                searchResults.setAdapter(adapter);

                searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Context context = mContext.get();

                        GamePreview gamePreview = (GamePreview) parent.getItemAtPosition(position);
                        Intent intent = new Intent(context, GamePageActivity.class);
                        intent.putExtra("gameID", gamePreview.getId());
                        context.startActivity(intent);
                    }

                });

                // make the fragment visible
                progressBar.setVisibility(View.GONE);
                searchResults.setVisibility(View.VISIBLE);

                // TODO: focus is not handle perfectly
                searchResults.requestFocus();

            } else {
                // progress bar disappears
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Nessun gioco trovato", Toast.LENGTH_SHORT).show();
            }
        }

    }

}