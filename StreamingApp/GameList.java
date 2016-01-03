package com.example.wildcat.espnfirstandten;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by wildcat on 11/7/2015.
 */
public class GameList extends Activity {

    private ImageButton button;
    private ImageButton cv;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);

        button = (ImageButton) findViewById(R.id.villanovaGame);
        cv = (ImageButton) findViewById(R.id.cvButton);

        final Intent intent = new Intent(this, GameStream.class);
        final Intent openCv = new Intent(this,openCvStream.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(openCv);
            }
        });

    }
}
