package com.example.vibeplayer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

   Button button;
   ImageView imageView;
   Context context;
   SeekBar seekBar;
   boolean isPlaying = false;
   Handler handler = new Handler();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       context = this;
       setContentView(R.layout.activity_main);
       
       button = findViewById(R.id.button);
       imageView = findViewById(R.id.imageView);
       seekBar = findViewById(R.id.seekBar);

       String imageUrl = "https://static-cse.canva.com/blob/1379502/1600w-1Nr6gsUndKw.jpg";
       
       // Tải ảnh bất đồng bộ
       new Thread(() -> {
           try {
               URL url = new URL(imageUrl);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               connection.setDoInput(true);
               connection.connect();
               InputStream input = connection.getInputStream();
               Bitmap myBitmap = BitmapFactory.decodeStream(input);
               runOnUiThread(() -> imageView.setImageBitmap(myBitmap));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }).start();

       button.setOnClickListener(view -> {
           if (isPlaying) {
               if (MyService.mediaPlayer != null && MyService.mediaPlayer.isPlaying()) {
                   MyService.mediaPlayer.pause();
                   isPlaying = false;
                   button.setText("Play");
               }
           } else {
               Intent intent = new Intent(this, MyService.class);
               startService(intent);
               isPlaying = true;
               button.setText("Pause");
               updateSeekBar();
           }
       });

       seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if (fromUser && MyService.mediaPlayer != null) {
                   MyService.mediaPlayer.seekTo(progress);
               }
           }
           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {}
           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {}
       });
    }

   @Override
   protected void onResume() {
       super.onResume();
       updateSeekBar();
   }

   private void updateSeekBar() {
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               if (MyService.mediaPlayer != null && MyService.mediaPlayer.isPlaying()) {
                   isPlaying = true;
                   button.setText("Pause");
                   seekBar.setMax(MyService.mediaPlayer.getDuration());
                   seekBar.setProgress(MyService.mediaPlayer.getCurrentPosition());
               }
               handler.postDelayed(this, 1000);
           }
       }, 0);
   }

   @Override
   protected void onStop() {
       super.onStop();
       if (isPlaying && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
           Intent notificationIntent = new Intent(context, MainActivity.class);
           notificationIntent.setAction(Intent.ACTION_MAIN);
           notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

           PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

           NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "noti_channel_id")
                   .setSmallIcon(android.R.drawable.ic_media_play)
                   .setContentTitle("Music Player")
                   .setContentText("Nhạc đang phát")
                   .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                   .setContentIntent(pendingIntent)
                   .setAutoCancel(true);

           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
           notificationManager.notify(1, builder.build());
       }
   }
}
