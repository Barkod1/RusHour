package com.example.dragdrop;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicPlayerService extends Service {
    static MediaPlayer player;
    static boolean isPlaying;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.hotel_california);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        isPlaying = true;

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {

    }
    public void onPause() {

    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {

    }

    public static boolean isPlaying(){
        if(player != null)
        return isPlaying;
        return false;
    }
}