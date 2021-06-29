package com.swapnil.helloworld.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swapnil.helloworld.R;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FCMReceiver extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "Notification_channel";
    Random random = new Random();

    /*
    * This is automatically called when notification is being received
    * */
    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannel();
        if (remoteMessage.getData().get("for") != null) {
            if (remoteMessage.getData().get("for").equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                showNotification(remoteMessage);
            }
        }
    }
    /*
    * Method to show notification when received
    * */
    private void showNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentText(remoteMessage.getData().get("body"))
                .setColor(ContextCompat.getColor(this,R.color.notification_red));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(random.nextInt() + 1000, builder.build());
    }

    /*
    * Method to create notification channel
    * */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
