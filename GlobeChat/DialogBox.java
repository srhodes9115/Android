package srhodes1.villanova.ece.globechat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by wildcat on 12/8/2015.
 */
public class DialogBox extends Activity{

    final Context context = this;
    String messageBody;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        messageBody = intent.getStringExtra("message");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        //set title
        alertDialogBuilder.setTitle("Translated Text:");

        //set dialog  message
        alertDialogBuilder
                .setMessage(messageBody)
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override //if button is clicked, close current activity
                    public void onClick(DialogInterface dialog, int which) {
                        DialogBox.this.finish();
                    }
                });

        //create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        //show dialog
        alertDialog.show();

    }

}
