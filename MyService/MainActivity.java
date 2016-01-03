package com.example.wildcat.myservice;

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
import android.widget.ImageView;

public class MainActivity extends Activity {

    private PlaySongService service;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,PlaySongService.class);
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(con);
        }
    }

    public void buttonClick(View view) {
        if (isBound) {int num = service.playSong(); }
    }

    public void pauseClick(View view) {
        if (isBound) {int num = service.pauseSong(); }
    }

    private ServiceConnection con = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibind) {
            service= ((PlaySongService.MyBinder)ibind).getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            isBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
