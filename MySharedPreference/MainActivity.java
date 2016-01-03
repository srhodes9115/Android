package com.example.wildcat.mysharedpreference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String WEBQUERY = "http://finance.yahoo.com/q?s=";

    EditText edittext;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.webLink);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockInfo = edittext.getText().toString();
                Intent intent  = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBQUERY + stockInfo));

                //store shared pref here
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String message = WEBQUERY+stockInfo;
                editor.putString("query",message);
                editor.commit();
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();
        //read back the shared pref
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        textView.setText(preferences.getString("query","message"));
    }

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
