package com.fermimn.gamewishlist.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.fermimn.gamewishlist.viewmodels.WishlistViewModel;

// TODO: rewrite completely this class

public class WishlistFragment extends Fragment {

    @SuppressWarnings("unused")
    private static final String TAG = WishlistFragment.class.getSimpleName();

    private GamePreviewListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mCount;
    private GamePreviewList mWishlist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        mRecyclerView = view.findViewById(R.id.wishlist);

        // View Model
        WishlistViewModel wishListViewModel = ViewModelProviders.of(getActivity()).get(WishlistViewModel.class);
        //wishListViewModel.init();

        wishListViewModel.getWishlist().observe(getActivity(), new Observer<GamePreviewList>() {
            @Override
            public void onChanged(GamePreviewList gamePreviewList) {

                Log.d(TAG, "Observer triggered");

                if (gamePreviewList == null) {
                    Log.d(TAG, "Observer: wishlist null");
                    return;
                }

                mWishlist.clear();
                mWishlist.addAll(gamePreviewList);

                boolean scroll = false;
                Log.d(TAG, "UPDATE: " + gamePreviewList.size() + "   BEFORE: "+ mCount );
                if (gamePreviewList.size() > mCount) {
                    scroll = true;
                }

                mCount = gamePreviewList.size();
                Log.d(TAG, "Updating wishlist ...");
                mAdapter.notifyDataSetChanged();

                if (scroll) {
                    Log.d(TAG, "scroll to position: " + (mCount-1));
                    mRecyclerView.smoothScrollToPosition(mCount-1);
                }

                Log.d(TAG, "Observer: adapter updated");
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

                if (isDownloading) {
                    Toast.makeText(getActivity(), "Downloading: " + gamePreview.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: if the user enters in another activity it can't see this message
                    Toast.makeText(getActivity(), "Added: " + gamePreview.getTitle(), Toast.LENGTH_SHORT).show();
                }

                // TODO: this code can be used somewhere in the app, so DON'T REMOVE IT
                // TODO: notification can't be updated if the user close the app
                // DOCS: https://stackoverflow.com/questions/16651009/android-service-stops-when-app-is-closed
                // DOCS: https://developer.android.com/reference/android/app/Service.html
//                String CHANNEL_ID = GameWishlistApplication.CHANNEL_ID;
//                NotificationCompat.Builder builder;
//
//                if (isDownloading) {
//                    builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
//                            .setSmallIcon(R.drawable.ic_notification)
//                            .setContentTitle(gamePreview.getTitle())
//                            .setContentText("Downloading Game...")
//                            .setProgress(0, 0, true)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                } else {
//                    builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
//                            .setSmallIcon(R.drawable.ic_notification)
//                            .setContentTitle(gamePreview.getTitle())
//                            .setContentText("Download completed")
//                            .setProgress(0, 0, false)
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                }
//
//                NotificationManagerCompat notificationManager =
//                        NotificationManagerCompat.from( getActivity() );
//
//                // notificationId is a unique int for each notification that you must define
//                int notificationId = gamePreview.hashCode();
//                notificationManager.notify(notificationId, builder.build());
            }
        });

        // set RecyclerView adapter - layout manager - divider
        mWishlist = new GamePreviewList();

        GamePreviewList gamePreviewList = wishListViewModel.getWishlist().getValue();
        if (gamePreviewList != null) {
            mWishlist.addAll(gamePreviewList);
        }

        mAdapter = new GamePreviewListAdapter(getActivity(), mWishlist);
        mAdapter.setHasStableIds(true);
        if (gamePreviewList != null ) {
            mCount = gamePreviewList.size();
        } else {
            Log.d(TAG, "Wishlist vuota");
            mCount = 0;
        }
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        mRecyclerView.setLayoutManager(layoutManager);

//        ItemTouchHelper itemTouchHelper =
//                new ItemTouchHelper(new SwipeToDeleteCallback(getActivity(), mAdapter, wishListViewModel));
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}
