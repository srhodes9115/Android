package com.example.wildcat.espnfirstandten;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by wildcat on 11/8/2015.
 */
public class openCvStream extends Activity{

    private VideoView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencv);

        view = (VideoView) findViewById(R.id.videoDraw);
        view.setMediaController(new MediaController(this));
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cv_final);
        view.setVideoURI(video);
        view.start();
    }
}
