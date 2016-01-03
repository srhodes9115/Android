package srhodes1.villanova.ece.globechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by wildcat on 12/2/2015.
 */
public class NewFriend extends Activity {

    private Button btn;
    private EditText et;
    private Spinner receive;
    private Spinner send;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_friend);

        btn = (Button) findViewById(R.id.createFriend);
        et = (EditText) findViewById(R.id.usernameFriend);
        receive = (Spinner) findViewById(R.id.receiveLanguageType);
        send = (Spinner) findViewById(R.id.sendLanguageType);
        currentUser = ParseUser.getCurrentUser().getUsername();

        ArrayAdapter<CharSequence> adapterReceive = ArrayAdapter.createFromResource(NewFriend.this,R.array.languages,android.R.layout.simple_spinner_item);
        adapterReceive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receive.setAdapter(adapterReceive);

        ArrayAdapter<CharSequence> adapterSend = ArrayAdapter.createFromResource(NewFriend.this,R.array.languages,android.R.layout.simple_spinner_item);
        adapterSend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        send.setAdapter(adapterSend);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) // Connection was successful
                        {
                            for (int i = 0; i < objects.size(); i++) {
                                if (objects.get(i).getString("username").equals(et.getText().toString())) {

                                    ParseObject chatPreference = new ParseObject("chatPreference_" + currentUser);
                                    checkUser(chatPreference, currentUser, et.getText().toString());

                                    Intent intent = new Intent(NewFriend.this, ContactList.class);
                                    intent.putExtra("username", et.getText().toString());
                                    intent.putExtra("sendLanguage",send.getSelectedItem().toString());
                                    intent.putExtra("receiveLanguage", receive.getSelectedItem().toString());
                                    intent.putExtra("phoneNumber",objects.get(i).getString("phoneNumber"));
                                    setResult(RESULT_OK, intent);
                                    startActivity(intent);
                                    break;
                                }
                            }
                        }
                        else {
                            Toast.makeText(NewFriend.this, "Error please try again", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "Failed to query ParseUser database");
                        }
                    }
                });
            }
        });
    }

    public void checkUser (final ParseObject chatPreference, String current, String user)
    {
        ParseQuery checkUser = ParseQuery.getQuery("chatPreference_" + current);
        checkUser.whereEqualTo("username", user);
        checkUser.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {

                        if (objects.size() != 0) { //check to see if User does not exist, update if exists add if doesn't exist
                            Log.d("MyApp", "Username = " + et.getText().toString() + " already exists, updated languages");
                            objects.get(0).put("sendLanguage",send.getSelectedItem().toString());
                            objects.get(0).put("receiveLanguage",receive.getSelectedItem().toString());
                            objects.get(0).saveInBackground();

                        }
                        else {
                            Log.d("MyApp", "Added Successful username = " + et.getText().toString());
                            chatPreference.put("username", et.getText().toString());
                            chatPreference.put("sendLanguage", send.getSelectedItem().toString());
                            chatPreference.put("receiveLanguage", receive.getSelectedItem().toString());
                            chatPreference.saveInBackground();
                        }
                    } catch (NullPointerException x) {
                        Log.d("MyApp", "Added Successful username = " + et.getText().toString());
                        chatPreference.put("username", et.getText().toString());
                        chatPreference.put("sendLanguage", send.getSelectedItem().toString());
                        chatPreference.put("receiveLanguage", receive.getSelectedItem().toString());
                        chatPreference.saveInBackground();
                    }
                }

            }
        });
    }

}
