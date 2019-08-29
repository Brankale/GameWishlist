package com.fermimn.gamewishlist.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.WishListRepository;
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
        new Thread() {
            @Override
            public void run() {

                // TODO: in this way it doesn't update the UI if the app is opened
                WishListRepository repository = WishListRepository.getInstance(getApplication());
                GamePreviewList gamePreviewList = repository.getWishList().getValue();

                Store store = new Gamestop();

                if (gamePreviewList != null) {
                    for (GamePreview gamePreview : gamePreviewList) {
                        Log.d(TAG, "Updating: " + gamePreview.getTitle());

                        if (mJobCancelled) {
                            return;
                        }

                        Game outDatedGame = (Game) gamePreview;
                        Game game = store.downloadGame( gamePreview.getId() );

                        // check if there are some updates

                        // TODO: display a notification if the has some updates

                        // TODO: remove hardcoded text
                        // TODO: check if the prices are lower
                        // TODO: check other things like release date
                        // TODO: preorder price can become a new price if the game is release
                        // TODO: save the file on teh local DRIVE
                        if (!game.getNewPrice().equals( outDatedGame.getNewPrice() )) {
                            Log.d(TAG, "il prezzo nuovo è cambiato");
                        }

                        if (!game.getUsedPrice().equals( outDatedGame.getUsedPrice() )) {
                            Log.d(TAG, "il prezzo usato è cambiato");
                        }

                        if (!game.getDigitalPrice().equals( outDatedGame.getDigitalPrice() )) {
                            Log.d(TAG, "il prezzo digitale è cambiato");
                        }

                        if (!game.getPreorderPrice().equals( outDatedGame.getPreorderPrice() )) {
                            Log.d(TAG, "il prezzo preordine è cambiato");
                        }

                    }
                }

                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }.start();
    }

}
