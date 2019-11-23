package com.fermimn.gamewishlist.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.fermimn.gamewishlist.App;
import com.fermimn.gamewishlist.R;
import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.models.GamePreviewList;
import com.fermimn.gamewishlist.repositories.Repository;

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

                        if (mJobCancelled) {
                            return;
                        }

                        Log.d(TAG, "Updating: " + wishlist.get(i).getTitle());
                        Game prev = (Game) wishlist.get(i);
                        Game current = repository.updateGame(wishlist.get(i).getId());

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
                                    App.sendOnUpdatesChannel(getApplication(), current, text.toString(), null);
                                } else {
                                    App.sendOnUpdatesChannel(getApplication(), current, getString(R.string.notif_lower_prices), text.toString());
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