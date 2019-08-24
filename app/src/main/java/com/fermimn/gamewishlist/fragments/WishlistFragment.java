package com.fermimn.gamewishlist.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.adapters.GamePreviewListAdapter;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishListViewModel;
import com.fermimn.gamewishlist.repositories.WishListRepository;
import com.fermimn.gamewishlist.utils.WishlistManager;

public class WishlistFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistFragment.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.wishlist);

        // get wishlist
        WishlistManager wishlistManager = WishlistManager.getInstance(getActivity());
        WishListRepository repository = WishListRepository.getInstance();
        for (GamePreview gamePreview : wishlistManager.getWishlist()) {
            repository.add(gamePreview);
        }

        // View Model
        WishListViewModel wishListViewModel = ViewModelProviders.of(getActivity()).get(WishListViewModel.class);
        wishListViewModel.getWishlist().observe(getActivity(), new Observer<GamePreviewList>() {
            @Override
            public void onChanged(GamePreviewList gamePreviewList) {
                Log.d(TAG, "Updating wishlist ...");
                mAdapter.notifyDataSetChanged();
            }
        });

        // set RecyclerView adapter - layout manager - divider
        mAdapter = new GamePreviewListAdapter(getActivity(), wishListViewModel.getWishlist().getValue());
        recyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}
