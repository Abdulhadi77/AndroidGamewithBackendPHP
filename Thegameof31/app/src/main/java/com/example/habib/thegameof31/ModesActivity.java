package com.example.habib.thegameof31;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class ModesActivity extends AppCompatActivity {

    //public SharedPreferences sharedpreferences;
    public MySharedPreferences prefs;
    public static final String LastLevel = "lastLevel";
    public static final String MyPREFERENCES = "MyPrefs";
    public final static String stageNumberKey = "stageNumberKey";
    public final static String stageNameKey = "stageNameKey";
    public final static String modePosKey = "modePosKey";
    public final static int requestModeKey=1996;
    public static final String Score = "score";

    GridView gridViewMode;
    CustomGridMode adapter;
    public static Context contextModeActivity;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modes);
        contextModeActivity = this;
        //if(savedInstanceState == null) {
            adapter = new CustomGridMode(ModesActivity.this, nameModes, imageModesId);
            gridViewMode = (GridView) findViewById(R.id.gridViewModes);
            gridViewMode.setAdapter(adapter);

            //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        for (int i=0;i<22;i++)
            lastLevelAndWinLevelsArray.lastLevel[i] = prefs.getInt(LastLevel+"_"+i, 0);
        View inflatedView = getLayoutInflater().inflate(R.layout.grid_mode_single,null);
        TextView textViewNumLevelsWins = (TextView) inflatedView.findViewById(R.id.num_levels_wins);
        for (int i=0;i<22 ; i++)
            textViewNumLevelsWins.setText(String.valueOf(lastLevelAndWinLevelsArray.lastLevel[i]) +"/22");
            //setContentView(R.layout.activity_modes);
            gridViewMode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    moveToLevelActivity(position);

                    //Toast.makeText(MainActivity.this, "You Clicked at " + nameLevel[+position], Toast.LENGTH_SHORT).show();
                }
            });
        //}
    }

    public void moveToLevelActivity(int modePos){
        Intent intent = new Intent(this,LevelActivity.class);
        intent.putExtra(modePosKey, modePos);
        //intent.putExtra("context", String.valueOf(this));
        lastLevelAndWinLevelsArray.modePos = modePos;
        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        //SharedPreferences.Editor editor = sharedpreferences.edit();
        prefs.putInt("modePos",modePos);
        prefs.commit();
        startActivityForResult(intent, requestModeKey);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == requestModeKey) && (resultCode == RESULT_OK)) {

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter = new CustomGridMode(ModesActivity.this, nameModes, imageModesId);
        gridViewMode = (GridView) findViewById(R.id.gridViewModes);
        gridViewMode.setAdapter(adapter);
        prefs = MySharedPreferences.getInstance(this,MyPREFERENCES); //provide context & preferences name.
        for (int i=0;i<22;i++)
            lastLevelAndWinLevelsArray.lastLevel[i] = prefs.getInt(LastLevel+"_"+i, 0);
        View inflatedView = getLayoutInflater().inflate(R.layout.grid_mode_single,null);
        TextView textViewNumLevelsWins = (TextView) inflatedView.findViewById(R.id.num_levels_wins);
        for (int i=0;i<22 ; i++)
            textViewNumLevelsWins.setText(String.valueOf(lastLevelAndWinLevelsArray.lastLevel[i]) +"/22");

    }

}
