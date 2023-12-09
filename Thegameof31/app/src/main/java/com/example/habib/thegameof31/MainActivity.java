package com.example.habib.thegameof31;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    public final static String stageNumberKey = "stageNumberKey";
    public final static String stageNameKey = "stageNameKey";
    private final static int request_code = 1995;
    public MySharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Score = "score";



    @Override
    protected void onResume() {
        super.onResume();
        //TextView scoreTV = (TextView) findViewById(R.id.your_score_main_activity);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        //scoreTV.setText(String.valueOf(prefs.getInt(Score, 0)) + " ");
    }




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TextView scoreTV = (TextView) findViewById(R.id.your_score_main_activity);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        //scoreTV.setText(String.valueOf(prefs.getInt(Score, 0)) + " ");
        /*File f = getDatabasePath("MyPrefs.xml");

        if (f != null)
            Log.i("File PATHHHH", f.getAbsolutePath());*/


    }

    public void playOnlineBtn(View view) {
        if (lastLevelAndWinLevelsArray.userNameForKeepLogin.equals("notLogin")){
            startActivity(new Intent(this, LoginActivity.class));
        }
        else{
            Intent intent = new Intent(this, ProfileActivity.class);
            //maybe we need to
            //intent.putExtra("comeFrom","notLoginAndNotSignup");
            startActivity(intent);

        }
    }

    public void trainingBtn(View view) {
        startActivity(new Intent(this,ModesActivity.class));
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

}
