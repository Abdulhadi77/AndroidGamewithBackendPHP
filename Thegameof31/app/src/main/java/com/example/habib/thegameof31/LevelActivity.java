package com.example.habib.thegameof31;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class LevelActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String LastLevel = "lastLevel";
    public static final String Score = "score";
    public static final String WinLevelsArray = "winLevelsArray";
    //public SharedPreferences sharedpreferences;
    public MySharedPreferences prefs;
    public int modePos;

    public final static String stageNumberKey = "stageNumberKey";
    public final static String stageNameKey = "stageNameKey";
    private final static int request_code = 1995;
    GridView grid;
    CustomGrid adapter;
    GridView gridViewMode;
    CustomGridMode adapterMode;

    final String[] nameModes = {
            "Mode 1",
            "Mode 2",
            "Mode 3",
            "Mode 4",
            "Mode 5",
            "Mode 6",
            "Mode 7",
            "Mode 8",
            "Mode 9",
            "Mode 10",
            "Mode 11",
            "Mode 12",
            "Mode 13",
            "Mode 14",
            "Mode 15",
            "Mode 16",
            "Mode 17",
            "Mode 18",
            "Mode 19",
            "Mode 20",
            "Mode 21",
            "Mode 22"
    };
    int[] imageModesId = {
            R.drawable.mode_number_1,
            R.drawable.mode_number_2,
            R.drawable.mode_number_3,
            R.drawable.mode_number_4,
            R.drawable.mode_number_5,
            R.drawable.mode_number_6,
            R.drawable.mode_number_7,
            R.drawable.mode_number_8,
            R.drawable.mode_number_9,
            R.drawable.mode_number_10,
            R.drawable.mode_number_11,
            R.drawable.mode_number_12,
            R.drawable.mode_number_13,
            R.drawable.mode_number_14,
            R.drawable.mode_number_15,
            R.drawable.mode_number_16,
            R.drawable.mode_number_17,
            R.drawable.mode_number_18,
            R.drawable.mode_number_19,
            R.drawable.mode_number_20,
            R.drawable.mode_number_21,
            R.drawable.mode_number_22//22
    };

    final String[] nameLevel = {
            "Level 1",
            "Level 2",
            "Level 3",
            "Level 4",
            "Level 5",
            "Level 6",
            "Level 7",
            "Level 8",
            "Level 9",
            "Level 10",
            "Level 11",
            "Level 12",
            "Level 13",
            "Level 14",
            "Level 15",
            "Level 16",
            "Level 17",
            "Level 18",
            "Level 19",
            "Level 20",
            "Level 21",
            "Level 22"
    };
    int[] imageId = {
            R.drawable.number_1,
            R.drawable.number_2,
            R.drawable.number_3,
            R.drawable.number_4,
            R.drawable.number_5,
            R.drawable.number_6,
            R.drawable.number_7,
            R.drawable.number_8,
            R.drawable.number_9,
            R.drawable.number_10,
            R.drawable.number_11,
            R.drawable.number_12,
            R.drawable.number_13,
            R.drawable.number_14,
            R.drawable.number_15,
            R.drawable.number_16,
            R.drawable.number_17,
            R.drawable.number_18,
            R.drawable.number_19,
            R.drawable.number_20,
            R.drawable.number_21,
            R.drawable.number_22//22
    };



    public Context contextModeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(savedInstanceState == null) {
            modePos = getIntent().getIntExtra(ModesActivity.modePosKey,0);

            prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.


            //read from file
            //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
            //for (int i=0;i<10;i++)
            lastLevelAndWinLevelsArray.lastLevel = new int[22];
            for (int i=0;i<22;i++)
                lastLevelAndWinLevelsArray.lastLevel[i] = prefs.getInt(LastLevel+"_"+i, 0);
            lastLevelAndWinLevelsArray.score = prefs.getInt(Score, 0);
            for (int i=0;i<lastLevelAndWinLevelsArray.numModes ; i++)
                lastLevelAndWinLevelsArray.winLevelsArray[i] = new boolean[22];
            for (int i=0;i<22;i++)
            for (int j = 0; j < 22; j++) {
                lastLevelAndWinLevelsArray.winLevelsArray[i][j] = prefs.getBoolean(WinLevelsArray + "_" + i + "_" + j, false);
                Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[i][j]), " read from sharedPreferences");
            }

            //Toast.makeText(LevelActivity.this, "Thanks Again" + lastLevelAndWinLevelsArray.lastLevel, Toast.LENGTH_LONG).show();
            //Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[0]), "no network");
            //Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[1]), "no network");
            //end read from file

            //String modePos = sharedpreferences.getString("modePos",null);
            adapter.modePos = modePos;
            adapter = new CustomGrid(LevelActivity.this, nameLevel, imageId);
            grid = (GridView) findViewById(R.id.grid);
            grid.setAdapter(adapter);

            final Animation animScale = AnimationUtils.loadAnimation(this, R.anim.scale);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    view.startAnimation(animScale);
                    animScale.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            //add sound
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            moveToTheGame(position, nameLevel[position],modePos);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    //Toast.makeText(LevelActivity.this, "You Clicked at Mode "+modePos +" and level " + nameLevel[+position], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void moveToTheGame(int pos , String stageName, int modePos)
    {
        Intent i = new Intent(this , GameField.class);
        i.putExtra(stageNumberKey, pos);
        i.putExtra(stageNameKey, stageName);
        i.putExtra(ModesActivity.modePosKey,modePos);
        startActivityForResult(i, request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == request_code) && (resultCode == RESULT_OK))
        {
            boolean playerWin = data.getBooleanExtra(GameField.playerWinKey , false);
            if (playerWin)
            {
                for (int i=0;i<22;i++)
                    for (int j=0;j<22;j++){
                        Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[i][j]), " Print WinLevels Array ");
                    }

                //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
                for (int i=0;i<22;i++)
                    lastLevelAndWinLevelsArray.lastLevel[i] = prefs.getInt(LastLevel+"_"+i, 0);
                //lastLevelAndWinLevelsArray.lastLevel[modePos] = sharedpreferences.getInt(LastLevel+"_"+modePos, 0);
                lastLevelAndWinLevelsArray.score = prefs.getInt(Score, 0);
                for (int i = 0; i < lastLevelAndWinLevelsArray.winLevelsArray[modePos].length; i++) {
                    lastLevelAndWinLevelsArray.winLevelsArray[modePos][i] = prefs.getBoolean(WinLevelsArray + "_" + i, false);
                    Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[modePos][i]), " read from sharedPreferences");
                }
                //startActivity(new Intent(LevelActivity.this, PopUp.class));
                int position = data.getIntExtra(stageNumberKey , 0);
                if (lastLevelAndWinLevelsArray.winLevelsArray[modePos][position] == false) {
                    //if he was not win the level ,, unlock next level
                    //lastLevelAndWinLevelsArray.winLevelsArray[modePos][position] = true;
                    //Toast.makeText(LevelActivity.this,"mode Pos : "+ modePos, Toast.LENGTH_SHORT).show();
                    for (int i=0;i<22;i++){
                        if (i != modePos){
                            lastLevelAndWinLevelsArray.winLevelsArray[i][position] = false;
                        }
                        else {
                            lastLevelAndWinLevelsArray.winLevelsArray[modePos][position] = true;
                        }
                    }

                    lastLevelAndWinLevelsArray.lastLevel[modePos] += 1;
                    //for (int i=0;i<22;i++)
                        //lastLevelAndWinLevelsArray.score =+ lastLevelAndWinLevelsArray.lastLevel[modePos];
                    lastLevelAndWinLevelsArray.score++;
                    Log.w(String.valueOf(lastLevelAndWinLevelsArray.score), " score");

                    //update it in database
                    if (lastLevelAndWinLevelsArray.hasAccount) {
                        JSONObject updateScore = new JSONObject();
                        try {
                            updateScore.put("cmd", "updateScore");
                            updateScore.put("newScore", lastLevelAndWinLevelsArray.score);
                            Log.w(String.valueOf(lastLevelAndWinLevelsArray.score), " I'm new score");
                            updateScore.put("userName", lastLevelAndWinLevelsArray.userNameForKeepLogin);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mySocket.mWebSocketClient.send(updateScore.toString());
                        //end save
                    }


                    //save data
                    prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                    for (int i=0 ; i<22 ; i++)
                        prefs.putInt(LastLevel+"_"+i,lastLevelAndWinLevelsArray.lastLevel[i]);
                    //Score is tha same of "newScore"
                    prefs.putInt(Score,lastLevelAndWinLevelsArray.score);
                    for (int i=0;i<22;i++)
                    for (int j=0 ; j<22 ;j++){
                        prefs.putBoolean(WinLevelsArray + "_" + i + "_" + j, lastLevelAndWinLevelsArray.winLevelsArray[i][j]);
                        Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[i][j]), " write into sharedPreferences");
                    }
                    prefs.commit();
                    //Toast.makeText(LevelActivity.this, "lastLevel[modePos]" + lastLevelAndWinLevelsArray.lastLevel[modePos], Toast.LENGTH_LONG).show();
                 //   Toast.makeText(LevelActivity.this, "score" + lastLevelAndWinLevelsArray.score, Toast.LENGTH_LONG).show();
                    for (int i=0;i<22 ;i++)
                        Log.w(String.valueOf(lastLevelAndWinLevelsArray.lastLevel[i]), "last Level[i]");
                    //Log.w(String.valueOf(lastLevelAndWinLevelsArray.winLevelsArray[1]), "no network");
                    //saveInFile();
                    //end save data

                    /*adapterMode = new CustomGridMode(ModesActivity.contextModeActivity, nameModes, imageModesId);
                    View inflatedView1 = getLayoutInflater().inflate(R.layout.activity_modes,null);
                    gridViewMode = (GridView) inflatedView1.findViewById(R.id.gridViewModes);
                    gridViewMode.setAdapter(adapterMode);*/
                    View inflatedView = getLayoutInflater().inflate(R.layout.grid_mode_single,null);
                    TextView textViewNumLevelsWins = (TextView) inflatedView.findViewById(R.id.num_levels_wins);
                    textViewNumLevelsWins.setText(String.valueOf(lastLevelAndWinLevelsArray.lastLevel[modePos]) +"/22");
                    Log.i("LAST LEVEL : ",String.valueOf(lastLevelAndWinLevelsArray.lastLevel[modePos]));

                    adapter.isEnabled(lastLevelAndWinLevelsArray.lastLevel[modePos]);
                    adapter.notifyDataSetChanged();
                    grid.setAdapter(adapter);
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.menu_level_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}