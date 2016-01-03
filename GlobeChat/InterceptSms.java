package srhodes1.villanova.ece.globechat;

import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Telephony;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


import java.util.Locale;


/**
 * Created by wildcat on 12/4/2015.
 */
public class InterceptSms extends Service {
    private final static String TAG = "INTERCEPT";
    SmsManager mgr = null;
    private final IBinder binder = new MyBinder();
    private String languageReceive;
    private String languageSend;
    private String msgText;
    BroadcastReceiver receiver;
    TextToSpeech tts;
    HandlerThread schedulr;

    @Override
    public void onCreate() {
        super.onCreate();

        mgr = SmsManager.getDefault();

        //register the broadcast receiver and run on separate thread
        IntentFilter receiverFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        receiver = new MyReceiver();
        schedulr = new HandlerThread("ht");
        schedulr.start();
        Looper loop = schedulr.getLooper();
        Handler handler = new Handler(loop);
        registerReceiver(receiver, receiverFilter, null, handler);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                {
                    Log.d("MyApp","inside text to speech");
                    tts.setLanguage(Locale.US);
                }
            }
        });

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            Log.d("MyApp","made it to the receiver");
            //get data from the delivered intent
            Bundle data = intent.getExtras();
            if (data != null) {
                //SMS uses the PDUS (Protocol Description Unit) messaging standard
                Object[] pdus = (Object[]) data.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];

                //create the sms data from the PDU data
                int i = 0;
                for (Object pdu: pdus) {
                    msgs[i++]=SmsMessage.createFromPdu((byte[])pdu);
                }

                //iterate through the messages: pull out the body, timestamp and sender then reply
                for (SmsMessage msg: msgs)
                {
                    msgText = msg.getMessageBody();


                    try {
                        Translate.setClientId("globe_Chat_21");
                        Translate.setClientSecret("SjkHEkEm0iyjPQn2H+pZawY7ygaRDIYPppabJo8u9Ws=");
                        msgText = Translate.execute(msgText,findLanguage(languageReceive), findLanguage(languageSend));
                        Log.d("MyApp", "translated text = " + msgText);
                        Log.d("MyApp", "receive language: " + findLanguage(languageReceive).toString());
                        Log.d("MyApp", "send language: " + findLanguage(languageSend).toString());

                        Intent dialog = new Intent(InterceptSms.this, DialogBox.class);
                        dialog.putExtra("message",msgText);
                        dialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialog);

                        //Toast.makeText(InterceptSms.this,msgText,Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        Log.d("MyApp", "hitting exception = " + e.getMessage());
                        Toast.makeText(InterceptSms.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                tts.speak(msgText, TextToSpeech.QUEUE_FLUSH, null);
            }
        }

        public MyReceiver()
        {
            super();
            Log.d("MyApp", "myReceiver called=");

        }

        //find language function
        public Language findLanguage (String languageType)
        {
            if(languageType.equals("Spanish"))
            {
                return Language.SPANISH;
            }
            else if (languageType.equals("French"))
            {

                return Language.FRENCH;
            }
            else if (languageType.equals("English"))
            {
                return Language.ENGLISH;
            }
            return Language.ENGLISH;
        }
    }

    //needed to receive intent extras
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        languageReceive =(String) intent.getExtras().get("receiveLanguage");
        languageSend = (String) intent.getExtras().get("sendLanguage");

        return START_STICKY;
    }

    public class MyBinder extends Binder {
        //return an instance of the service
        InterceptSms getService() {return InterceptSms.this;}
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        schedulr.stop();

        unregisterReceiver(receiver);
        super.onDestroy();
    }


}
