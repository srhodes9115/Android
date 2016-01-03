package srhodes1.villanova.ece.mysms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends Activity {

    private final static String TAG = "INTERCEPT";

    SmsManager mgr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() triggered");

        // obtain the default SMS Manager handle
        mgr = SmsManager.getDefault();

    }

    // register the broadcast receiver
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() triggered");
        // NOTE: Sms.Intents.SMS_RECEIVED_ACTION now available in API 19
        // http://developer.android.com/reference/android/provider/Telephony.Sms.Intents.html#SMS_RECEIVED_ACTION
        IntentFilter receiverfilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(receiver, receiverfilter);
    }

    // unregister the broadcast receiver
    @Override
    public void onPause()
    {
        Log.d(TAG, "onPause() triggered");
        unregisterReceiver(receiver);
        super.onPause();
    }

    // our broadcast receiver
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive() triggered");

            // get data from the delivered intent
            Bundle data = intent.getExtras();
            if (data != null) {
                // SMS uses the PDUS (Protocol Description Unit) messaging standard
                Object[] pdus = (Object[])data.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];

                // create the SMS data from the PDU data
                int i=0;
                for (Object pdu : pdus) {
                    msgs[i++]=SmsMessage.createFromPdu((byte[])pdu);
                }

                // iterate through the messages: pull out the body, timestamp and sender then reply
                for (SmsMessage msg: msgs) {
                    String msgText = msg.getMessageBody();
                    long msgTimestamp = msg.getTimestampMillis();
                    String msgFrom = msg.getOriginatingAddress();
                    Log.d(TAG, "Received \"" + msgText + "\" from " + msgFrom + " @ " + Long.toString(msgTimestamp));
                    smsReply(msgFrom);
                }
            }
        }
    };

    // reply to sender
    private void smsReply(String destination) {
        EditText et_response = (EditText)this.findViewById(R.id.et_response);
        String response = et_response.getText().toString();
        response = (response.length() > 0) ? response : "Sorry, in class right now.";
        mgr.sendTextMessage(destination, null, response, null, null);
        Log.d(TAG, "Responded \"" + response + "\" to " + destination);
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
