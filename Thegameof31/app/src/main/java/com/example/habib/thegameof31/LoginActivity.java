package com.example.habib.thegameof31;
//    <item name="android:colorButtonNormal">@color/primary_darker</item>
/*<ImageView android:src="@drawable/logo"
            android:layout_width="wrap_content"
            android:layout_height="72dp"
            android:layout_marginBottom="24dp"
            android:layout_gravity="center_horizontal" />*/

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.logging.Handler;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _userName;
    EditText _passwordText ;
    Button _loginButton  ;
    TextView _signupLink ;
    Context loginContext;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //public SharedPreferences sharedpreferences;
    public MySharedPreferences prefs;

    private WebSocketClient mWebSocketClient;
    private TextView tv;

    public static int idSender;
    public static String userName;
    public static String showInMsgBox;
    public static Context mainContext;


    /*public static Handler handlerMsgBox = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Bundle b = msg.getData();
            //String key = b.getString("timeKey");
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mainContext);
            dlgAlert.setTitle("App Title");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.setMessage(showInMsgBox);
            dlgAlert.create().show();
        }
    };*/

    public static android.os.Handler handlerMsgBox = new android.os.Handler() {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mainContext = this;
        loginContext = this;
        mySocket.init();
        mySocket.context = this;
        _userName =(EditText) findViewById(R.id.input_User_Name);
        _passwordText =(EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(loginContext.getResources().getString(R.string.authenticating));
        progressDialog.show();

        String userName = _userName.getText().toString();
        String password = _passwordText.getText().toString();

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        lastLevelAndWinLevelsArray.score = prefs.getInt("score",0);


        JSONObject loginRequest = new JSONObject();
        try{
            loginRequest.put("cmd","loginRequest");
            loginRequest.put("userName" , userName);
            loginRequest.put("password" , password);
            loginRequest.put("score", lastLevelAndWinLevelsArray.score);
            //loginRequest.put()
        }catch (JSONException e) {
            e.printStackTrace();
        }
        mySocket.mWebSocketClient.send((loginRequest.toString()));
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                //this.finish();
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        //moveTaskToBack(true);
    }*/

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), loginContext.getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = _userName.getText().toString();
        String password = _passwordText.getText().toString();

        //if (userName.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
        if (userName.isEmpty()){
            _userName.setError(loginContext.getResources().getString(R.string.enter_user_name));
            valid = false;
        } else {
            _userName.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError(loginContext.getResources().getString(R.string.error_password));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
