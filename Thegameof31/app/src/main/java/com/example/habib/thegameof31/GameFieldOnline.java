package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class GameFieldOnline extends AppCompatActivity {

    public SharedPreferences sharedpreferences;
    public static MySharedPreferences prefs;
    public static final String MyPREFERENCES = "MySavedData";
    public static final String FirstHalf = "First Half";
    public static final String SecondHalf = "Second Half";
    public static String userNameOfPlayer1;
    public static String userNameOfPlayer2;
    public int scoreOfPlayer1;
    public int scoreOfPlayer2;
    //public boolean half = false;
    public static String showInMsgBox= " ";
    static boolean endGame;
    public static boolean endHalf;
    public static String userNameForKeepLogin;
    public static boolean secondHalf = false;
    public static boolean player2Turn = false;
    public static boolean player1Turn = true;
    public static boolean endFirstHalfForWebSocket = false;
    public static String userNamePlayerPlayed=" ";
    public static TextView tvHalf;
    public static TextView tvPlayerTurn;
    public static boolean endFirstHalf = false;
    public static boolean endSecondHalf = false;

    private static TextView TVsum;
    private TextView TV3 , TV4;
    public static int sum = 0;
    public static Context gameFieldContext;
    public static int btnIdPressed = 0;
    //private int limit;
    private int stageNumber;
    private String stageName;
    //private VirtualPlayer virtualPlayer;
    //private boolean turnOnComputer;
    //private static int player2Win = 0;
    //private static int player1Win = 0;
    public static int playerWin = 0;
    private static Button btns[][];
    public int btnsId[][];
    //private int data[];
    public static int btnI, btnJ;

    //public static boolean myTurn;



    public static final String playerWinKey = "playerWinKey";
    private static final String keySum = "keySum";


    public static Handler handlerMsg = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameFieldContext);
            dlgAlert.setTitle(R.string.game_name);
            dlgAlert.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    moveToProfileActivity();
                }
            });
            dlgAlert.setCancelable(true);
            dlgAlert.setMessage(showInMsgBox);
            dlgAlert.create().show();
        }
    };


    public static Handler handlerMsgBox = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameFieldContext);


                tvPlayerTurn.setText(lastLevelAndWinLevelsArray.userNameForKeepLogin);

            //setButtonsEnabled();
            btns[btnI][btnJ].setText("");
            sum += (btnI+1);
            TVsum.setText(sum+"");


            //Test in  player's activity who win
            if (endFirstHalfForWebSocket){
                // sum > 31
                endHalf = true;
                //the name which in textviewplayerturn is win and is the same of userNameForKeepLogin
                if (lastLevelAndWinLevelsArray.userNameForKeepLogin.equals(userNameOfPlayer1)) {
                    dlgAlert.setMessage(userNameOfPlayer1 + ",Winner of the frist half! ");
                    //player1Win++;
                    playerWin++;
                }
                else {
                    dlgAlert.setMessage(userNameOfPlayer2 + ",Winner of the frist half! ");
                    //player2Win++;
                    playerWin++;
                }

                endFirstHalfForWebSocket = false;
            }
            else if (endSecondHalf){
                endHalf = true;
                endSecondHalf = true;
                if (tvPlayerTurn.getText().toString().equals(lastLevelAndWinLevelsArray.userNameForKeepLogin)) {
                    if (lastLevelAndWinLevelsArray.userNameForKeepLogin.equals(userNameOfPlayer1)) {
                        dlgAlert.setMessage(userNameOfPlayer1 + "," + ",Winner of the second half! ");
                        //player1Win++;
                        playerWin++;
                    } else {
                        dlgAlert.setMessage(userNameOfPlayer2 + "," + ",Winner of the second half! ");
                        //player2Win++;
                        playerWin++;
                    }
                }
                dlgAlert.setTitle(R.string.congratulations);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
            if (!endHalf) {
                return;
            }
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(R.string.ok_dialog,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //end first half
                            if (secondHalf == false)
                                secondeHalf();
                                //end second half
                            else {
                                finalResult();
                            }
                        }
                    });
            dlgAlert.create().show();

        }
    };



    private Handler handlerApplyColors = new Handler(){
        public void handleMessage(Message msg){
            getSupportActionBar().setTitle("31- Online");
            Context c = getBaseContext();
            applyColors(ContextCompat.getColor(c, R.color.green), ContextCompat.getColor(c, R.color.blue));

        }
    };


    private void applyColors(int color1 , int color2){

        for (int i = 0 ; i < 6 ; i++)
        {
            btns[i][0].setBackgroundColor(color1);
            btns[i][2].setBackgroundColor(color1);
            btns[i][1].setBackgroundColor(color2);
            btns[i][3].setBackgroundColor(color2);
        }
        TV3.setBackgroundColor(color2);
        TV4.setBackgroundColor(color1);
    }


    private Runnable applyColorsThread = new Runnable() {
        @Override
        public void run() {
            handlerApplyColors.sendMessage(new Message());
        }
    };


    private void setScreenSize()
    {
        View v =  findViewById(R.id.linear1);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float height = 0.6f * size.y;
        if (height < 350)
            height = 350;
        v.getLayoutParams().height = (int)height;
    }



    private void prepareButtons()
    {
        btns = new Button [6][4];
        btns[0][0] = (Button)findViewById(R.id.btn1_1Online);
        btns[0][1] = (Button)findViewById(R.id.btn2_1Online);
        btns[0][2] = (Button)findViewById(R.id.btn3_1Online);
        btns[0][3] = (Button)findViewById(R.id.btn4_1Online);
        btns[1][0] = (Button)findViewById(R.id.btn1_2Online);
        btns[1][1] = (Button)findViewById(R.id.btn2_2Online);
        btns[1][2] = (Button)findViewById(R.id.btn3_2Online);
        btns[1][3] = (Button)findViewById(R.id.btn4_2Online);
        btns[2][0] = (Button)findViewById(R.id.btn1_3Online);
        btns[2][1] = (Button)findViewById(R.id.btn2_3Online);
        btns[2][2] = (Button)findViewById(R.id.btn3_3Online);
        btns[2][3] = (Button)findViewById(R.id.btn4_3Online);
        btns[3][0] = (Button)findViewById(R.id.btn1_4Online);
        btns[3][1] = (Button)findViewById(R.id.btn2_4Online);
        btns[3][2] = (Button)findViewById(R.id.btn3_4Online);
        btns[3][3] = (Button)findViewById(R.id.btn4_4Online);
        btns[4][0] = (Button)findViewById(R.id.btn1_5Online);
        btns[4][1] = (Button)findViewById(R.id.btn2_5Online);
        btns[4][2] = (Button)findViewById(R.id.btn3_5Online);
        btns[4][3] = (Button)findViewById(R.id.btn4_5Online);
        btns[5][0] = (Button)findViewById(R.id.btn1_6Online);
        btns[5][1] = (Button)findViewById(R.id.btn2_6Online);
        btns[5][2] = (Button)findViewById(R.id.btn3_6Online);
        btns[5][3] = (Button)findViewById(R.id.btn4_6Online);
    }


    private void checkEnd()
    {

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        endGame = false;
        if (sum > 31)
        {
            if (secondHalf == false)
            {
                endHalf = true;
                endFirstHalfForWebSocket = true;
                if (mySocket.myTurn) {
                    dlgAlert.setMessage("loser of the first half! ");
                } else {
                    dlgAlert.setMessage(userNameOfPlayer2 + ","  + "loser of the first half! ");
                    mySocket.nbrWins++;
                }
                dlgAlert.setTitle(R.string.Unfor);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(R.string.ok_dialog,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                secondeHalf();
                            }
                        });
                dlgAlert.create().show();

            }
            else
            {
                endHalf = true;
                endSecondHalf = true;

                //busy = 0
                JSONObject updateUserNameRequest = new JSONObject();
                try {
                    updateUserNameRequest.put("cmd", "endGame");
                    updateUserNameRequest.put("userNameOfPlayer1", mySocket.userNameSender);
                    updateUserNameRequest.put("userNameOfPlayer2", userNameForKeepLogin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mySocket.mWebSocketClient.send(updateUserNameRequest.toString());
                finalResult();
            }
        }

    }


    private void playPlayerTurn(View view , int i , int ii , int j)  {
        if (!mySocket.myTurn)
            return;
        TextView tvPlayerTurn = (TextView) findViewById(R.id.player_turn);
        Button btn = (Button) view;
        if (btn.getText().toString() == "")
            return;
        btn.setText("");
        sum += i;
        TVsum.setText("" + sum);

        checkEnd();

        JSONObject connectionRequestGameOnline = new JSONObject();
        try {
            connectionRequestGameOnline.put("cmd", "connectionRequestGameOnline");
            connectionRequestGameOnline.put("btnI" , ii);
            connectionRequestGameOnline.put("btnJ", j);

            connectionRequestGameOnline.put("idReceiver",ProfileActivity.idSender);
            connectionRequestGameOnline.put("userNamePlayerPlayed",lastLevelAndWinLevelsArray.userNameForKeepLogin);
            connectionRequestGameOnline.put("endFirstHalf" , endFirstHalfForWebSocket);
            connectionRequestGameOnline.put("endSecondHalf" , endSecondHalf);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mySocket.mWebSocketClient.send(connectionRequestGameOnline.toString());
        mySocket.myTurn = false;
        endFirstHalfForWebSocket = false;

    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_field_online);
        player1Turn = true;
        player2Turn = false;
        endHalf = false;
        gameFieldContext = this;
        mySocket.context = this;
       // btnsId = new int [6][4];
        sum = 0;
        setScreenSize();
        prepareButtons();
        TV3 = (TextView) findViewById(R.id.TV3);
        TV4 = (TextView) findViewById(R.id.TV4);

        Thread t = new Thread(applyColorsThread);
        t.start();

        //playerWin = false;
        TVsum = (TextView) findViewById(R.id.TVsumOnline);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();


        //get user name and score of tow players
        prefs = MySharedPreferences.getInstance(this, MyPREFERENCES); //provide context & preferences name.
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        lastLevelAndWinLevelsArray.player1 = prefs.getString("userNameOfPlayer1"," ");
        userNameOfPlayer1 = lastLevelAndWinLevelsArray.player1;
        lastLevelAndWinLevelsArray.player2 = prefs.getString("userNameOfRandomPlayer",null);
        userNameOfPlayer2 = lastLevelAndWinLevelsArray.player2;
        lastLevelAndWinLevelsArray.scorePlayer1 = prefs.getInt("scoreOfPlayer1",0);
        scoreOfPlayer1 = lastLevelAndWinLevelsArray.scorePlayer1;
        lastLevelAndWinLevelsArray.scoreOfRandomPlayer = prefs.getInt("scoreOfRandomPlayer",0);
        scoreOfPlayer2 = lastLevelAndWinLevelsArray.scoreOfRandomPlayer;
        userNameForKeepLogin = prefs.getString("userNameForKeepLogin",null);
        lastLevelAndWinLevelsArray.userNameForKeepLogin = prefs.getString("userNameForKeepLogin",null);
        lastLevelAndWinLevelsArray.score = prefs.getInt("score",0);
        Log.i("plaer 1", userNameOfPlayer1);

        Log.i("score 1", String.valueOf(scoreOfPlayer1));

        Log.i("player2", userNameOfPlayer2);

        Log.i("score 2", String.valueOf(scoreOfPlayer2));

        //in the begginning turn player1
        //First Half (( int the end match of it we call secondHalf
        tvHalf = (TextView) findViewById(R.id.half);
        tvPlayerTurn = (TextView) findViewById(R.id.player_turn);
        tvHalf.setText(FirstHalf);
        if (player1Turn)
            tvPlayerTurn.setText(userNameOfPlayer1);
        else if (player2Turn)
            tvPlayerTurn.setText(userNameOfPlayer2);

    }


    public static void secondeHalf(){
        secondHalf = true;
        endFirstHalf = false;
        endHalf = false;
        sum = 0;
        TVsum.setText(sum+"");
        tvHalf.setText(SecondHalf);
        //set turns
        if (mySocket.firstTurn)
            mySocket.myTurn = false;
        else
            mySocket.myTurn = true;
        //reset the btns
        for (int i = 0 ; i < 6 ; i++)
            for (int j = 0 ; j < 4 ; j++)
                btns[i][j].setText(""+(i+1));

    }

    public static void finalResult(){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(gameFieldContext);
        dlgAlert.setTitle(R.string.final_result);
        if (mySocket.nbrWins == 1)
            dlgAlert.setMessage(R.string.equalizing);
        else if (mySocket.nbrWins == 2){
            lastLevelAndWinLevelsArray.score += 2;
            prefs = MySharedPreferences.getInstance(gameFieldContext,MyPREFERENCES); //provide context & preferences name.

            prefs.putInt("score", lastLevelAndWinLevelsArray.score);
            prefs.commit();
            //save it in database and make sure about score is edited when he is back to profile activity

            dlgAlert.setMessage(lastLevelAndWinLevelsArray.userNameForKeepLogin + " !, "+"You win in this game !");
            dlgAlert.setMessage(lastLevelAndWinLevelsArray.userNameForKeepLogin + " !, " + "Your new score is " + " " + lastLevelAndWinLevelsArray.score + " !!");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(R.string.ok_dialog,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            moveToProfileActivity();
                        }
                    });
            dlgAlert.create().show();
        }else {
            JSONObject updateUserNameRequest = new JSONObject();
            try {
                updateUserNameRequest.put("cmd" , "updateScore");
                updateUserNameRequest.put("userName" , mySocket.userNameSender);
                updateUserNameRequest.put("newScore",1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mySocket.mWebSocketClient.send(updateUserNameRequest.toString());
            dlgAlert.setMessage("Game finished!");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(R.string.ok_dialog,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            moveToProfileActivity();
                        }
                    });
            dlgAlert.create().show();

        }

    }

    public static void moveToProfileActivity(){
        sum = 0;
        Intent intent = new Intent(gameFieldContext,ProfileActivity.class);
        gameFieldContext.startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        JSONObject connectionRequestEscape = new JSONObject();
        try {
            connectionRequestEscape.put("cmd", "connectionRequestGameOnline");
            if (lastLevelAndWinLevelsArray.userNameForKeepLogin.equals(userNameOfPlayer1)) {
                connectionRequestEscape.put("userNameReceiver", userNameOfPlayer2);
            }
            else
            if (lastLevelAndWinLevelsArray.userNameForKeepLogin.equals(userNameOfPlayer2)) {
                connectionRequestEscape.put("userNameReceiver", userNameOfPlayer1);
            }
            connectionRequestEscape.put("userNamePlayerEscaped",lastLevelAndWinLevelsArray.userNameForKeepLogin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mySocket.mWebSocketClient.send(connectionRequestEscape.toString());
    }











    public void handlerBtn2_1(View view) {

        playPlayerTurn(view , 1,1,0);
    }

    public void handlerBtn2_2(View view) {
        playPlayerTurn(view , 2,1,1);
    }

    public void handlerBtn2_3(View view) {
        playPlayerTurn(view , 3,1,2);
    }

    public void handlerBtn2_4(View view) {
        playPlayerTurn(view , 4,1,3);
    }

    public void handlerBtn2_5(View view) {
        playPlayerTurn(view , 5,1,4);
    }

    public void handlerBtn2_6(View view) {
        playPlayerTurn(view , 6,1,5);
    }

    public void handlerBtn1_1(View view) {
        playPlayerTurn(view , 1,0,0);
    }

    public void handlerBtn1_2(View view) {
        playPlayerTurn(view , 2,0,1);
    }

    public void handlerBtn1_3(View view) {
        playPlayerTurn(view , 3,0,2);
    }

    public void handlerBtn1_4(View view) {
        playPlayerTurn(view , 4,0,3);
    }

    public void handlerBtn1_5(View view) {
        playPlayerTurn(view , 5,0,4);
    }

    public void handlerBtn1_6(View view) {
        playPlayerTurn(view , 6,0,5);
    }

    public void handlerBtn3_1(View view) {
        playPlayerTurn(view , 1,2,0);
    }

    public void handlerBtn3_2(View view) {
        playPlayerTurn(view , 2,2,1);
    }

    public void handlerBtn3_3(View view) {
        playPlayerTurn(view , 3,2,2);
    }

    public void handlerBtn3_4(View view) {
        playPlayerTurn(view , 4,2,3);
    }

    public void handlerBtn3_5(View view) {
        playPlayerTurn(view , 5,2,4);
    }

    public void handlerBtn3_6(View view) {
        playPlayerTurn(view , 6,2,5);
    }

    public void handlerBtn4_1(View view) {
        playPlayerTurn(view , 1,3,0);
    }

    public void handlerBtn4_2(View view) {
        playPlayerTurn(view , 2,3,1);
    }

    public void handlerBtn4_3(View view) {
        playPlayerTurn(view , 3,3,2);
    }

    public void handlerBtn4_4(View view) { playPlayerTurn(view, 4, 3, 3); }

    public void handlerBtn4_5(View view) {
        playPlayerTurn(view, 5, 3, 4);
    }

    public void handlerBtn4_6(View view) {
        playPlayerTurn(view, 6, 3, 5);
    }


}