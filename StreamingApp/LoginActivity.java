package com.example.wildcat.espnfirstandten;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LoginActivity extends Activity {
    private SongService service;
    boolean isBound = false;
    private Button button;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        button = (Button) findViewById(R.id.button);
        signUp = (Button) findViewById(R.id.signButton);

        final Intent sign = new Intent(this,SignUpActivity.class);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(sign);
            }
        });

        final Intent intent = new Intent(this, GameList.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    service.playSong();
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SongService.class);
        bindService(intent, con, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound) {
            unbindService(con);
        }
    }

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibind) {
            service=((SongService.MyBinder)ibind).getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service=null;
            isBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
