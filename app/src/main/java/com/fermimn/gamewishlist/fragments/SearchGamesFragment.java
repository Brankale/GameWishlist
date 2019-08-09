package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.fermimn.gamewishlist.R;
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
    private FrameLayout mSearchResults;
    private SearchGamesFragment mFragment;
    private View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: check what is savedInstanceState
        mFragment = this;

        mView = inflater.inflate(R.layout.fragment_search_games, container, false);
        SearchView searchView = mView.findViewById(R.id.search_bar);
        mProgressBar = mView.findViewById(R.id.indeterminateBar);
        mSearchResults = mView.findViewById(R.id.search_results);

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
                mRunningTask = new Search(mContext, mFragment, mView);
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
        private final WeakReference<SearchGamesFragment> mSearchGamesFragment;
        private final WeakReference<View> mSearchGamesFragmentView;

        // TODO: find a best way to pass parameters
        private Search(Context context, SearchGamesFragment fragment, View rootView) {
            mContext = new WeakReference<>(context);
            mSearchGamesFragment = new WeakReference<>(fragment);
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
            SearchGamesFragment fragment = mSearchGamesFragment.get();
            View view = mSearchGamesFragmentView.get();

            ProgressBar progressBar = view.findViewById(R.id.indeterminateBar);
            FrameLayout searchResults = view.findViewById(R.id.search_results);


            if (gamePreviewList != null) {

                // add the fragment
                GamePreviewListFragment gamePreviewListFragment =
                        new GamePreviewListFragment(gamePreviewList);

                // set ListView padding in dp
                //gamePreviewListFragment.setPadding(56,87);

                FragmentTransaction transaction = fragment.getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.search_results, gamePreviewListFragment, "game_list");
                transaction.commit();

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
