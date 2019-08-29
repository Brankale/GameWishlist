package com.fermimn.gamewishlist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.fermimn.gamewishlist.services.SearchForUpdatesJobService;
import com.fermimn.gamewishlist.utils.SettingsManager;

@SuppressWarnings("WeakerAccess")
public class GameWishlistApplication extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = GameWishlistApplication.class.getSimpleName();

    public static final String CHANNEL_ID = "Download status";

    // ID for Job Service
    public static final int SEARCH_FOR_UPDATES_JOB = 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        // set light or dark theme
        SettingsManager settings = SettingsManager.getInstance(this);
        AppCompatDelegate.setDefaultNightMode( settings.getDarkMode() );

        createNotificationChannel();
        scheduleJob();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_download);
            String description = getString(R.string.channel_download_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, SearchForUpdatesJobService.class);
        JobInfo info = new JobInfo.Builder(SEARCH_FOR_UPDATES_JOB, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(3*60*60*1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

}
