package com.fermimn.gamewishlist.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapterNew;
import com.fermimn.gamewishlist.data_types.GamePreviewList;
import com.fermimn.gamewishlist.utils.Connectivity;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

import java.lang.ref.WeakReference;

public class SearchGamesFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = SearchGamesFragment.class.getSimpleName();

    private Context mContext;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private GamePreviewList mGamePreviewList;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_games, container, false);

        // get views
        mSearchView = view.findViewById(R.id.search_bar);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = view.findViewById(R.id.search_results);

        // set SearchView listener
        mSearchView.setOnQueryTextListener(queryTextListener);

        // set RecyclerView adapter - layout manager - divider
        mGamePreviewList = new GamePreviewList();
        RecyclerView.Adapter adapter = new GamePreviewListAdapterNew(mContext, mGamePreviewList);
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mContext, layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchedGame) {

            // check if internet is available
            if (!Connectivity.isNetworkAvailable(mContext)) {
                Toast.makeText(mContext, getString(R.string.toast_internet_not_available), Toast.LENGTH_SHORT).show();
                return false;
            }

            // start online research
            new Search(mContext, mProgressBar, mRecyclerView).execute(searchedGame);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

    private static class Search extends AsyncTask<String, Integer, GamePreviewList> {

        // DOCS: https://medium.com/google-developer-experts/finally-understanding-how-references-work-in-android-and-java-26a0d9c92f83
        private WeakReference<Context> mContext;
        private WeakReference<ProgressBar> mProgressBar;
        private WeakReference<RecyclerView> mRecyclerView;

        Search(Context context, ProgressBar progressBar, RecyclerView recyclerView) {
            mContext = new WeakReference<>(context);
            mProgressBar = new WeakReference<>(progressBar);
            mRecyclerView = new WeakReference<>(recyclerView);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RecyclerView recyclerView = mRecyclerView.get();
            if (recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
            }

            ProgressBar progressBar = mProgressBar.get();
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected GamePreviewList doInBackground(String... strings) {
            Store store = new Gamestop();
            return store.searchGame(strings[0]);
        }

        @Override
        protected void onPostExecute(GamePreviewList searchResults) {
            super.onPostExecute(searchResults);

            Context context = mContext.get();
            ProgressBar progressBar = mProgressBar.get();
            RecyclerView recyclerView = mRecyclerView.get();

            if (context == null) {
                return;
            }

            if (searchResults == null) {
                Toast.makeText(context, "Nessun gioco trovato", Toast.LENGTH_SHORT).show();
                searchResults = new GamePreviewList();
            }

            if (recyclerView != null && recyclerView.getAdapter() != null) {
                GamePreviewListAdapterNew testAdapter = (GamePreviewListAdapterNew) recyclerView.getAdapter();
                if (testAdapter != null) {
                    testAdapter.setDataset(searchResults);
                }
            }

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            if (recyclerView != null) {
                recyclerView.setVisibility(View.VISIBLE);
                // TODO: focus is not handle perfectly
                recyclerView.requestFocus();
            }
        }
    }

}
