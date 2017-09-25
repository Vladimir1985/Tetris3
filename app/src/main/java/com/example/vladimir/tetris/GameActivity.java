package com.example.vladimir.tetris;



import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vladimir.logic.LogicMachine;
import com.example.vladimir.logic.SQLWorker;
import com.example.vladimir.visual.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


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
    //Имя файла в который сохраняем
    String filename = "save";

    private static final String LOG_TAG = "my_tag";
    SQLWorker sqw=new SQLWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Context context=this;
        //Извлекаем ключ переданный нам через intent, если он равен 1 значит мы создаем новую игру
        //Если равен 2, то загружаем сохраненную игру
        Bundle bundle = getIntent().getExtras();
        int bundleValue=0;
        if(bundle!=null)
            bundleValue=bundle.getInt("key");

        try {
            playerScore=(TextView)findViewById(R.id.score);
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
            switch (bundleValue)
            {
                case 1:
                    StartNewGame();
                    break;
                case 2:
                   //String s=LoadDataFromFile();
                    if(!sqw.CheckDB())
                        StartNewGame();
                    else
                        ContinueGame();
                    break;


            }

            //Создание таймера с заданной скоростью
            handler.postDelayed(runnable, startSpeedMs);
        }catch (Exception e)
        {
            int y=0;
        }
    }

    void ContinueGame()
    {
        String BF=sqw.ReadBF();
        for(int i=0;i<logic.GetHeigth();i++)
            for (int j = 0; j < logic.GetWidth(); j++)
            {
                if(BF.charAt(logic.GetWidth()*i+j)=='1')
                    logic.gamePlanel[j][i]=true;
                else
                    logic.gamePlanel[j][i]=false;
            }

        int []f=sqw.ReadFigures(logic.GetFigures().length);
        logic.SetFigures(f);

        logic.player.SetScore(sqw.ReadScore());

        logic.CreateObject(sqw.ReadFigureNum());
        logic.GetCurrentObject().SetObjectCoord(sqw.ReadCoord());

    }
void ContinueGame(String lastGameData)
{


    //Разбиваем строку по нашему разделителю
    String[] separated =  lastGameData.split("&");
    //Первая группа данных это данные о состоянии нашего игрового поля, заносим их в матрицу
    for(int i=0;i<logic.GetHeigth();i++)
        for (int j = 0; j < logic.GetWidth(); j++)
        {
            if(separated[0].charAt(logic.GetWidth()*i+j)=='1')
                logic.gamePlanel[j][i]=true;
            else
                logic.gamePlanel[j][i]=false;
        }

    //Записываем данные о предсказанных фигурах
    for(int i=0;i<logic.GetFigures().length;i++)
        logic.GetFigures()[i]=Integer.parseInt(separated[1+i]);

    //Сохраняем очки пользователя
    logic.SetScore(Integer.parseInt(separated[4]));
    //Сохраняем текущую фигуру
    logic.CreateObject(Integer.parseInt(separated[5]));

//    byte[] blob = sqw.ReadGameState();
//    String json = new String(blob);
//    Gson gson = new Gson();
//    LogicMachine retrieved_logic = gson.fromJson(json, new TypeToken<LogicMachine>()
//    {}.getType());
//    logic=retrieved_logic;
}

    void StartNewGame()
    {
        boolean b = logic.CreateObject();
        canvasView.UpdatePlane(logic.gamePlanel);
        canvasView.postInvalidate();
        //Задаем нашим ImageView картинки сгенерированных объектов
        SetPredictedFigures();
    }

    //При выходе из текущего активити сохраняем данные игры
    @Override
    public void onBackPressed() {


        //Создаем объект GSON
//        Gson gson = new Gson();
//        //Затем конвертируем наш класс логики в массив байт и передаем для записи в БД
//        try {
//            sqw.WriteGameState(gson.toJson(logic).getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //Строка в которую записываем сохраняемые данные
        String out = "";
        //Переносим данные о состоянии игрового поля со всеми фигурами
        for(int i=0;i<logic.GetHeigth();i++)
            for (int j = 0; j < logic.GetWidth(); j++)
            {
                if(logic.gamePlanel[j][i])
                    out+='1';
                else
                    out+='0';
            }

        sqw.WriteGameState(out,logic.GetFigures(),logic.GetScore(),logic.currentFigure,logic.GetCurrentObject().GetObjectCoord());
        //Записываем разделительные знаки, они делят строку данных на отдельные блоки
        out+='&';

        //Записываем данные о предсказанных фигурах
        for(int i=0;i<logic.GetFigures().length;i++)
            out += String.valueOf(logic.GetFigures()[i])+'&';
        //Сохраняем очки пользователя
        out+=String.valueOf(logic.GetScore())+'&';
        //Сохраняем текущую фигуру
        out+=String.valueOf(logic.currentFigure);
        File file=new File(this.getFilesDir(),filename);

        //Выходной поток
        FileOutputStream outputStream;
        //Запись данных
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(out.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();


        return;
    }

    String LoadDataFromFile()
    {
    FileInputStream stream = null;
    StringBuilder sb = new StringBuilder();
    String line;

        try {
        stream = openFileInput(filename);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            stream.close();
        }

        Log.d(LOG_TAG, "Data from file: " + sb.toString());

    } catch (Exception e) {

        Log.d(LOG_TAG, "Файла нет или произошла ошибка при чтении");
            return "";
    }
    return sb.toString();
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
            image.setImageResource(GetImageNum(logic.GetFigures()[0]));
            image=(ImageView) findViewById(R.id.imageView2);
            image.setImageResource(GetImageNum(logic.GetFigures()[1]));
            image=(ImageView) findViewById(R.id.imageView3);
            image.setImageResource(GetImageNum(logic.GetFigures()[2]));
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
                sqw.ClearSettingsTable();
                sqw.SetChampion(logic.player.GetScore());
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
