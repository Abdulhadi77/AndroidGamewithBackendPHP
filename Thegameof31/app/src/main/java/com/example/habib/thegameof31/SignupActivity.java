package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

/*<ImageView android:src="@drawable/logo"
        android:layout_width="wrap_content"
        android:layout_height="72dp"
        android:layout_marginBottom="24dp"
        android:layout_gravity="center_horizontal" />*/
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText _nameText;
    EditText _emailText;
    EditText _passwordText;
    Button _signupButton;
    TextView _loginLink;
    Context signupContext;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public MySharedPreferences prefs;

    private WebSocketClient mWebSocketClient;
    private TextView tv;

    public static int idSender;
    public static String userName;
    public static String showInMsgBox;
    public static Context mainContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mainContext = this;
        signupContext = this;
        //mySocket.init(this);
        mySocket.context = this;
        //ButterKnife.bind(this);
        _nameText = (EditText)findViewById(R.id.input_name);
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _signupButton = (Button)findViewById(R.id.btn_signup);
        _loginLink = (TextView)findViewById(R.id.link_login);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                //finish();
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }

    public static Handler handlerMsgBox = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Bundle b = msg.getData();
            //String key = b.getString("timeKey");
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mainContext);
            dlgAlert.setTitle(R.string.game_name);
            dlgAlert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.setMessage(showInMsgBox);
            dlgAlert.create().show();
        }
    };

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(signupContext.getResources().getString(R.string.creating_account));
        progressDialog.show();

        _signupButton.setEnabled(false);
        EditText edUser = (EditText) findViewById(R.id.input_name);
        String _userName = edUser.getText().toString();
        EditText email = (EditText) findViewById(R.id.input_email);
        String _email = email.getText().toString();
        EditText password = (EditText) findViewById(R.id.input_password);
        String _password = password.getText().toString();
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        lastLevelAndWinLevelsArray.score = prefs.getInt("score",0);

        JSONObject signupRequest = new JSONObject();
        try {
            signupRequest.put("cmd" , "signupRequest");
            signupRequest.put("userName" , _userName);
            signupRequest.put("email" , _email);
            signupRequest.put("password" , _password);
            signupRequest.put("score",lastLevelAndWinLevelsArray.score);
            //add score and save it in database
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mySocket.mWebSocketClient.send(signupRequest.toString());


        // TODO: Implement your own signup logic here.
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), signupContext.getResources().getString(R.string.signup_failed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError(signupContext.getResources().getString(R.string.error_user_name));
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(signupContext.getResources().getString(R.string.error_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError(signupContext.getResources().getString(R.string.error_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}