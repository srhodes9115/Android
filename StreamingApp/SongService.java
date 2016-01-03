package com.example.wildcat.espnfirstandten;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by wildcat on 11/7/2015.
 */
public class SongService extends Service{
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        //return an instance of the service
        SongService getService() {
            return SongService.this;
        }
    }

    @Override
    public IBinder onBind (Intent intent) {
        return binder;
    }

    public int playSong () {
        int result = 0;
        MediaPlayer mediaPlayer = MediaPlayer.create(SongService.this,R.raw.espnsound);
        mediaPlayer.start();
        return result;
    }
}
