package com.example.habib.thegameof31;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by HABIB on 4/3/2016.
 */
public class VirtualPlayer
{
    private int limit ;
    private int game[][][][][][][];
    public int data[] ;

    public VirtualPlayer(int limit , int[] data1)
    {
        this.limit = limit;
        game = new int [32][5][5][5][5][5][5];
        data = new int[7];
        for (int i = 1; i < 7; i++)
            data[i] = data1[i];
        for (int a = 0; a < 5; a++)
            for (int b = 0; b < 5; b++)
                for (int c = 0; c < 5; c++)
                    for (int d = 0; d < 5; d++)
                        for (int e = 0; e < 5; e++)
                            for (int f = 0; f < 5; f++)
                                game[31][a][b][c][d][e][f] = 2;
    }

    private boolean win(int sum)
    {
        if (sum > 31)
            return true;
        if (game[sum][data[1]][data[2]][data[3]][data[4]][data[5]][data[6]] != 0)
        {
            if (game[sum][data[1]][data[2]][data[3]][data[4]][data[5]][data[6]] == 1)
                return true;
            else
                return false;
        }
        else
        {
            boolean  ww = false;
            boolean[] w = new boolean[7];
            int d;
            for (int i = 1; i < 7; i++)
            {
                w[i] = false;
                if (data[i] > 0)
                {
                    d = data[i];
                    data[i]--;
                    if ((sum + i) > 31)
                        w[i] = false;
                    else
                        w[i] = lose(sum + i);
                    data[i] = d;
                }
            }
            ww = w[1] || w[2] || w[3] || w[4] || w[5] || w[6];
            if (ww)
                game[sum][data[1]][data[2]][data[3]][data[4]][data[5]][data[6]] = 1;
            else
                game[sum][data[1]][data[2]][data[3]][data[4]][data[5]][data[6]] = 2;
            return ww;
        }
    }

    private boolean lose(int sum)
    {
        return !win(sum);
    }

    public int getProperMove(int sum ,int [] d)
    {
        LinkedList<Integer> moves = new LinkedList<>();
        for (int i = 1 ; i < 7 ; i++)
            data[i] = d[i];
        if (sum >= limit)
        {
            for (int i = 1 ; i < 7 ; i++)
            {
                if (data[i] > 0)
                {
                    data[i]--;
                    if (lose(sum+i))
                        moves.add(i);
                    data[i]++;
                }
            }
        }
        if (moves.size() == 0)
        {
            for (int i = 1 ; i < 7 ; i++)
            {
                if (data[i] > 0)
                    moves.add(i);
            }
        }

        Random random = new Random();
        int move = random.nextInt(moves.size());
        return moves.get(move);
    }
}
