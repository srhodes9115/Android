package srhodes1.villanova.ece.globechat;

import android.app.ListActivity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wildcat on 11/11/2015.
 */
public class ContactList extends ListActivity {

    private Button logoutButton;
    private Button addButton;
    private Button chatButton;
    private Service service;
    private List<String> listValues;
    private ArrayAdapter<String> adp;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contacts);
        Log.d("MyApp","onCreate()");

        logoutButton = (Button) findViewById(R.id.logout);
        addButton = (Button) findViewById(R.id.addFriend);
        chatButton = (Button) findViewById(R.id.buttonChat);

        final Intent friendAdd = new Intent(this,NewFriend.class);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(friendAdd,1);
            }
        });


        final Intent intent = getIntent();
        final String userAdd = intent.getStringExtra("username");

        setListAdapter(addToList(userAdd, listValues, adp));

            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent for service
                    Intent intentService = new Intent(ContactList.this, InterceptSms.class);
                    intentService.putExtra("receiveLanguage", intent.getStringExtra("receiveLanguage"));
                    intentService.putExtra("sendLanguage", intent.getStringExtra("sendLanguage"));

                    bindService(intentService, con, Context.BIND_AUTO_CREATE);
                    startService(intentService);

                    Uri uri = Uri.parse("smsto:" + intent.getStringExtra("phoneNumber"));
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra("sms_body", "Message...");
                    startActivity(intent);
                }
            });

        //Log out of application
        final Intent logoutIntent = new Intent(this,LoginActivity.class);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                startActivity(logoutIntent);
            }
        });
    }

    public ArrayAdapter<String> addToList (String userName,List<String> list,ArrayAdapter<String> adp)
    {
        if (userName != null) {
            if (list == null || list.isEmpty()) {
                Log.d("MyApp:","listValues Empty");
                list = new ArrayList<String>();
                list.add(userName);

            } else {
                Log.d("MyApp:","List Values adding, not empty");
                list.add(userName);
            }
            if(adp == null || adp.isEmpty())
            {
                Log.d("MyApp:","ADP is empty, creating new one");
                adp = new ArrayAdapter<String>(this,R.layout.friendlist_entry,R.id.friendName,list);
            }
            else {
                Log.d("MyApp:","reached notify data set changed");
                adp.notifyDataSetChanged();
            }
        }
        return adp;
    }

    private ServiceConnection con = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder ibind) {
            service= ((InterceptSms.MyBinder)ibind).getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            isBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyApp", "onResume() triggered");
        if (isBound) {
            unbindService(con);
        }
    }
}
