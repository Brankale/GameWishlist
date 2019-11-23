package com.fermimn.gamewishlist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fermimn.gamewishlist.services.SearchForUpdatesJobService;
import com.fermimn.gamewishlist.utils.SettingsManager;

public class App extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = App.class.getSimpleName();

    // notification channels
    private static final String CHANNEL_UPDATES_ID = "updates";

    // ID for Job Service
    private static final int SEARCH_FOR_UPDATES_JOB = 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        // set light or dark theme
        SettingsManager settings = SettingsManager.getInstance(this);
        AppCompatDelegate.setDefaultNightMode( settings.getDarkMode() );

        createNotificationChannel();
        scheduleJob();
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, SearchForUpdatesJobService.class);
        JobInfo info = new JobInfo.Builder(SEARCH_FOR_UPDATES_JOB, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(3*60*60*1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (scheduler != null) {
            int resultCode = scheduler.schedule(info);

            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
            } else {
                Log.d(TAG, "Job scheduling failed");
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_updates);
            String description = getString(R.string.channel_updates_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_UPDATES_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void sendOnUpdatesChannel(Context context, int id, String title, String text, Bitmap image) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_UPDATES_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(image)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }

}
