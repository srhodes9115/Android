package com.example.wildcat.myservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by wildcat on 11/6/2015.
 */
public class PlaySongService extends Service {
    private final IBinder binder = new MyBinder();
    private MediaPlayer mediaPlayer;


    public class MyBinder extends Binder {
        //return an instance of the service
        PlaySongService getService() {
            return PlaySongService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //bounded, provide an interface for clients in same process to use
        return binder;
    }

    public int playSong () {
        int result = 0;
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(PlaySongService.this, R.raw.fightsong);
            mediaPlayer.start();
        }
        else {
            mediaPlayer.start();
        }
        return result;
    }

    public int pauseSong()
    {
        int result = 0;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

        return result;
    }
}
