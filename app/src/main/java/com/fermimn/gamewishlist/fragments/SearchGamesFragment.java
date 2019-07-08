package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.data_types.GamePreviews;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

import java.io.IOException;

public class SearchGamesFragment extends Fragment {

    private static final String TAG = SearchGamesFragment.class.getSimpleName();

    private Context mContext;

    public SearchGamesFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_games, container, false);
        SearchView searchView = view.findViewById(R.id.search_box);

        // Set the listeners of the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // search the game (Search is a private class)
                new Search().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        return view;
    }

    /**
     * This method is called by the private class "Search" during the onPostExecute()
     * It shows the results on the screen
     * @param gamePreviews list of games of the searchResults
     */
    public void showSearchResults(GamePreviews gamePreviews) {
        if (gamePreviews != null) {
            GamePreviewsFragment gamePreviewsFragment = new GamePreviewsFragment(mContext, gamePreviews);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.search_results, gamePreviewsFragment, "search_results");
            transaction.commit();
        } else {
            Toast.makeText(mContext, "Nessun gioco trovato", Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: add documentation
    private class Search extends AsyncTask<String, Integer, GamePreviews> {

        @Override
        protected GamePreviews doInBackground(String... strings) {

            Log.d(TAG, "Ricerca avviata");

            String gameSearched = strings[0];
            GamePreviews searchResults = null;

            // TODO: try/catch need revision
            // TODO: check if internet is available
            // TODO: crash if internet is not available
            try {
                Store store = new Gamestop();
                searchResults = store.searchGame(gameSearched);
            } catch (IOException e) {
                Toast.makeText(mContext, "Qualcosa è andato storto", Toast.LENGTH_SHORT).show();
            }

            return searchResults;
        }

        @Override
        protected void onPostExecute(GamePreviews searchResults) {
            Log.d(TAG, "Ricerca conclusa");
            showSearchResults(searchResults);
        }

    }

}
