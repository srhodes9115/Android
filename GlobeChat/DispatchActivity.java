package srhodes1.villanova.ece.globechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by wildcat on 11/21/2015.
 */
public class DispatchActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this,ContactList.class));
        }
        else {
            startActivity(new Intent(this,LoginActivity.class));
        }

    }


}
