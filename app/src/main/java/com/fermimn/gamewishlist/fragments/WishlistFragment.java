package com.fermimn.gamewishlist.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel;

public class WishlistFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private GamePreviewListAdapter mAdapter;
    private WishlistViewModel mViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private GamePreviewList mWishlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        mWishlist = new GamePreviewList();

        FragmentActivity activity = getActivity();
        if (activity != null) {
            // attach ViewModel to the Fragment
            mViewModel = ViewModelProviders.of( getActivity() ).get(WishlistViewModel.class);

            // init wishlist
            GamePreviewList wishlist = mViewModel.getWishlist().getValue();
            if (wishlist != null) {
                mWishlist.addAll(wishlist);
            }
        }

        activity = getActivity();
        if (activity != null) {

            // update RecyclerView UI after changes
            mViewModel.getWishlist().observe(activity, new Observer<GamePreviewList>() {
                @Override
                public void onChanged(GamePreviewList wishlist) {
                    int prevElements = mWishlist.size();
                    mWishlist.clear();
                    mWishlist.addAll(wishlist);

                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getItemCount() - prevElements > 0) {
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }
            });

            // show Toast during RecyclerView state changes
            mViewModel.isUpdating().observe(activity, new Observer<Pair<GamePreview, Boolean>>() {
                @Override
                public void onChanged(Pair<GamePreview, Boolean> updatedGame) {
                    if (updatedGame.first != null) {
                        if (updatedGame.second) {
                            Toast.makeText(
                                    getActivity(),
                                    "Downloading: " + updatedGame.first.getTitle(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    getActivity(),
                                    "Aggiunto: " + updatedGame.first.getTitle(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
            });

        }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // TODO: there could be memory leaks
                new AsyncTask<GamePreviewList, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(GamePreviewList... gamePreviewLists) {
                        for (int i = 0; i < mWishlist.size(); ++i) {
                            mViewModel.updateGame( mWishlist.get(i).getId() );
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean refreshing) {
                        mSwipeRefreshLayout.setRefreshing(refreshing);
                    }
                }.execute();

//                new Thread() {
//                    public void run() {
//                        for (int i = 0; i < mWishlist.size(); ++i) {
//                            mViewModel.updateGame( mWishlist.get(i).getId() );
//                        }
//
//                    }
//                }.start();
//
//                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView = view.findViewById(R.id.wishlist);

        // init RecyclerView layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        mRecyclerView.setLayoutManager(layoutManager);

        // add divider to RecyclerView
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // add Adapter to RecyclerView
        mAdapter = new GamePreviewListAdapter(getActivity(), mWishlist);
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

//        ItemTouchHelper itemTouchHelper =
//                new ItemTouchHelper(new SwipeToDeleteCallback(getActivity(), mAdapter, wishListViewModel));
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

}
