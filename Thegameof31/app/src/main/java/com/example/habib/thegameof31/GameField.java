package com.example.habib.thegameof31;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class GameField extends AppCompatActivity {

    private TextView TVsum , TV3 , TV4;
    private int sum = 0;
    private int limit;
    private int stageNumber;
    private String stageName;
    private int modePos;
    private VirtualPlayer virtualPlayer;
    private boolean turnOnComputer;
    private boolean playerWin;
    private Button btns[][];
    private int data[];

    public static final String playerWinKey = "playerWinKey";
    private static final String keySum = "keySum";


    private Handler handlerDisplaySum = new Handler(){
        public void handleMessage(Message msg){
            TVsum.setText(""+sum);
        }
    };

    private Handler handlerApplyColors = new Handler(){
        public void handleMessage(Message msg){
            getSupportActionBar().setTitle("31-"+stageName);

            Context c = getBaseContext();
            switch (stageNumber){
                case 0:
                    applyColors(ContextCompat.getColor(c, R.color.cyan) , ContextCompat.getColor(c, R.color.lightpink));
                    break;
                case 1:
                    applyColors(ContextCompat.getColor(c, R.color.lightpink) , ContextCompat.getColor(c, R.color.cyan));
                    break;
                case 2:
                    applyColors(ContextCompat.getColor(c, R.color.lightgreen) , ContextCompat.getColor(c, R.color.bashari));
                    break;
                case 3:
                    applyColors(ContextCompat.getColor(c, R.color.bashari) , ContextCompat.getColor(c, R.color.lightgreen));
                    break;
                case 4:
                    applyColors(ContextCompat.getColor(c, R.color.yellow) , ContextCompat.getColor(c, R.color.afani));
                    break;
                case 5:
                    applyColors(ContextCompat.getColor(c, R.color.afani) , ContextCompat.getColor(c, R.color.yellow));
                    break;
                case 6:
                    applyColors(ContextCompat.getColor(c, R.color.green) , ContextCompat.getColor(c, R.color.blue));
                    break;
                case 7:
                    applyColors(ContextCompat.getColor(c, R.color.blue) , ContextCompat.getColor(c, R.color.green));
                    break;
                case 8:
                    applyColors(ContextCompat.getColor(c, R.color.orange) , ContextCompat.getColor(c, R.color.red));
                    break;
                case 9:
                    applyColors(ContextCompat.getColor(c, R.color.red) , ContextCompat.getColor(c, R.color.orange));
                    break;

                case 10:
                    applyColors(ContextCompat.getColor(c, R.color.darkblue) , ContextCompat.getColor(c, R.color.darkpurple));
                    break;
                case 11:
                    applyColors(ContextCompat.getColor(c, R.color.darkpurple) , ContextCompat.getColor(c, R.color.darkblue));
                    break;
                case 12:
                    applyColors(ContextCompat.getColor(c, R.color.darkgreen) , ContextCompat.getColor(c, R.color.darkorange));
                    break;
                case 13:
                    applyColors(ContextCompat.getColor(c, R.color.darkorange) , ContextCompat.getColor(c, R.color.darkgreen));
                    break;
                case 14:
                    applyColors(ContextCompat.getColor(c, R.color.blackyred) , ContextCompat.getColor(c, R.color.blackycyan));
                    break;
                case 15:
                    applyColors(ContextCompat.getColor(c, R.color.blackycyan) , ContextCompat.getColor(c, R.color.blackyred));
                    break;
                case 16:
                    applyColors(ContextCompat.getColor(c, R.color.wine) , ContextCompat.getColor(c, R.color.purple));
                    break;
                case 17:
                    applyColors(ContextCompat.getColor(c, R.color.purple) , ContextCompat.getColor(c, R.color.wine));
                    break;
                case 18:
                    applyColors(ContextCompat.getColor(c, R.color.move) , ContextCompat.getColor(c, R.color.gray));
                    break;
                case 19:
                    applyColors(ContextCompat.getColor(c, R.color.gray) , ContextCompat.getColor(c, R.color.move));                    break;
                case 20:
                    applyColors(ContextCompat.getColor(c, R.color.darkred) , ContextCompat.getColor(c, R.color.black));
                    break;
                case 21:
                    applyColors(ContextCompat.getColor(c, R.color.black) , ContextCompat.getColor(c, R.color.darkred));
                    break;
            }
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

    private Runnable displaySumThread = new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(500);
            } catch(InterruptedException e){
            }
            handlerDisplaySum.sendMessage(new Message());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_field);
        setScreenSize();
        prepareButtons();
        data = new int [7];

        TV3 = (TextView) findViewById(R.id.TV3);
        TV4 = (TextView) findViewById(R.id.TV4);

        Thread t = new Thread(applyColorsThread);
        t.start();



        playerWin = false;
        TVsum = (TextView) findViewById(R.id.TVsum);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stageName = getIntent().getStringExtra(MainActivity.stageNameKey);
        stageNumber = getIntent().getIntExtra(MainActivity.stageNumberKey , 0);
        //get modePos
        modePos = getIntent().getIntExtra(ModesActivity.modePosKey,0);
        //according to mode pos we should fill the data array
        //and prepare the buttons
        for (int i = 1 ; i < 7 ; i++)
            data [i] = 4;

        switch(modePos)
        {
            case 0 :
            {
                break;
            }
            case 1 :
            {
                data[1] = 0; delNbr(1);
                break;
            }
            case 2:
            {
                data[2] = 0; delNbr(2);
                break;
            }
            case 3 :
            {
                data[3] = 0; delNbr(3);
                break;
            }
            case 4:
            {
                data[4] = 0; delNbr(4);
                break;
            }
            case 5:
            {
                data[5] = 0; delNbr(5);
                break;
            }
            case 6:
            {
                data[6] = 0; delNbr(6);
                break;
            }
            case 7 :
            {
                data[1] = 0; delNbr(1);
                data[2] = 0; delNbr(2);
                break;
            }
            case 8:
            {
                data[1] = 0; delNbr(1);
                data[3] = 0; delNbr(3);
                break;
            }
            case 9 :
            {
                data[1] = 0; delNbr(1);
                data[4] = 0; delNbr(4);
                break;
            }
            case 10:
            {
                data[1] = 0; delNbr(1);
                data[5] = 0; delNbr(5);
                break;
            }
            case 11:
            {
                data[1] = 0; delNbr(1);
                data[6] = 0; delNbr(6);
                break;
            }
            case 12:
            {
                data[2] = 0; delNbr(2);
                data[3] = 0; delNbr(3);
                break;
            }
            case 13 :
            {
                data[2] = 0; delNbr(2);
                data[4] = 0; delNbr(4);
                break;
            }
            case 14 :
            {
                data[2] = 0; delNbr(2);
                data[5] = 0; delNbr(5);
                break;
            }
            case 15:
            {
                data[2] = 0; delNbr(2);
                data[6] = 0; delNbr(6);
                break;
            }
            case 16:
            {
                data[3] = 0; delNbr(3);
                data[4] = 0; delNbr(4);
                break;
            }
            case 17 :
            {
                data[3] = 0; delNbr(3);
                data[5] = 0; delNbr(5);
                break;
            }
            case 18:
            {
                data[3] = 0; delNbr(3);
                data[6] = 0; delNbr(6);
                break;
            }
            case 19 :
            {
                data[4] = 0; delNbr(4);
                data[5] = 0; delNbr(5);
                break;
            }
            case 20 :
            {
                data[4] = 0; delNbr(4);
                data[6] = 0; delNbr(6);
                break;
            }
            case 21:
            {
                data[5] = 0; delNbr(5);
                data[6] = 0; delNbr(6);
                break;
            }
        }
        limit = stageNumber + 1;
        if (limit % 2 == 0) {
            turnOnComputer = true;
            limit = 30 - 3 * (limit - 1) / 2;

        }
        else{
            turnOnComputer = false;
            limit = 30 - 3*limit/2;
        }
        virtualPlayer = new VirtualPlayer(limit , data);
        if (turnOnComputer)
            playComputerTurn();

    }

    private void delNbr(int nbr)
    {
        for (int i = 0 ; i < 4 ; i++)
            btns[nbr-1][i].setText("");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setScreenSize();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_field, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        item.setTitle("habib");
        if (id == android.R.id.home)
            moveToStages();

        return super.onOptionsItemSelected(item);
    }

    private void prepareButtons()
    {
        btns = new Button [6][4];
        btns[0][0] = (Button)findViewById(R.id.btn1_1);
        btns[0][1] = (Button)findViewById(R.id.btn2_1);
        btns[0][2] = (Button)findViewById(R.id.btn3_1);
        btns[0][3] = (Button)findViewById(R.id.btn4_1);
        btns[1][0] = (Button)findViewById(R.id.btn1_2);
        btns[1][1] = (Button)findViewById(R.id.btn2_2);
        btns[1][2] = (Button)findViewById(R.id.btn3_2);
        btns[1][3] = (Button)findViewById(R.id.btn4_2);
        btns[2][0] = (Button)findViewById(R.id.btn1_3);
        btns[2][1] = (Button)findViewById(R.id.btn2_3);
        btns[2][2] = (Button)findViewById(R.id.btn3_3);
        btns[2][3] = (Button)findViewById(R.id.btn4_3);
        btns[3][0] = (Button)findViewById(R.id.btn1_4);
        btns[3][1] = (Button)findViewById(R.id.btn2_4);
        btns[3][2] = (Button)findViewById(R.id.btn3_4);
        btns[3][3] = (Button)findViewById(R.id.btn4_4);
        btns[4][0] = (Button)findViewById(R.id.btn1_5);
        btns[4][1] = (Button)findViewById(R.id.btn2_5);
        btns[4][2] = (Button)findViewById(R.id.btn3_5);
        btns[4][3] = (Button)findViewById(R.id.btn4_5);
        btns[5][0] = (Button)findViewById(R.id.btn1_6);
        btns[5][1] = (Button)findViewById(R.id.btn2_6);
        btns[5][2] = (Button)findViewById(R.id.btn3_6);
        btns[5][3] = (Button)findViewById(R.id.btn4_6);
    }


    private void checkEnd()
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        boolean endGame = false;
        if (sum > 31)
        {
            endGame = true;
            if (turnOnComputer)
            {
                dlgAlert.setMessage("You win !!");
                playerWin = true;
                dlgAlert.setTitle("Congratulations");
            }
            else
            {
                dlgAlert.setMessage("You can make it, try again !");
                dlgAlert.setTitle("Game over");
            }
        }
        if (!endGame)
            return;
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        moveToStages();
                    }
                });
        dlgAlert.create().show();
    }

    private void moveToStages()
    {
        Intent i = getIntent();
        i.putExtra(MainActivity.stageNumberKey , stageNumber);
        i.putExtra(playerWinKey , playerWin);
        setResult(RESULT_OK , i);
        finish();
    }

    private void playPlayerTurn(View view , int i)  {
        if (turnOnComputer)
            return;
        Button btn = (Button) view;
        if (btn.getText().toString() == "")
            return;
        btn.setText("");
        //view.setVisibility(View.INVISIBLE);
        data[i]--;
        sum += i;
        TVsum.setText("" + sum);
        checkEnd();
        turnOnComputer = true;
        if (sum <= 31)
            playComputerTurn();

    }

    private void playComputerTurn()
    {
        int move = virtualPlayer.getProperMove(sum , data);
        sum += move;
        data[move]--;
        boolean done = false;
        Random rand = new Random();
        while (!done)
        {
            int btn = rand.nextInt(4);
            if (btns[move-1][btn].getText().toString() != "")
            {
                done = true;
                btns[move-1][btn].setText("");
            }
        }
        Thread t = new Thread(displaySumThread);
        t.start();
        Toast.makeText(this , ""+move , Toast.LENGTH_SHORT).show();
        checkEnd();
        turnOnComputer = false;
    }

    public void handlerBtn2_1(View view) {
        playPlayerTurn(view , 1);
    }

    public void handlerBtn2_2(View view) {
        playPlayerTurn(view , 2);
    }

    public void handlerBtn2_3(View view) {
        playPlayerTurn(view , 3);
    }

    public void handlerBtn2_4(View view) {
        playPlayerTurn(view , 4);
    }

    public void handlerBtn2_5(View view) {
        playPlayerTurn(view , 5);
    }

    public void handlerBtn2_6(View view) {
        playPlayerTurn(view , 6);
    }

    public void handlerBtn1_1(View view) {
        playPlayerTurn(view , 1);
    }

    public void handlerBtn1_2(View view) {
        playPlayerTurn(view , 2);
    }

    public void handlerBtn1_3(View view) {
        playPlayerTurn(view , 3);
    }

    public void handlerBtn1_4(View view) {
        playPlayerTurn(view , 4);
    }

    public void handlerBtn1_5(View view) {
        playPlayerTurn(view , 5);
    }

    public void handlerBtn1_6(View view) {
        playPlayerTurn(view , 6);
    }

    public void handlerBtn3_1(View view) {
        playPlayerTurn(view , 1);
    }

    public void handlerBtn3_2(View view) {
        playPlayerTurn(view , 2);
    }

    public void handlerBtn3_3(View view) {
        playPlayerTurn(view , 3);
    }

    public void handlerBtn3_4(View view) {
        playPlayerTurn(view , 4);
    }

    public void handlerBtn3_5(View view) {
        playPlayerTurn(view , 5);
    }

    public void handlerBtn3_6(View view) {
        playPlayerTurn(view , 6);
    }

    public void handlerBtn4_1(View view) {
        playPlayerTurn(view , 1);
    }

    public void handlerBtn4_2(View view) {
        playPlayerTurn(view , 2);
    }

    public void handlerBtn4_3(View view) {
        playPlayerTurn(view , 3);
    }

    public void handlerBtn4_4(View view) { playPlayerTurn(view , 4); }

    public void handlerBtn4_5(View view) {
        playPlayerTurn(view , 5);
    }

    public void handlerBtn4_6(View view) {
        playPlayerTurn(view , 6);
    }


}
