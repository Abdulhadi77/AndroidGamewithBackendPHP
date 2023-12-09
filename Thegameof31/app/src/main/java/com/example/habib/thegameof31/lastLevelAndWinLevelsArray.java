package com.example.habib.thegameof31;

/**
 * Created by ASUS on 4/12/2016.
 */
public class lastLevelAndWinLevelsArray  {
    public static int modePos ;
    public static int []lastLevel = new int[22];
    public static int numModes = 22;
    public static boolean [][] winLevelsArray = new boolean[numModes][22];
    public static int score = 0;
    public static String userNameForKeepLogin = "notLogin";
    public static boolean hasAccount=false;
    public static String userNameOfRandomPlayer = " ";

    public static int scoreOfRandomPlayer = 0;
    public static int scorePlayer1 = 0;
    public static String player1 = " ";
    public static String player2 = " ";
    //public static int sumGameFieldOnline = 0;


    public static int getNumOfLevelsWin(int modePos){

        int sum=0;
        for (int i=0;i<winLevelsArray[modePos].length;i++){
            if (winLevelsArray[modePos][i] == true){
                sum++;
            }
        }
        return sum;
    }
}
