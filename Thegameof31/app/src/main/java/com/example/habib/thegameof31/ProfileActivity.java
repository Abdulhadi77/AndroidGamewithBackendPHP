package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    public static String showInMsgBox;
    public static Context profileContext;

    public static final String MyPREFERENCES = "MyPrefs" ;
    //public SharedPreferences sharedpreferences;
    public MySharedPreferences prefs;
    SectionsPagerAdapter mSectionsPagerAdapter;
    SlidingTabLayout tabs;
    CharSequence Title_Tab[]={String.valueOf(R.string.your_profile),String.valueOf(R.string.play_with_facebook),String.valueOf(R.string.play_randomly)};
    //private ActionBar actionBar;

    ViewPager mViewPager;
    TextView userNameTextView;
    TextView pwTextView;
    TextView emailTextView;
    String userNameFromDB;

    public static int idSender;
    public static Context mainContext;

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

    public static Handler connectionRequestHandler = new Handler(){
        public void handleMessage(Message msg){
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(mainContext);
            dlgAlert.setTitle(R.string.game_name);

            dlgAlert.setMessage(mySocket.userNameSender + " " +profileContext.getResources().getString(R.string.wants_to_play_with_you));

            dlgAlert.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mySocket.myTurn = false;
                    JSONObject reponse = new JSONObject();
                    try {
                        reponse.put("cmd", "connectionReponse");
                        reponse.put("idReceiver", idSender);
                        reponse.put("reponse", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(reponse.toString());
                    //move to game field online activity
                    Intent i = new Intent(mainContext, GameFieldOnline.class);
                    mainContext.startActivity(i);
                }
            });
            dlgAlert.setNegativeButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    JSONObject reponse = new JSONObject();
                    try {
                        reponse.put("cmd" , "connectionReponse");
                        reponse.put("idReceiver" , idSender);
                        reponse.put("reponse" , false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(reponse.toString());
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileContext = this;
        mainContext = this;
        mySocket.context = this;
        View inflatedView = getLayoutInflater().inflate(R.layout.activity_fragment_profile, null);
        LinearLayout linearLayout = (LinearLayout) inflatedView.findViewById(R.id.info_layout);
        linearLayout.setVisibility(View.INVISIBLE);
        Button displayButton = (Button) inflatedView.findViewById(R.id.display_btn);
        displayButton.setVisibility(View.VISIBLE);

        TextView userNameUnderImage = (TextView) findViewById(R.id.your_name);
        //TextView scoreProfile = (TextView) findViewById(R.id.score_profile_id);


        prefs = MySharedPreferences.getInstance(this,"tutorialsFACE_Prefs"); //provide context & preferences name.
        lastLevelAndWinLevelsArray.score = prefs.getInt("score",0);

        userNameUnderImage.setText(lastLevelAndWinLevelsArray.userNameForKeepLogin);
        //scoreProfile.setText(String.valueOf(lastLevelAndWinLevelsArray.score));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class
            // below).
            switch(position){
                case 0:
                    return new FragmentProfile();
                case 1:
                    return new SwipeRefreshListFragmentFragment();
                case 2:
                    return new FragmentRandom();
                default:
                    return new FragmentProfile();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Locale l = Locale.getDefault();

            switch (position) {
                case 0:
                    return getString(R.string.title_section1);
                case 1:
                    return getString(R.string.title_section2);
                case 2:
                    return getString(R.string.title_section3);
            }
            return null;
        }
    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_fragment_profile, container, false);
            return rootView;
        }
    }


    //Edit Info

    //edit userName
    public void btnEditUserName(View view)
    {
        //Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setMessage(R.string.edit_your_user_name);
        alert.setTitle(R.string.edit_your_informations);
        alert.setView(edittext);
        userNameTextView = (TextView) findViewById(R.id.profile_user_name_id);
        final String userName = userNameTextView.getText().toString();
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newUserName = edittext.getText().toString();
                if (newUserName.isEmpty() || newUserName.length() < 3) {
                    edittext.setError(String.valueOf(R.string.error_user_name));
                    Toast.makeText(ProfileActivity.this, R.string.error_user_name, Toast.LENGTH_SHORT).show();
                }
                else{
                    lastLevelAndWinLevelsArray.userNameForKeepLogin = newUserName;
                    //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    prefs = MySharedPreferences.getInstance(ProfileActivity.this,"tutorialsFACE_Prefs"); //provide context & preferences name.
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    prefs.putString("userName",newUserName);
                    prefs.putString("userNameForKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);
                    prefs.commit();

                    //update it in database
                    JSONObject updateUserNameRequest = new JSONObject();
                    try {
                        updateUserNameRequest.put("cmd" , "updateUserName");
                        updateUserNameRequest.put("newUserName" , newUserName);
                        updateUserNameRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updateUserNameRequest.toString());
                    //update it in textview in fragment
                    Log.i("New User Name" , newUserName);
                    userNameTextView.setText(newUserName);
                    TextView userName =(TextView)findViewById(R.id.your_name);
                }
            }
        });

        alert.setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.

            }
        });

        alert.show();
    }
    //edit password
    public void btnEditPassword(View view)
    {
        //Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alert.setTitle(R.string.edit_your_informations);
        alert.setMessage(R.string.edit_your_password);
        TextView TVPassword = (TextView) findViewById(R.id.pw_id);
        final String password = TVPassword.getText().toString();
        alert.setView(edittext);
        userNameTextView = (TextView) findViewById(R.id.profile_user_name_id);
        final String userName = userNameTextView.getText().toString();

        alert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newPassword = edittext.getText().toString();
                if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
                    edittext.setError(String.valueOf(R.string.error_password));
                    Toast.makeText(ProfileActivity.this, R.string.error_password, Toast.LENGTH_LONG).show();
                }
                else{
                    //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    prefs = MySharedPreferences.getInstance(ProfileActivity.this,"tutorialsFACE_Prefs"); //provide context & preferences name.

                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    prefs.putString("pw",newPassword);
                    prefs.commit();

                    //update it in database
                    JSONObject updatePasswordRequest = new JSONObject();
                    try {
                        updatePasswordRequest.put("cmd" , "updatePassword");
                        updatePasswordRequest.put("newPassword" , newPassword);
                        updatePasswordRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updatePasswordRequest.toString());
                    //update it in textview in fragment
                    pwTextView.setText(newPassword);
                }
            }
        });

        alert.setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.

            }
        });

        alert.show();
    }
    //edit email
    public void btnEditEmail(View view)
    {
        //Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle(R.string.edit_your_informations);
        alert.setMessage(R.string.edit_your_email);
        TextView TVEmail = (TextView) findViewById(R.id.email_id);
        final String email = TVEmail.getText().toString();
        alert.setView(edittext);
        userNameTextView = (TextView) findViewById(R.id.profile_user_name_id);
        final String userName = userNameTextView.getText().toString();

        alert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newEmail = edittext.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edittext.setError(String.valueOf(R.string.error_email));
                    Toast.makeText(ProfileActivity.this, R.string.error_email, Toast.LENGTH_SHORT).show();
                }
                else{
                    //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    prefs = MySharedPreferences.getInstance(ProfileActivity.this,"tutorialsFACE_Prefs"); //provide context & preferences name.
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    prefs.putString("email",newEmail);
                    prefs.commit();

                    //update it in database
                    JSONObject updateEmailRequest = new JSONObject();
                    try {
                        updateEmailRequest.put("cmd" , "updateEmail");
                        updateEmailRequest.put("newEmail" , newEmail);
                        updateEmailRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updateEmailRequest.toString());
                    //update it in textview in fragment
                    emailTextView.setText(newEmail);
                }
            }
        });

        alert.setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.

            }
        });

        alert.show();
    }
    //edit image
    public void editImage(View view) {
    }

    //End Edit Info


    public void logoutBtn(View view){
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        prefs.putBoolean("hasAccount",lastLevelAndWinLevelsArray.hasAccount);
        prefs.putString("userNameForKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);
        prefs.commit();

        TextView TVUserName = (TextView) findViewById(R.id.profile_user_name_id);
        String _userName = TVUserName.getText().toString();
        TextView TVPassword = (TextView) findViewById(R.id.pw_id);
        String _password = TVPassword.getText().toString();
        TextView TVEmail = (TextView) findViewById(R.id.email_id);
        String _email = TVEmail.getText().toString();
        TextView TVScore = (TextView) findViewById(R.id.score_id);
        String _score = TVScore.getText().toString();

        //make him offline in database
        JSONObject logoutRequest = new JSONObject();
        try {
            logoutRequest.put("cmd" , "logoutRequest");
            logoutRequest.put("userName" , _userName);
            logoutRequest.put("password" , _password);
            logoutRequest.put("email" , _email);
            logoutRequest.put("score" ,_score);
            logoutRequest.put("userNameKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);
            //add score and save it in database
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mySocket.mWebSocketClient.send(logoutRequest.toString());

        lastLevelAndWinLevelsArray.userNameForKeepLogin = "notLogin";
        lastLevelAndWinLevelsArray.hasAccount = false;

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void displayBtn(View view) {
        Intent intent = getIntent();

        //View inflatedView = getLayoutInflater().inflate(R.layout.activity_fragment_profile, null);
        userNameTextView = (TextView) findViewById(R.id.profile_user_name_id);
         pwTextView = (TextView) findViewById(R.id.pw_id);
         emailTextView = (TextView) findViewById(R.id.email_id);
        TextView score = (TextView) findViewById(R.id.score_id);
        TextView yourName = (TextView) findViewById(R.id.your_name);
        android.app.Fragment fragmentProfile = getFragmentManager().findFragmentByTag("fragmentProfileTag");
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.

        if (intent.getExtras() != null)
            if (intent.getExtras().getString("comeFrom").equals("login") || intent.getExtras().getString("comeFrom").equals("signup")){

                userNameFromDB = intent.getExtras().getString("userName");
                String pwFromDB = intent.getExtras().getString("pw");
                String emailFromDB = intent.getExtras().getString("email");
                int scoreFromDB = intent.getExtras().getInt("score");

                userNameTextView.setText(userNameFromDB);
                pwTextView.setText(pwFromDB);
                emailTextView.setText(emailFromDB);
                score.setText(String.valueOf(scoreFromDB));
                yourName.setText(userNameFromDB);
                //save info in sharedPreference , for when he closes the app and return back (with keepping login)
                //SharedPreferences.Editor editor = sharedpreferences.edit();
                prefs.putString("userNameForKeepLogin",userNameFromDB);
                prefs.putString("userName", userNameFromDB);
                prefs.putString("pw", pwFromDB);
                prefs.putString("email", emailFromDB);
                prefs.putInt("score", scoreFromDB);
                prefs.commit();
            }
            else{
                //get data from sharedPreference and display it in textviews
                userNameFromDB = prefs.getString("userNameForKeepLogin",null);
                String userNameFromSP = prefs.getString("userName",null);
                String pwFromSP = prefs.getString("password",null);
                String emailFromSP = prefs.getString("email",null);
                int scoreFromSP = prefs.getInt("score",0);

                userNameTextView.setText(userNameFromSP);
                pwTextView.setText(pwFromSP);
                emailTextView.setText(emailFromSP);
                score.setText(String.valueOf(scoreFromSP));
            }
        else{
            //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
            String userNameFromSP = prefs.getString("userName",null);
            String pwFromSP = prefs.getString("pw",null);
            String emailFromSP = prefs.getString("email",null);
            int scoreFromSP = prefs.getInt("score",0);

            userNameTextView.setText(userNameFromSP);
            pwTextView.setText(pwFromSP);
            emailTextView.setText(emailFromSP);
            score.setText(String.valueOf(scoreFromSP));
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.info_layout);
        linearLayout.setVisibility(View.VISIBLE);
        Button displayButton = (Button) findViewById(R.id.display_btn);
        displayButton.setVisibility(View.INVISIBLE);
    }

    public void searchRandomUser(View view) {
        final TextView userNameTextView = (TextView) findViewById(R.id.userNameOfRandomPlayer);
        //final TextView scoreTextView = (TextView) findViewById(R.id.scoreOfRandomPlayer);
        final Button buttonInvite = (Button) findViewById(R.id.btnInviteRandomPlayer);

        JSONObject searchRandomPlayerRequest = new JSONObject();
        try {
            searchRandomPlayerRequest.put("cmd" , "searchRandomPlayerRequest");
            searchRandomPlayerRequest.put("userName" , userNameFromDB);
            //searchRandomPlayerRequest.put("","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mySocket.mWebSocketClient.send(searchRandomPlayerRequest.toString());

        userNameTextView.setVisibility(View.VISIBLE);
        //scoreTextView.setVisibility(View.VISIBLE);
        buttonInvite.setVisibility(View.VISIBLE);

        lastLevelAndWinLevelsArray.player2 = prefs.getString("userNameOfRandomPlayer",null);
        lastLevelAndWinLevelsArray.scoreOfRandomPlayer = prefs.getInt("scoreOfRandomPlayer",0);
        userNameTextView.setText(lastLevelAndWinLevelsArray.player2);
        //scoreTextView.setText(String.valueOf(lastLevelAndWinLevelsArray.scoreOfRandomPlayer) +" "+profileContext.getResources().getString(R.string.points));
    }

    public void inviteBtn(View view){
        TextView tvPartner = (TextView) findViewById(R.id.userNameOfRandomPlayer);
        String partnerUserName = tvPartner.getText().toString();

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        lastLevelAndWinLevelsArray.userNameForKeepLogin =  prefs.getString("userNameForKeepLogin",null);

        lastLevelAndWinLevelsArray.player1 = lastLevelAndWinLevelsArray.userNameForKeepLogin;
        lastLevelAndWinLevelsArray.scorePlayer1 = lastLevelAndWinLevelsArray.score;

        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        prefs.putString("userNameOfPlayer1",lastLevelAndWinLevelsArray.player1);
        prefs.putInt("scoreOfPlayer1",lastLevelAndWinLevelsArray.scorePlayer1);
        prefs.commit();

        Log.i("partnerUserName", partnerUserName);
        Log.i("userNameForKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);


        if (!partnerUserName.toLowerCase().equals(lastLevelAndWinLevelsArray.userNameForKeepLogin.toLowerCase())) {
            JSONObject connectionRequest = new JSONObject();
            try {
                connectionRequest.put("cmd", "connectionRequest");
                connectionRequest.put("userNameReceiver", partnerUserName);
                connectionRequest.put("userNameSender", lastLevelAndWinLevelsArray.userNameForKeepLogin);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mySocket.mWebSocketClient.send(connectionRequest.toString());
        }
        else{
            Toast.makeText(ProfileActivity.this, R.string.wants_to_play_with_you, Toast.LENGTH_SHORT).show();
        }
    }
}
