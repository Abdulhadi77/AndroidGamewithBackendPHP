package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by HABIB on 5/11/2016.
 */
public class mySocket
{
    public static WebSocketClient mWebSocketClient ;
    private static URI uri ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static String userNameSender;
    public static String lastMsg;
    public static Context context;
    public static SharedPreferences sharedpreferences;
    public static MySharedPreferences prefs;
    public static boolean myTurn = false;
    public static  boolean firstTurn = false;
    public static int nbrWins = 0;


    public static void init()
    {
        try{
            uri = new URI("ws://10.0.2.2:8080");
            //uri = new URI("ws://192.168.92.1:8080");
        } catch (URISyntaxException e) {
            Log.i("Websocket", "error on uri");
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) {
                JSONObject read;

                try {
                    read = new JSONObject(s);
                    Log.i("Websocket",read.getString("cmd"));
                    String cmd = read.getString("cmd");

                    if (cmd.equals("loginRequest"))
                    {
                        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        prefs = MySharedPreferences.getInstance(context,MyPREFERENCES); //provide context & preferences name.
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse){

                            LoginActivity.showInMsgBox = String.valueOf(R.string.login_success);

                            String email = read.getString("email");
                            String userName = read.getString("userName");
                            String pw = read.getString("pw");
                            int score = read.getInt("score");

                            lastLevelAndWinLevelsArray.hasAccount = true;
                            lastLevelAndWinLevelsArray.userNameForKeepLogin = userName;
                            //SharedPreferences.Editor editor = sharedpreferences.edit();
                            prefs.putBoolean("hasAccount",lastLevelAndWinLevelsArray.hasAccount);
                            prefs.putString("userNameForKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);
                            prefs.putInt("score",lastLevelAndWinLevelsArray.score);
                            prefs.commit();

                            Intent intent = new Intent(context,ProfileActivity.class);
                            intent.putExtra("email",email);
                            intent.putExtra("userName",userName);
                            intent.putExtra("pw",pw);
                            intent.putExtra("score",score);
                            intent.putExtra("comeFrom","login");

                            context.startActivity(intent);
                        }
                        else{
                            LoginActivity.showInMsgBox = context.getResources().getString(R.string.login_failed);
                            LoginActivity.handlerMsgBox.sendMessage(new Message());
                        }
                    }
                    else if (cmd.equals("signupReponse"))
                    {
                        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        prefs = MySharedPreferences.getInstance(context,MyPREFERENCES); //provide context & preferences name.
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse) {
                            SignupActivity.showInMsgBox = context.getResources().getString(R.string.signup_success);

                            lastLevelAndWinLevelsArray.hasAccount = true;
                            String userName = read.getString("userName");
                            String pw = read.getString("pw");
                            String email = read.getString("email");
                            int score = read.getInt("score");

                            lastLevelAndWinLevelsArray.hasAccount = true;
                            lastLevelAndWinLevelsArray.userNameForKeepLogin = userName;
                            prefs.putBoolean("hasAccount",lastLevelAndWinLevelsArray.hasAccount);
                            prefs.putString("userNameForKeepLogin",lastLevelAndWinLevelsArray.userNameForKeepLogin);
                            prefs.putInt("score",lastLevelAndWinLevelsArray.score);
                            prefs.commit();

                            Intent intent = new Intent(context,ProfileActivity.class);
                            intent.putExtra("email",email);
                            intent.putExtra("userName",userName);
                            intent.putExtra("pw",pw);
                            intent.putExtra("score",score);
                            intent.putExtra("comeFrom","signup");
                            context.startActivity(intent);
                        }
                        else
                            SignupActivity.showInMsgBox = context.getResources().getString(R.string.signup_failed);
                            SignupActivity.handlerMsgBox.sendMessage(new Message());

                    }
                    else if (cmd.equals("connectionRequest"))
                    {
                        firstTurn = false;
                        nbrWins = 0;
                        Log.i("Web", "enter to connectionRequest block");
                        ProfileActivity.idSender = read.getInt("idSender");
                        lastLevelAndWinLevelsArray.userNameForKeepLogin = read.getString("userNameSender");
                        userNameSender = read.getString("userNameSender");
                        ProfileActivity.connectionRequestHandler.sendMessage(new Message());
                        Log.i("Web", "connectionRequest after handler SENG MESSAGE");
                    }
                    else if (cmd.equals("connectionReponse"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse) {
                            myTurn = true;
                            firstTurn = true;
                            nbrWins = 0;
                            Intent i = new Intent(context , GameFieldOnline.class);
                            ProfileActivity.idSender = read.getInt("idSender");
                            context.startActivity(i);
                        }
                        else{
                            if(read.getBoolean("receiverIsOffline")){
                                ProfileActivity.showInMsgBox = " is offline !";
                                ProfileActivity.handlerMsgBox.sendMessage(new Message());
                            }else if (read.getBoolean("receiverIsBusy")){
                                ProfileActivity.showInMsgBox = " "+context.getResources().getString(R.string.playeing_with_other_player);
                                ProfileActivity.handlerMsgBox.sendMessage(new Message());
                            }else if (read.getBoolean("didNotAccept")){
                                ProfileActivity.showInMsgBox = " "+context.getResources().getString(R.string.didnot_accept_play);
                                ProfileActivity.handlerMsgBox.sendMessage(new Message());
                            }
                        }

                    }
                    else if (cmd.equals("Move"))
                    {
                        Log.i("Web","Move : enter block");
                        lastMsg = read.getString("msg");
                        Log.i("Web","Move : get data");
                        //ChatActivity.msgViewHandler.sendMessage(new Message());
                        Log.i("Web", "Move : edit view");
                    }
                    else if (cmd.equals("updateUserName"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse) {
                            String newUserName = read.getString("newUserName");
                            lastLevelAndWinLevelsArray.userNameForKeepLogin = newUserName;
                            ProfileActivity.showInMsgBox = context.getResources().getString(R.string.edited);
                        }
                        else
                                ProfileActivity.showInMsgBox = String.valueOf(R.string.try_later_again);
                    }
                    else if (cmd.equals("updatePassword"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse)
                            ProfileActivity.showInMsgBox = context.getResources().getString(R.string.edited);
                        else
                            ProfileActivity.showInMsgBox = context.getResources().getString(R.string.try_later_again);
                    }
                    else if (cmd.equals("updateEmail"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse)
                            ProfileActivity.showInMsgBox = context.getResources().getString(R.string.edited);
                        else
                            ProfileActivity.showInMsgBox = context.getResources().getString(R.string.try_later_again);

                    }
                    else if(cmd.equals("updateScore"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse) {
                            sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            prefs = MySharedPreferences.getInstance(context,MyPREFERENCES); //provide context & preferences name.
                            prefs = MySharedPreferences.getInstance(context,MyPREFERENCES); //provide context & preferences name.
                            lastLevelAndWinLevelsArray.score = prefs.getInt("score",0);

                            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
                            dlgAlert.setTitle(R.string.congratulations);
                            dlgAlert.setMessage(context.getResources().getString(R.string.your_new_score) +" "+ lastLevelAndWinLevelsArray.score +" !!");
                            dlgAlert.setPositiveButton(R.string.ok_dialog, null);
                            dlgAlert.setCancelable(false);
                            dlgAlert.setPositiveButton(R.string.ok_dialog,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                    });
                            dlgAlert.create().show();

                        }
                    }
                    else if (cmd.equals("logoutRequest"))
                    {
                        boolean reponse = read.getBoolean("reponse");
                        if (!reponse)
                            Toast.makeText(context, context.getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    }
                    else if (cmd.equals("searchRandomPlayerRequest"))
                    {
                        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        prefs = MySharedPreferences.getInstance(context,MyPREFERENCES); //provide context & preferences name.
                        boolean reponse = read.getBoolean("reponse");
                        if (reponse){
                            String player2 = read.getString("userNameOfRandomPlayer");
                            int scoreOfRandomPlayer = read.getInt("scoreOfRandomPlayer");
                            lastLevelAndWinLevelsArray.player2 = player2;
                            lastLevelAndWinLevelsArray.scoreOfRandomPlayer = scoreOfRandomPlayer;
                            prefs.putString("userNameOfRandomPlayer",lastLevelAndWinLevelsArray.player2);
                            prefs.putInt("scoreOfRandomPlayer",lastLevelAndWinLevelsArray.scoreOfRandomPlayer);
                            prefs.commit();
                        }
                    }
                    else if (cmd.equals("endGame"))
                    {
                                                        //GameFieldOnline.showInMsgBox = "Oops, please try later again";
                                                        //GameFieldOnline.handlerMsgBox.sendMessage(new Message());
                    }
                    else if (cmd.equals("connectionReponseGameOnline"))
                    {
                        myTurn = !myTurn;
                        GameFieldOnline.btnI = read.getInt("btnI");
                        GameFieldOnline.btnJ = read.getInt("btnJ");
                        Log.i("WEB:","recieve : " + GameFieldOnline.btnI+ " " + String.valueOf(GameFieldOnline.btnJ));
                        GameFieldOnline.userNamePlayerPlayed = read.getString("userNamePlayerPlayed");
                        GameFieldOnline.endFirstHalfForWebSocket = read.getBoolean("endFirstHalf");
                        GameFieldOnline.endSecondHalf = read.getBoolean("endSecondHalf");
                        GameFieldOnline.handlerMsgBox.sendMessage(new Message());
                    }
                    else if (cmd.equals("connectionRequestEscape"))
                    {
                        GameFieldOnline.showInMsgBox = read.getString("userNamePlayerEscaped")
                                + context.getResources().getString(R.string.is_escaped_from_this_game);
                        GameFieldOnline.handlerMsgBox.sendMessage(new Message());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}
