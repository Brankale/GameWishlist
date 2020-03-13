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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fermimn.gamewishlist.App;
import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel;

@Deprecated
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
            mViewModel = new ViewModelProvider(getActivity()).get(WishlistViewModel.class);

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
                            Game prev = (Game) mWishlist.get(i);
                            mViewModel.updateGame( mWishlist.get(i).getId() );
                            Game current = (Game) mWishlist.get(i);

                            if (current != null) {

                                int priceChanges = 0;
                                StringBuilder text = new StringBuilder();

                                // the game has been released
                                if (current.getNewPrice() != null && prev.getPreorderPrice() != null) {
                                    text.append( getString(R.string.notif_game_released) );
                                }

                                // lower new price
                                if (current.getNewPrice() != null && prev.getNewPrice() != null) {
                                    if (current.getNewPrice() < prev.getNewPrice()) {
                                        text.append( getString(R.string.notif_lower_new_price) );
                                        priceChanges++;
                                    }
                                }

                                // lower used price
                                if (current.getUsedPrice() != null && prev.getUsedPrice() != null) {
                                    if (current.getUsedPrice() < prev.getUsedPrice()) {
                                        text.append( getString(R.string.notif_lower_used_price) );
                                        priceChanges++;
                                    }
                                }

                                // lower digital price
                                if (current.getDigitalPrice() != null && prev.getDigitalPrice() != null) {
                                    if (current.getDigitalPrice() < prev.getDigitalPrice()) {
                                        text.append( getString(R.string.notif_lower_digital_price) );
                                        priceChanges++;
                                    }
                                }

                                // lower preorder price
                                if (current.getPreorderPrice() != null && prev.getPreorderPrice() != null) {
                                    if (current.getPreorderPrice() < prev.getPreorderPrice()) {
                                        text.append( getString(R.string.notif_lower_preorder_price) );
                                        priceChanges++;
                                    }
                                }

                                if (text.length() != 0) {
                                    text.deleteCharAt(text.length() - 1);

                                    if (priceChanges == 1) {
                                        App.sendOnUpdatesChannel(getContext(), current, text.toString(), null);
                                    } else {
                                        App.sendOnUpdatesChannel(getContext(), current, getString(R.string.notif_lower_prices), text.toString());
                                    }
                                }
                            }
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean refreshing) {
                        mSwipeRefreshLayout.setRefreshing(refreshing);
                    }
                }.execute();
            }
        });

        mRecyclerView = view.findViewById(R.id.wishlist);

        // all elements in the recyclerview have the same size
        mRecyclerView.setHasFixedSize(true);

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
