package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentProfile extends Fragment{
    private static final String TAG = "RecyclerViewFragment";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public SharedPreferences sharedpreferences;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 30;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;


    protected MyAdapter mAdapter;
    protected String[] mDataset;

    public FragmentProfile(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //SecurePreferences preferences = new SecurePreferences(context, "my-preferences", "SometopSecretKey1235", true);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = getActivity().getIntent();
        View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.activity_fragment_profile, null);
        TextView userName = (TextView) inflatedView.findViewById(R.id.profile_user_name_id);
        TextView pw = (TextView) inflatedView.findViewById(R.id.pw_id);
        TextView email = (TextView) inflatedView.findViewById(R.id.email_id);
        TextView score = (TextView) inflatedView.findViewById(R.id.score_id);

    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_fragment_profile, container, false);
            Intent intent = getActivity().getIntent();
            View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.activity_fragment_profile, null);
            TextView userName = (TextView) inflatedView.findViewById(R.id.profile_user_name_id);
            TextView pw = (TextView) inflatedView.findViewById(R.id.pw_id);
            TextView email = (TextView) inflatedView.findViewById(R.id.email_id);
            TextView score = (TextView) inflatedView.findViewById(R.id.score_id);

            if (intent.getExtras() != null)
                if (intent.getExtras().getString("comeFrom").equals("login") || intent.getExtras().getString("comeFrom").equals("signup")){

                    String userNameFromDB = intent.getExtras().getString("userName");
                    String pwFromDB = intent.getExtras().getString("pw");
                    String emailFromDB = intent.getExtras().getString("email");
                    int scoreFromDB = intent.getExtras().getInt("score");

                    userName.setText(userNameFromDB);
                    pw.setText(pwFromDB);
                    email.setText(emailFromDB);
                    score.setText(String.valueOf(scoreFromDB));

                    //save info in sharedPreference , for when he closes the app and return back (with keepping login)
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userNameForKeepLogin",userNameFromDB);
                    editor.putString("userName", userNameFromDB);
                    editor.putString("password", pwFromDB);
                    editor.putString("email", emailFromDB);
                    editor.putInt("score", scoreFromDB);
                    editor.commit();
                }
                else{
                    //get data from sharedPreference and display it in textviews
                    String userNameFromSP = sharedpreferences.getString("userName",null);
                    String pwFromSP = sharedpreferences.getString("password",null);
                    String emailFromSP = sharedpreferences.getString("email",null);
                    int scoreFromSP = sharedpreferences.getInt("score",0);

                    userName.setText(userNameFromSP);
                    pw.setText(pwFromSP);
                    email.setText(emailFromSP);
                    score.setText(String.valueOf(scoreFromSP));
                }

        rootView.setTag(TAG);
        return rootView;
    }

    public void setTextViewUserName(String newUserName){
        TextView textView = (TextView) getView().findViewById(R.id.profile_user_name_id);
        textView.setText(newUserName);
    }
    public void setTextViewEmail(String newEmail){
        TextView textView = (TextView) getView().findViewById(R.id.email_id);
        textView.setText(newEmail);
    }
    public void setTextViewPassword(String newPassword){
        TextView textView = (TextView) getView().findViewById(R.id.pw_id);
        textView.setText(newPassword);
    }
    public void setTextViewScore(String newScore){
        TextView textView = (TextView) getView().findViewById(R.id.score_id);
        textView.setText(newScore);
    }

    //final TextView TVuserName = (TextView) getView().findViewById(R.id.user_name_id);
    //final String userName = TVuserName.getText().toString();

    //Edit Info
    public void btnEditUserName(View view)
    {
        Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setMessage(R.string.edit_your_informations);
        alert.setTitle(R.string.edit_your_user_name);
        alert.setView(edittext);

        alert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newUserName = edittext.getText().toString();
                if (newUserName.isEmpty() || newUserName.length() < 3) {
                    edittext.setError(String.valueOf(R.string.error_user_name));
                }
                else{
                    //update it in database
                    JSONObject updateUserNameRequest = new JSONObject();
                    try {
                        updateUserNameRequest.put("cmd" , "updateUserName");
                        updateUserNameRequest.put("newuserName" , newUserName);
                        //updateUserNameRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updateUserNameRequest.toString());
                    //update it in textview in fragment
                    //TVuserName.setText(newUserName);
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
        Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(context);

        final EditText edittext = new EditText(context);
        alert.setMessage(R.string.edit_your_informations);
        alert.setTitle(R.string.edit_your_password);
        TextView TVPassword = (TextView) getView().findViewById(R.id.pw_id);
        final String password = TVPassword.getText().toString();
        alert.setView(edittext);

        alert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newPassword = edittext.getText().toString();
                if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
                    edittext.setError(String.valueOf(R.string.error_password));
                }
                else{
                    //update it in database
                    JSONObject updatePasswordRequest = new JSONObject();
                    try {
                        updatePasswordRequest.put("cmd" , "updatePassword");
                        updatePasswordRequest.put("newPassword" , newPassword);
                        //updatePasswordRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updatePasswordRequest.toString());
                    //update it in textview in fragment
                    //TVuserName.setText(newPassword);
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
        Context context = FragmentProfile.this.getActivity();
        AlertDialog.Builder alert  = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setMessage(R.string.edit_your_informations);
        alert.setTitle(R.string.edit_your_email);
        TextView TVEmail = (TextView) getView().findViewById(R.id.email_id);
        final String email = TVEmail.getText().toString();
        alert.setView(edittext);

        alert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = edittext.getText();
                //OR
                String newEmail = edittext.getText().toString();
                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edittext.setError(String.valueOf(R.string.error_email));
                }
                    else{
                    //update it in database
                    JSONObject updateEmailRequest = new JSONObject();
                    try {
                        updateEmailRequest.put("cmd" , "updateEmail");
                        updateEmailRequest.put("newEmail" , newEmail);
                        //updateEmailRequest.put("userName",userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mySocket.mWebSocketClient.send(updateEmailRequest.toString());
                    //update it in textview in fragment
                    //TVuserName.setText(newEmail);
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

    //End Edit Info

}
