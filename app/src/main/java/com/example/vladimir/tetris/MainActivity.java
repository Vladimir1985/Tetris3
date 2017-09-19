package com.example.vladimir.tetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void NewGame(View view)
    {
        // Do something in response to button
        Intent intent = new Intent(this,GameActivity.class);
        Bundle b=new Bundle();
        b.putInt("key",1);
        intent.putExtras(b);
        startActivity(intent);
       // finish();
    }

    public void ContinueGame(View view)
    {
        Intent intent = new Intent(this,GameActivity.class);
        Bundle b=new Bundle();
        b.putInt("key",2);
        intent.putExtras(b);
        startActivity(intent);
    }

}
