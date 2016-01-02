package com.example.wildcat.mysqlite;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity { //how to configure it as ListActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StudentHelper dbHelper = new StudentHelper(this);
        Student student = new Student();

        Cursor cursor = dbHelper.getStudents();
        //this.getContentResolver().query();

        Log.d("Insert: ", "Inserting ..");
        dbHelper.addStudent("Shannon Rhodes", "CPE", 2016);
        dbHelper.addStudent("Colleen Hickey","CPE", 2016);
        dbHelper.addStudent("Matthew Gregory","CPE" ,2017);
        dbHelper.addStudent("Brian Kim", "CPE", 2015);
        dbHelper.addStudent("Katie Novak", "EE", 2015);

        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_entry, //layout
                dbHelper.getStudents(), //this is the cursor
                new String [] {dbHelper.S_NAME,dbHelper.S_MAJOR,dbHelper.S_YEAR},
                new int [] {R.id.student_name, R.id.student_major, R.id.student_year},
                0);

            setListAdapter(adapter);

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
