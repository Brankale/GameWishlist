package com.fermimn.gamewishlist.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.GameWishlistApplication;
import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.utils.Connectivity;
import com.fermimn.gamewishlist.viewmodels.SearchViewModel;

public class SearchFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = SearchFragment.class.getSimpleName();

    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private RecyclerView mSearchResults;
    private SearchViewModel mViewModel;

    // TODO: find a way to remove this variable
    private boolean mStartup = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get views
        mSearchView = view.findViewById(R.id.search_bar);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mSearchResults = view.findViewById(R.id.search_results);

        // set view model & observers
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);

        mViewModel.getSearchResults().observe(getActivity(), new Observer<GamePreviewList>() {
            @Override
            public void onChanged(GamePreviewList searchResults) {
                if (searchResults.isEmpty() && !mStartup) {
                    Toast.makeText(getActivity(), getString(R.string.no_games_found), Toast.LENGTH_SHORT).show();
                }
                mStartup = false;
                mSearchResults.getAdapter().notifyDataSetChanged();
                mSearchResults.scrollToPosition(0);
            }
        });

        mViewModel.isSearching().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSearching) {
                if (isSearching) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mSearchResults.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mSearchResults.setVisibility(View.VISIBLE);
                    // TODO: search view must lose the focus every time the user click
                    //       another part of the screen
                    mSearchResults.requestFocus();
                }
            }
        });

        // init search bar
        mSearchView.setOnQueryTextListener(queryTextListener);

        // init recycler view
        RecyclerView.Adapter adapter =
                new GamePreviewListAdapter(getActivity(), mViewModel.getSearchResults().getValue());
        mSearchResults.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mSearchResults.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mSearchResults.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchedGame) {

            // check if internet is available
            if (!Connectivity.isNetworkAvailable(getActivity())) {
                Toast.makeText(getActivity(), getString(R.string.toast_internet_not_available), Toast.LENGTH_SHORT).show();
                return false;
            }

            mViewModel.search(searchedGame);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    };

}
