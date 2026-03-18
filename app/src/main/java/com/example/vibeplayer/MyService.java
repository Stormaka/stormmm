package com.example.vibeplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
   public static MediaPlayer mediaPlayer;

   @Override
   public void onCreate() {
       super.onCreate();
       // Lưu ý: R.raw.country_rock cần được thêm vào thư mục res/raw
       mediaPlayer = MediaPlayer.create(this, R.raw.country_rock);
       if (mediaPlayer != null) {
           mediaPlayer.setLooping(true);
       }
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       // 1. Tạo Notification Channel
       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           NotificationChannel channel = new NotificationChannel("noti_channel_id", "My Service Channel", NotificationManager.IMPORTANCE_LOW);
           NotificationManager manager = getSystemService(NotificationManager.class);
           if (manager != null) {
               manager.createNotificationChannel(channel);
           }
       }

       // 2. Tạo Notification
       Notification notification = new NotificationCompat.Builder(this, "noti_channel_id")
                .setContentTitle("VibePlayer")
                .setContentText("Đang phát nhạc...")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

       // 3. Chỉ định là Foreground Service
       startForeground(1, notification);

       if (mediaPlayer != null) {
           mediaPlayer.start();
       }

       return START_STICKY;
   }

   @Override
   public void onDestroy() {
       super.onDestroy();
       if (mediaPlayer != null) {
           mediaPlayer.stop();
           mediaPlayer.release();
           mediaPlayer = null;
       }
   }

   @Nullable
   @Override
   public IBinder onBind(Intent intent) {
       return null;
   }
}
