package com.example.wildcat.espnfirstandten;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.VideoView;

/**
 * Created by wildcat on 11/7/2015.
 */
public class GameStream extends Activity {
    WebView video;
    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamestream);

        load = ProgressDialog.show(GameStream.this, "", "Loading...", true);
        video= (WebView) findViewById(R.id.video);
        video.getSettings().setJavaScriptEnabled(true);
        video.getSettings().setPluginState(WebSettings.PluginState.ON);
        String path = "http://153.104.47.81:9000/javascript_simple.html";
        //MediaController mc = new MediaController(GameStream.this);
        video.loadUrl(path);
        video.setWebChromeClient(new WebChromeClient());

        video.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
            public void onPageFinished(WebView view, String url) {
                load.dismiss();
            }
        });


        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(path));
        startActivity(intent);
    }
}

