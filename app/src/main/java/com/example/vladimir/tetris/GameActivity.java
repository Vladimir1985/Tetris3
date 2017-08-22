package com.example.vladimir.tetris;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladimir.logic.LogicMachine;
import com.example.vladimir.visual.*;


public class GameActivity extends AppCompatActivity {

    //Канвас на котором рисуем игровое поле
    DrawCanvasTimer canvasView;
    //Класс отвечающий за логику
    LogicMachine logic;

    TextView playerScore;

    //Таймер анимации
    private Handler handler = new Handler();
AnimationDrawable gameover_animation;
    ImageView anim_gameover;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playerScore=(TextView)findViewById(R.id.score);
        try {
            canvasView=(DrawCanvasTimer)findViewById(R.id.customview);
            //Создаем перехватчик событий и присваиваем его нашему канвасу
            GameActivity.TListener tl = new GameActivity.TListener();
            canvasView.setOnTouchListener(tl);

            //Включение анимации GameOver
           // anim_gameover=(ImageView)findViewById(R.id.anim_gameover);
//            anim_gameover.setBackgroundResource(R.drawable.a_gameover);
//            gameover_animation = (AnimationDrawable) anim_gameover.getBackground();
//            gameover_animation.start();
//            Drawable drawable=anim_gameover.getDrawable();
//            if(drawable instanceof Animatable)
//                ((Animatable)drawable).start();

            logic = new LogicMachine(this);
            //Создание нового объекта
            boolean b=logic.CreateObject();
            canvasView.UpdatePlane(logic.gamePlanel);
            canvasView.postInvalidate();
            //Задаем нашим ImageView картинки сгенерированных объектов
            SetPredictedFigures();
            //Создание таймера с заданной скоростью
            handler.postDelayed(runnable, startSpeedMs);
        }catch (Exception e)
        {
            int y=0;
        }
    }
    //Передаем в функцию наши внутренние иден.номера фигур, соответственно ним возвращаем id номера изображений этих фигур
    int GetImageNum(int a)
    {
        int currentImage=0;
        switch (a)
        {
            case 0:currentImage=R.mipmap.cub;break;
            case 1:currentImage=R.mipmap.straigth;break;
            case 2:currentImage=R.mipmap.t_object;break;
            case 3:currentImage=R.mipmap.r_angle;break;
            case 4:currentImage=R.mipmap.l_angle;break;
            case 5:currentImage=R.mipmap.lz_object;break;
            case 6:currentImage=R.mipmap.rz_object;break;
            default:break;
        }
        return currentImage;
    }
    //Устанавливаем содержимое наших ImageView согласно массиву figures
    void SetPredictedFigures()
    {
            ImageView image=(ImageView) findViewById(R.id.imageView1);
            image.setImageResource(GetImageNum(logic.figures[0]));
            image=(ImageView) findViewById(R.id.imageView2);
            image.setImageResource(GetImageNum(logic.figures[1]));
            image=(ImageView) findViewById(R.id.imageView3);
            image.setImageResource(GetImageNum(logic.figures[2]));
    }
    //Шаг с которой изменяется скорость движения фигур в миллисекундах
    int stepChangeSpeedMs=100;
    //Начальная скорость движения фигур в миллисекундах
    int startSpeedMs=500;
    //Минимально допустимая скорость движения фигур в миллисекундах
    int minSpeedMs=200;

    //Обработчик таймера
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //Движение текущей фигуры на 1 шаг вниз
            logic.MoveDown();
            //Если переменная конца игры
            if (logic.gameover) {
                //Выводим GameOver
                playerScore.setText("GAMEOVER");
                //Делаем последний апедейт сцены
                canvasView.UpdatePlane(logic.gamePlanel);
                canvasView.postInvalidate();
//                //Включение анимации GameOver
//                ImageView a_gameover=(ImageView)findViewById(R.id.anim_gameover);
//                Drawable drawable=a_gameover.getDrawable();
//                if(drawable instanceof Animatable)
//                    ((Animatable)drawable).start();

                //Удаляем таймер
                handler.removeCallbacks(runnable);

            } else
            {
                //Обновляем очки
                playerScore.setText(String.valueOf(logic.GetScore()));
                //Обновляем сцену после движения
                canvasView.UpdatePlane(logic.gamePlanel);
                canvasView.postInvalidate();
                //В зависимости от уровня формируется текущая скорость падения фигур
                int currentSpeedMs=startSpeedMs-(logic.player.GetLevel()*stepChangeSpeedMs);
                currentSpeedMs=(currentSpeedMs>minSpeedMs)?currentSpeedMs:minSpeedMs;
                //Задаем нашим ImageView картинки сгенерированных объектов
                SetPredictedFigures();
                //Запускаем новую итерацию таймера с новой скоростью
                handler.postDelayed(this, currentSpeedMs);
            }
        }
    };

    //Позиция нажатия на экран по оси X
    float LastPointX=0;
    //Переменные для обрабативания двойного нажатия
    int numberOfTaps = 0;
    long lastTapTimeMs = 0;
    long touchDownMs = 0;

    //Перехватчик нажатий на экран в игровой области
    class TListener implements View.OnTouchListener
    {
        //Время необходимое для дабл клика
        int doubleClick=300;
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_MOVE:
                    if(LastPointX==0)
                    {
                        LastPointX=event.getX();
                        return  true;
                    }
                    //Минимальное расстояние которое должен пройти по экрану палец равно размеру одного "кубика" нашего игрового поля
                    if(LastPointX-event.getX()>canvasView.gameCubeSize)
                    {
                        logic.MoveLeft();
                        canvasView.UpdatePlane(logic.gamePlanel);
                        canvasView.invalidate();
                          LastPointX = event.getX();
                    }
                    if(event.getX()-LastPointX>canvasView.gameCubeSize)
                    {
                        logic.MoveRight();
                        canvasView.UpdatePlane(logic.gamePlanel);
                        canvasView.invalidate();
                        LastPointX = event.getX();
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    touchDownMs = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    LastPointX=0;
                    if ((System.currentTimeMillis() - touchDownMs) > doubleClick)
                    {
                        numberOfTaps = 0;
                        lastTapTimeMs = 0;
                        break;
                    }

                    if (numberOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < doubleClick)
                    {
                        lastTapTimeMs = 0;
                        numberOfTaps = 0;
                        logic.RotateObject();
                    } else
                    {
                        numberOfTaps = 1;
                    }
                    lastTapTimeMs = System.currentTimeMillis();
                break;
                default:break;
            }
            return true;
        }

    }

}
