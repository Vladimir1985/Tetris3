package com.example.vladimir.struct;

/**
 * Created by Vladimir on 14.07.2017.
 */

public class Player {
    int score=0;
    int scoreStep=100;
    int levelThreshold=100;
    byte level=1;
    String name="Player";
    public  void  Player()
    {

    }


    public  void IncrementScore()
    {
        score+=scoreStep;
        if(score%levelThreshold==0)
            level++;
    }

    public byte GetLevel()
    {
        return  level;
    }

    public int GetScore()
    {
        return score;
    }
}
