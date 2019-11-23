package com.fermimn.gamewishlist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.models.Game;
import com.fermimn.gamewishlist.services.SearchForUpdatesJobService;
import com.fermimn.gamewishlist.utils.SettingsManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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

    public static void sendOnUpdatesChannel(Context context, Game game, String summary, String text) {

        // Get info
        int id = game.hashCode();
        String title = game.getTitle();
        Bitmap bitmap = null;

        try {
            bitmap = Picasso.get().load(game.getCover()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an Intent for the GamePageActivity
        Intent intent = new Intent(context, GamePageActivity.class);
        intent.putExtra("gameID", game.getId());

        // Inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_UPDATES_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(getCircleBitmap(bitmap))
                        .setContentTitle(title)
                        .setContentText(summary)
                        .setStyle( new NotificationCompat.BigTextStyle().bigText(text) )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }

    private static Bitmap getCircleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap output;
            Rect srcRect, dstRect;
            float r;
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            if (width > height){
                output = Bitmap.createBitmap(height, height, Bitmap.Config.ARGB_8888);
                int left = (width - height) / 2;
                int right = left + height;
                srcRect = new Rect(left, 0, right, height);
                dstRect = new Rect(0, 0, height, height);
                r = height / 2;
            }else{
                output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                int top = (height - width)/2;
                int bottom = top + width;
                srcRect = new Rect(0, top, width, bottom);
                dstRect = new Rect(0, 0, width, width);
                r = width / 2;
            }

            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(r, r, r, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, srcRect, dstRect, paint);

            return output;
        }

        return null;
    }

}
