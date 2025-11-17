package com.lunartag.app.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.lunartag.app.R;

import java.io.File;

public class SendService extends Service {

    private static final String TAG = "SendService";
    private static final String CHANNEL_ID = "SendServiceChannel";
    private static final int NOTIFICATION_ID = 101;

    public static final String EXTRA_FILE_PATH = "com.lunartag.app.EXTRA_FILE_PATH";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filePath = intent.getStringExtra(EXTRA_FILE_PATH);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Lunar Tag")
                .setContentText("Preparing to send scheduled photo...")
                .setSmallIcon(R.drawable.ic_camera) // A placeholder icon
                .build();

        startForeground(NOTIFICATION_ID, notification);

        if (filePath != null && !filePath.isEmpty()) {
            shareImageToWhatsApp(filePath);
        } else {
            Log.e(TAG, "File path was null or empty. Stopping service.");
            stopSelf();
        }

        // We stop the service ourselves, so START_NOT_STICKY is appropriate.
        return START_NOT_STICKY;
    }

    private void shareImageToWhatsApp(String filePath) {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            Log.e(TAG, "Image file does not exist: " + filePath);
            stopSelf();
            return;
        }

        // Use FileProvider to get a content URI
        Uri imageUri = FileProvider.getUriForFile(
                this,
                getApplicationContext().getPackageName() + ".fileprovider",
                imageFile
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setPackage("com.whatsapp"); // Target WhatsApp specifically
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Log.e(TAG, "WhatsApp is not installed.");
            // Here you would handle the error, maybe show a toast.
        } finally {
            // The service has done its job of launching the UI.
            stopSelf();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Scheduled Send Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // This is a started service, not a bound service.
        return null;
    }
          }
