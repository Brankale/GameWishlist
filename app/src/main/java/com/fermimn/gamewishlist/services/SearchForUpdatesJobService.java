package com.fermimn.gamewishlist.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fermimn.gamewishlist.GameWishlistApplication;
import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.Repository;
import com.fermimn.gamewishlist.utils.Gamestop;
import com.fermimn.gamewishlist.utils.Store;

public class SearchForUpdatesJobService extends JobService {

    @SuppressWarnings("unused")
    private static final String TAG = SearchForUpdatesJobService.class.getSimpleName();

    private boolean mJobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job started");
        searchForUpdates(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job cancelled before completion");
        mJobCancelled = true;
        return false;
    }

    private void searchForUpdates(final JobParameters params) {

        final Repository repository = Repository.getInstance(getApplication());
        final GamePreviewList wishlist = repository.getWishlist().getValue();

        if (wishlist != null) {
            new Thread() {
                public void run() {
                    for (int i = 0; i < wishlist.size(); ++i) {
                        Game game = repository.updateGame(wishlist.get(i).getId());
                        if (game != null) {
                            if (game.getNewPrice() != null) {
                                if (!game.getNewPrice().equals(wishlist.get(i).getNewPrice())) {
                                    Log.d(TAG, "il prezzo nuovo è cambiato");
                                }
                            }

                            if (game.getUsedPrice() != null) {
                                if (!game.getUsedPrice().equals(wishlist.get(i).getUsedPrice())) {
                                    Log.d(TAG, "il prezzo usato è cambiato");
                                }
                            }

                            if (game.getDigitalPrice() != null) {
                                if (!game.getDigitalPrice().equals(wishlist.get(i).getDigitalPrice())) {
                                    Log.d(TAG, "il prezzo digitale è cambiato");
                                }
                            }

                            if (game.getPreorderPrice() != null) {
                                if (!game.getPreorderPrice().equals(wishlist.get(i).getPreorderPrice())) {
                                    Log.d(TAG, "il prezzo preordine è cambiato");
                                }
                            }
                        }
                    }

                    Log.d(TAG, "Job finished");
                    jobFinished(params, false);

                }
            }.start();
        }
    }

}


//        // TODO: start method has been removed because the thread can cause crashes
//        new Thread() {
//            @Override
//            public void run() {
//
//                // TODO: in this way it doesn't update the UI if the app is opened
//                // TODO: in the MVVM pattern you shouldn't call the repository but the ViewModel
//                Repository repository = Repository.getInstance(getApplication());
//                GamePreviewList gamePreviewList = repository.getWishlist().getValue();
//
//                Store store = new Gamestop();
//
//                if (gamePreviewList != null) {
//                    for (GamePreview gamePreview : gamePreviewList) {
//                        Log.d(TAG, "Updating: " + gamePreview.getTitle());
//
//                        if (mJobCancelled) {
//                            return;
//                        }
//
//                        Game outDatedGame = (Game) gamePreview;
//                        Game game = store.downloadGame( gamePreview.getId() );
//
//                        // check if there are some updates
//
//                        // TODO: display a notification if the has some updates
//
//                        // TODO: remove hardcoded text
//                        // TODO: check if the prices are lower
//                        // TODO: check other things like release date
//                        // TODO: preorder price can become a new price if the game is release
//                        // TODO: save the file on the local DRIVE
//                        if (!game.getNewPrice().equals( outDatedGame.getNewPrice() )) {
//                            Log.d(TAG, "il prezzo nuovo è cambiato");
//                        }
//
//                        if (!game.getUsedPrice().equals( outDatedGame.getUsedPrice() )) {
//                            Log.d(TAG, "il prezzo usato è cambiato");
//                        }
//
//                        if (!game.getDigitalPrice().equals( outDatedGame.getDigitalPrice() )) {
//                            Log.d(TAG, "il prezzo digitale è cambiato");
//                        }
//
//                        if (!game.getPreorderPrice().equals( outDatedGame.getPreorderPrice() )) {
//                            Log.d(TAG, "il prezzo preordine è cambiato");
//                        }
//
//                    }
//                }
//
//                Log.d(TAG, "Job finished");
//                jobFinished(params, false);
//            }
//        };
//    }
//
//}


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
