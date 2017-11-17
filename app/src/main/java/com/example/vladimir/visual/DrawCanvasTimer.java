package com.example.vladimir.visual;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;

import com.example.vladimir.tetris.R;

public class DrawCanvasTimer extends View {

    Resources res = getResources();
    int GameWidth = res.getInteger(R.integer.GameFeel_Width);
    int GameHeight = res.getInteger(R.integer.GameFeel_Heigth);
    public boolean[][] gamePlane ;

    public int gameCubeSize;

    Bitmap black, green;
    SurfaceHolder surfaceHolder;
    public DrawCanvasTimer(Context context)
    {
        super(context);
        init(context);
    }

    public DrawCanvasTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawCanvasTimer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        gamePlane = new boolean[GameWidth][GameHeight];
    }

    void CreateBitmap()
    {
        black = Bitmap.createBitmap(gameCubeSize, gameCubeSize, Bitmap.Config.RGB_565);
        green = Bitmap.createBitmap(gameCubeSize, gameCubeSize, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(black);
        Paint p = new Paint();

        p.setColor(0xFF000000);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0, 0, gameCubeSize, gameCubeSize), p);

        canvas = new Canvas(green);

        p.setColor(0x8800FF00);
        canvas.drawRect(new Rect(0, 0, gameCubeSize, gameCubeSize), p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(6);
        p.setColor(0xFFFFFFFF);
        canvas.drawRect(new Rect(0, 0, gameCubeSize, gameCubeSize), p);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        gameCubeSize = w / 10;
        CreateBitmap();
    }


    public void UpdatePlane(boolean b[][]) {
        for (int i = 0; i < GameWidth; i++)
            for (int j = 0; j < GameHeight; j++)
                gamePlane[i][j] = b[i][j];
        //invalidate();
    }




    @Override
    protected void onDraw(Canvas canvas)
    {
        try {
            Paint mPaints = new Paint();
            mPaints.setAntiAlias(true);
            Rect bounds;
            for(int i=0;i<GameWidth;i++)
                for(int j=0;j<GameHeight;j++)
                {
                    bounds = new Rect(i * gameCubeSize, j * gameCubeSize, (i + 1) * gameCubeSize, (j + 1) * gameCubeSize);
                    if(gamePlane[i][j]==true)
                    {
                        canvas.drawBitmap(green,bounds.left,bounds.top,mPaints);
                    }else
                    {
                        canvas.drawBitmap(black,bounds.left,bounds.top,mPaints);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
