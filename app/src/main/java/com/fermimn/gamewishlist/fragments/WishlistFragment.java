package com.fermimn.gamewishlist.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.viewmodels.WishListViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class WishlistFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistFragment.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private int mCount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        mRecyclerView = view.findViewById(R.id.wishlist);

        // View Model
        WishListViewModel wishListViewModel = ViewModelProviders.of(getActivity()).get(WishListViewModel.class);

        wishListViewModel.init();

        wishListViewModel.getWishlist().observe(getActivity(), new Observer<GamePreviewList>() {
            @Override
            public void onChanged(GamePreviewList gamePreviewList) {

                boolean scroll = false;
                Log.d(TAG, "UPDATE: " + gamePreviewList.size() + "   BEFORE: "+ mCount );
                if (gamePreviewList.size() > mCount) {
                    scroll = true;
                }

                mCount = gamePreviewList.size();
                Log.d(TAG, "Updating wishlist ...");
                mAdapter.notifyDataSetChanged();

                if (scroll) {
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                }
            }
        });


        wishListViewModel.isUpdating().observe(getActivity(), new Observer<Pair<GamePreview, Boolean>>() {
            @Override
            public void onChanged(Pair<GamePreview, Boolean> isUpdating) {
                GamePreview gamePreview = isUpdating.first;
                boolean isDownloading = isUpdating.second;

                if (gamePreview == null) {
                    return;
                }

                // TODO: notification can't be updated if the user close the app
                // DOCS: https://stackoverflow.com/questions/16651009/android-service-stops-when-app-is-closed
                // DOCS: https://developer.android.com/reference/android/app/Service.html
                String CHANNEL_ID = GameWishlistApplication.CHANNEL_ID;
                NotificationCompat.Builder builder;

                if (isDownloading) {
                    builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(gamePreview.getTitle())
                            .setContentText("Downloading Game...")
                            .setProgress(0, 0, true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                } else {
                    builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(gamePreview.getTitle())
                            .setContentText("Download completed")
                            .setProgress(0, 0, false)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                }

                NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from( getActivity() );

                // notificationId is a unique int for each notification that you must define
                int notificationId = gamePreview.hashCode();
                notificationManager.notify(notificationId, builder.build());
            }
        });

        // set RecyclerView adapter - layout manager - divider
        mAdapter = new GamePreviewListAdapter(getActivity(), wishListViewModel.getWishlist().getValue());
        mCount = wishListViewModel.getWishlist().getValue().size();
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}
