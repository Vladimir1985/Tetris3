package com.example.vladimir.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void NewGame(View view) {
        // Do something in response to button
        Intent intent = new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    public void ThreadTest(View view) {
        // Do something in response to button
       // Intent intent = new Intent(this,Main2Activity.class);
       // startActivity(intent);
    }

}
