package com.example.vladimir.tetris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vladimir.logic.SQLWorker;

public class ScoreActivity extends AppCompatActivity {

    TextView playerChampion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        playerChampion=(TextView)findViewById(R.id.textChampion);
        SQLWorker sqw=new SQLWorker(this);
        int score=sqw.ReadChampion();
        if(score!=0)
        playerChampion.setText("Лучший игрок: "+String.valueOf(score));
    }
}
