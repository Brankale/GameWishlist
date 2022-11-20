package com.fermimn.gamewishlist;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.fermimn.gamewishlist.activities.GamePageActivity;
import com.fermimn.gamewishlist.models.GamePreview;
import com.fermimn.gamewishlist.services.UpdateWorker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class App extends Application {

    @SuppressWarnings("unused")
    private static final String TAG = App.class.getSimpleName();

    // notification channels
    private static final String CHANNEL_UPDATES_ID = "updates";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String theme = sharedPrefs.getString("themes",  "2");
        switch (theme) {
            case "white":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "auto":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest updateWorkRequest =
                new PeriodicWorkRequest.Builder(UpdateWorker.class, 3, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(updateWorkRequest);

        createNotificationChannel();
    }

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

    // TODO: revise passed parameters
    public static void sendOnUpdatesChannel(Context context, GamePreview game, String summary, String text) {

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
        // NB: Don't delete intent.setAction() because when there are multiple notifications
        // PendingIntent.FLAG_UPDATE_CURRENT overwrites the other intents. The result is that
        // every notification have the same intent.
        Intent intent = new Intent(context, GamePageActivity.class);
        intent.putExtra("gameID", game.getId());
        intent.setAction("gameID: " + game.getId());

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

    @SuppressWarnings("SuspiciousNameCombination")
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
                r = (float)(height / 2.0);
            }else{
                output = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                int top = (height - width)/2;
                int bottom = top + width;
                srcRect = new Rect(0, top, width, bottom);
                dstRect = new Rect(0, 0, width, width);
                r = (float)(width / 2.0);
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
