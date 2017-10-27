package com.example.vladimir.tetris;


import android.content.SharedPreferences;
import android.os.Debug;
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
import com.example.vladimir.serialize.CurrentStateDeserializer;
import com.example.vladimir.serialize.CurrentStateSerializer;
import com.example.vladimir.struct.CurrentState;
import com.example.vladimir.visual.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class GameActivity extends AppCompatActivity {

    //Канвас на котором рисуем игровое поле
    DrawCanvasTimer canvasView;
    //Класс отвечающий за логику
    LogicMachine logic;

    TextView playerScore;

    //Таймер анимации
    private Handler handler = new Handler();

    //Файл настроек в который сохраняем
    private SharedPreferences  mSettings ;


    private static final String LOG_TAG = "my_tag";
    //Класс для работы с SQL
    SQLWorker sqw=new SQLWorker(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Извлекаем ключ переданный нам через intent, если он равен 1 значит мы создаем новую игру
        //Если равен 2, то загружаем сохраненную игру
        Bundle bundle = getIntent().getExtras();
        int bundleValue=0;
        if(bundle!=null)
            bundleValue=bundle.getInt("key");

        playerScore=(TextView)findViewById(R.id.score);
        canvasView=(DrawCanvasTimer)findViewById(R.id.customview);
        //Создаем перехватчик событий и присваиваем его нашему канвасу
        GameActivity.TListener tl = new GameActivity.TListener();
        //Инициализируем файл настроек в приватном режиме, он может быть изменен только из нашей программы
        mSettings=getPreferences(MODE_PRIVATE);
        //Подключаем к нашему канвасу Listener, для перехвата касаний
        canvasView.setOnTouchListener(tl);

        logic = new LogicMachine();
        switch (bundleValue)
        {
            case 1:
                StartNewGame();
                break;
            case 2:
                ContinueGame();
                break;


        }

        //Создание таймера с заданной скоростью
        handler.postDelayed(runnable, startSpeedMs);

    }
    //Загрузка сохраненной игры
    void ContinueGame()
    {
        CurrentState state = new CurrentState();
        //Подключаем наш адаптер для десериализации
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CurrentState.class, new CurrentStateDeserializer())
                .create();
        //Извлекаем данные из свойств
        String js = mSettings.getString("MyState", "");
        //Если данных нет значит начинаем новую игру
        if(js=="")
            StartNewGame();
        else {
            //Если данные есть десериализуем их в класс состояния
            state = gson.fromJson(js, CurrentState.class);
            logic.SetCurrentStateAfterLoad(state);
        }
    }
//Начало новой игры
    void StartNewGame()
    {
        //Создаем игровой объект
        boolean b = logic.CreateObject();
        //Передаем его в наш канвас
        canvasView.UpdatePlane(logic.GetBattlefield());
        //перерисовываем канвас
        canvasView.postInvalidate();
        //Задаем нашим ImageView картинки сгенерированных объектов
        SetPredictedFigures();
    }

    //При выходе из текущего активити сохраняем данные игры
    @Override
    public void onBackPressed() {
        //Создаем файл настроек
        SharedPreferences.Editor prefsEditor = mSettings.edit();
        //Подключаем наш сериализатор
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CurrentState.class, new CurrentStateSerializer())
                .create();
        //Сериализуем класс состояния
        String json = gson.toJson(logic.GetCurrentState());
        //Записываем результат в свойства
        prefsEditor.putString("MyState", json);
        prefsEditor.commit();
        super.onBackPressed();
    }

//    String LoadDataFromFile()
//    {
//        FileInputStream stream = null;
//        StringBuilder sb = new StringBuilder();
//        String line;
//
//        try {
//            stream = openFileInput(APP_PREFERENCES);
//
//            try {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line);
//                }
//            } finally {
//                stream.close();
//            }
//
//            Log.d(LOG_TAG, "Data from file: " + sb.toString());
//
//        } catch (Exception e) {
//
//            Log.d(LOG_TAG, "Файла нет или произошла ошибка при чтении");
//            return "";
//        }
//        return sb.toString();
//    }

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
            if (logic.getGameOverState()) {
                //Выводим GameOver
                playerScore.setText("GAMEOVER");
                //Делаем последний апедейт сцены
                canvasView.UpdatePlane(logic.GetBattlefield());
                canvasView.postInvalidate();
                //Записываем  в базу данных очки игрока
                sqw.ClearSettingsTable();
                sqw.SetChampion(logic.GetScore());

                //Удаляем таймер
                handler.removeCallbacks(runnable);

            } else
            {
                //Обновляем очки на экране
                playerScore.setText(String.valueOf(logic.GetScore()));
                //Обновляем сцену после движения
                canvasView.UpdatePlane(logic.GetBattlefield());
                canvasView.postInvalidate();
                //В зависимости от уровня формируется текущая скорость падения фигур
                int currentSpeedMs=startSpeedMs-(logic.GetLevel()*stepChangeSpeedMs);
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
                        canvasView.UpdatePlane(logic.GetBattlefield());
                        canvasView.invalidate();
                        LastPointX = event.getX();
                    }
                    if(event.getX()-LastPointX>canvasView.gameCubeSize)
                    {
                        logic.MoveRight();
                        canvasView.UpdatePlane(logic.GetBattlefield());
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
