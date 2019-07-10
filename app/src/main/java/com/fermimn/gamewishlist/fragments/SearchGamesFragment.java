package com.fermimn.gamewishlist.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.Connectivity;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

import java.io.IOException;

public class SearchGamesFragment extends Fragment {

    private static final String TAG = SearchGamesFragment.class.getSimpleName();

    private Context mContext;
    private ProgressBar mProgressBar;
    private LinearLayout mSearchResults;

    public SearchGamesFragment(){
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

        View view = inflater.inflate(R.layout.fragment_search_games, container, false);
        SearchView searchView = view.findViewById(R.id.search_bar);
        mProgressBar = view.findViewById(R.id.indeterminateBar);
        mSearchResults = view.findViewById(R.id.search_results);

        // remove the grey line in the SearchView
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);
        if (searchPlate != null) {
            searchPlate.setBackgroundColor(Color.TRANSPARENT);
        }

        // Set listeners of the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            private AsyncTask mRunningTask;

            @Override
            public boolean onQueryTextSubmit(String query) {

                // check if internet is available
                Connectivity connectivity = Connectivity.getInstance();
                if (connectivity.isNetworkAvailable(mContext) == false) {
                    Toast.makeText(mContext, "Internet non disponibile", Toast.LENGTH_SHORT);
                    return false;
                }

                // stop the old task if it exists
                if (mRunningTask != null) {
                    mRunningTask.cancel(true);
                    mProgressBar.setVisibility(View.GONE);
                }

                // search the game (Search is a private class)
                mRunningTask = new Search().execute(query);

                // progress bar appears
                mSearchResults.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // stop the old task if it exists
                if (mRunningTask != null) {
                    mRunningTask.cancel(true);
                    mProgressBar.setVisibility(View.GONE);
                }

                return false;
            }

        });

        return view;
    }

    /**
     * This method is called by the private class "Search" during the onPostExecute().
     * It shows the results on the screen.
     * @param gamePreviews list of games of the searchResults
     */
    public void showSearchResults(GamePreviewList gamePreviews) {

        if (gamePreviews != null) {

            // add fragment
            GamePreviewListFragment gamePreviewListFragment =
                    new GamePreviewListFragment(mContext, gamePreviews);

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.search_results, gamePreviewListFragment, "game_list");
            transaction.commit();

            // progress bar disappears
            mProgressBar.setVisibility(View.GONE);
            mSearchResults.setVisibility(View.VISIBLE);

        } else {
            // progress bar disappears
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(mContext, "Nessun gioco trovato", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: add documentation
    private class Search extends AsyncTask<String, Integer, GamePreviewList> {

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
            showSearchResults(searchResults);
        }
    }

}
