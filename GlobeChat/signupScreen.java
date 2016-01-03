package srhodes1.villanova.ece.globechat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by wildcat on 11/21/2015.
 */
public class signupScreen extends Activity {

    private Button finish;
    private EditText username;
    private EditText phoneNumber;
    private EditText password;
    private EditText passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signup_screen);

        finish = (Button) findViewById(R.id.finish);
        username = (EditText) findViewById(R.id.addUserEdit);
        phoneNumber = (EditText) findViewById(R.id.addPhoneNumber);
        password = (EditText) findViewById(R.id.passwordFirstEdit);
        passwordCheck = (EditText) findViewById(R.id.passwordCheckEdit);
        isEmpty(username);

        final Intent intent = new Intent(this,DispatchActivity.class);
        findViewById(R.id.finish).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please ");

                if(isEmpty(username))
                {
                    validationError = true;
                    validationErrorMessage.append("enter a username");
                }

                if(isEmpty(phoneNumber))
                {
                    if(validationError)
                    {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter a phone number");
                }

                if(isEmpty(password))
                {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter a password");
                }

                if(!isMatching(password,passwordCheck))
                {
                    if(validationError)
                    {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter the same password twice");
                }
                validationErrorMessage.append(".");


                if (validationError)
                {
                    Toast.makeText(signupScreen.this,validationErrorMessage.toString(),Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog dlg = new ProgressDialog(signupScreen.this);
                dlg.setTitle("Please wait");
                dlg.setMessage("Signing up. please wait");
                dlg.show();

                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.put("phoneNumber",phoneNumber.getText().toString());
                user.setPassword(password.getText().toString());


                user.signUpInBackground(new SignUpCallback()
                {
                    @Override
                    public void done(ParseException e) {
                        dlg.dismiss();
                        if (e!= null)
                        {
                            Toast.makeText(signupScreen.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    public boolean isEmpty(EditText editText)
    {
        if(editText.getText().toString().trim().length() > 0)
        {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isMatching(EditText editText, EditText editText2)
    {
        if(editText.getText().toString().equals(editText2.getText().toString()))
        {
            return true;
        }
        else {
            return false;
        }
    }
}
