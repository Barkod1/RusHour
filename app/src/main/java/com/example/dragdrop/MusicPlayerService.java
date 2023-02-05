package com.example.dragdrop;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

public class MusicPlayerService extends Service {
    //the music player of the app
    static MediaPlayer player;
    //in case the user chooses a song from the phone
    static Uri songUri;
    //gets track if the player is playing
    static boolean isPlaying;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        songUri = SettingsActivity.songUri;
        if(songUri == null) {
                player = MediaPlayer.create(this, R.raw.hotel_california);
        }
        else {
            try {
                player.setDataSource(this,songUri);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        isPlaying = true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            if(player != null)
        player.start();
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        if(player!=null){
            player.stop();
            player.release();
        }
    }

}
