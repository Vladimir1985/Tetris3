package com.example.vladimir.logic;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.Log;

//import com.example.vladimir.struct.CubeObject;
import com.example.vladimir.struct.*;
//import com.example.vladimir.struct.GameObject;
//import com.example.vladimir.struct.Player;
//import com.example.vladimir.struct.StraightObject;
import com.example.vladimir.tetris.R;

import java.util.Random;

/**
 * Created by Vladimir on 28.06.2017.
 */

public class LogicMachine
{
    //Размеры нашего игрового поля в игровых "кубиках"
    int GameWidth ;
    int  GameHeight;

    //Матрица отвечающая за отображение игрового поля. Если элемент матрицы равен "true" то значит в этой ячейке находится объект,
    // если "false" то нет
   // boolean[][] gamePlanel;

    private Context context;
    //Класс хранящий данные о текущем состоянии игры
    CurrentState currentState;



    //Флаг конца игры
    public boolean gameover=false;


   //Вернуть ширину игрового поля
    public int GetWidth()
    {
        return  GameWidth;
    }
    //Вернуть высоту игрового поля
    public int GetHeigth()
    {
        return  GameHeight;
    }
     public int GetLevel()
     {
         return  currentState.GetLevel();
     }

     public CurrentState GetCurrentState()
     {
         return  currentState;
     }

    public void SetCurrentState(CurrentState currentState)
    {
        this.currentState=currentState;
        currentState.coord=currentState.GetObject().GetObjectCoord();
    }


    public void SetCurrentStateAfterLoad(CurrentState currentState)
    {
        this.currentState=currentState;
        CreateObject(currentState.currentFigure);
        this.currentState.GetObject().SetObjectCoord(currentState.coord);

    }
    //Возвращаем тип текущего объекта
    public int GetCurrentFigure()
    {
        return  currentState.GetCurrentFigure();
    }
    public  LogicMachine(Context current)
    {
        this.context=current;

        //Размеры игрового поля берем из ресурсов и создаем игровое поле и игрока
        Resources res = context.getResources();

        GameWidth = res.getInteger(R.integer.GameFeel_Width);
        GameHeight = res.getInteger(R.integer.GameFeel_Heigth);
        currentState=new CurrentState();
       // gamePlanel=new boolean[GameWidth][GameHeight];
        //player=new Player();
        //Генерируем предсказание на первые 3 фигуры
        SetStartFigures();
    }


    //Возвращаем игровое поле
    public boolean[][] GetBattlefield()
    {
        return currentState.GetPanel();
    }
    //Возвращает текущий игровой объект
    public GameObject GetCurrentObject()
    {
        return  currentState.GetObject();
    }
    //Возвращаем массив с предсказанными фигурами
    public int[] GetFigures()
    {
        return currentState.GetFigures();
    }
    //Устанавливает массив с предсказанными фигурами
    public void SetFigures(int[]f)
    {
        currentState.SetFigures(f);
    }
    //Генерация первых трех фигур на старте
    private void SetStartFigures()
    {
        for(int i=0;i<currentState.GetFigures().length;i++)
            currentState.GetFigures()[i] = GetRandomNumber();
    }
    //Генерируем число от 0 до 7
    public int GetRandomNumber()
    {
        //Генерируем число от 0 до 7
        Random rand = new Random();
        int i = rand.nextInt(7);
        return  i;
    }

    void InitializeGameObject(int i)
    {
        GameObject currentObject;
        switch (i)
        {
            case 0:currentObject=new CubeObject();currentState.SetObject(currentObject);break;
            case 1:currentObject=new StraightObject();currentState.SetObject(currentObject);break;
            case 2:currentObject=new TObject();currentState.SetObject(currentObject);break;
            case 3:currentObject=new RAngleObject();currentState.SetObject(currentObject);break;
            case 4:currentObject=new LAngleObject();currentState.SetObject(currentObject);break;
            case 5:currentObject=new LZObject();currentState.SetObject(currentObject);break;
            case 6:currentObject=new RZObject();currentState.SetObject(currentObject);break;
            default:break;
        }
    }
    //Создаем заданный объект
    public boolean CreateObject(int i)
    {
        //В зависимости от числа мы инициализируем объект родительского класса нужным классом потомком
        InitializeGameObject(i);
        return true;
    }
    //Создаем случайный объект
    public boolean CreateObject()
    {

        //В зависимости от числа мы инициализируем объект родительского класса нужным классом потомком
        InitializeGameObject(currentState.GetFigures()[0]);
        //Номер текущей фигуры
        currentState.SetCurrentFigure(currentState.GetFigures()[0]);

        //Сдвигаем массив предсказанных фигур на 1 элемент "влево"
        for (int i = 0; i <currentState.GetFigures().length-1; i++)
            currentState.GetFigures()[i] = currentState.GetFigures()[i+1];

        //Обновляем последнюю сгенерированную фигуру
        currentState.GetFigures()[currentState.GetFigures().length-1]=GetRandomNumber();
        //После создания нового объекта проверяем не заканчивается ли игра с появлением этого объекта на поле
        return CheckEnd(currentState.GetObject().GetObjectCoord());
    }

    public int GetScore()
{
    return currentState.GetScore();
}
    public void SetScore(int score)
    {
        currentState.SetScore(score);
    }
    //Обнуляет ячейки игрового поля в которых находится текущая фигура
    void ClearObjectOnPlane()
    {
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
        currentState.SetGamePanel(currentState.GetObject().GetObjectCoord()[i][0],currentState.GetObject().GetObjectCoord()[i][1],false);
    }
    //Заполняет ячейки игрового поля в которых находится текущая фигура
    void PlaceObjectOnPlane()
    {
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            currentState.SetGamePanel(currentState.GetObject().GetObjectCoord()[i][0],currentState.GetObject().GetObjectCoord()[i][1],true);
    }

    //Проверяет точки под всеми точками текущей фигуры, если хоть под одной игровое поле заканчивается, возвращает false, иначе true
    boolean BottomCheck()
    {
        int limit=GameHeight-1;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
                if (currentState.GetObject().GetObjectCoord()[i][1]==limit)
                    return false;
        return true;
    }

//Получает координаты точки на игровом поле и массив координат, возвращает true если эта точка входит в координаты, false если нет
    boolean CheckObjectPoints(int w, int h,int coord[][])
    {
        boolean flag=false;
        for(int i=0;i<coord.length;i++)
            if(coord[i][0]==w && coord[i][1]==h)
                flag=true;
        return flag;
    }
// Проверяет точки под всеми точками текущей фигуры, если хоть под одной есть занятое пространство и эта точка не принадлежит самой  фигуре возвращает true, иначе false
    boolean BottomFiguresCheck()
    {
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if(currentState.GetGamePanel(currentState.GetObject().GetObjectCoord()[i][0],currentState.GetObject().GetObjectCoord()[i][1]+1)==true && !CheckObjectPoints(currentState.GetObject().GetObjectCoord()[i][0],currentState.GetObject().GetObjectCoord()[i][1]+1,currentState.GetObject().GetObjectCoord()))
                    return true;
        return false;
    }

    //В тетрисе, когда фигрура движется вертикально вдоль одной из боковых сторон и приходит сигнал на поворот, она поворачивается, при
    //этом отодвигаясь от края игрового поля на столько на сколько нужно чтобы корректно поместиться в игровом поле. Данная функция
    //получает массив координат фигуры и затем, если требуется,  сдвигает фигуру вправо или влево, пока  она не будет корректно вмещаться
    // в поле.
    int [][] BoundaryAlignment(int rot[][])

    {
        int countLeft=0;
        int countRight=0;
        //Считаем насколько фигуры выходит вправо или влево за пределы игрового поля
        for(int i=0;i<rot.length;i++)
        {
            if (rot[i][0] < 0) {
                countLeft++;
            }
            if (rot[i][0] >GameWidth-1) {
                countRight++;
            }
        }
        //Затем сдвигаем её в нужном направлении
        if(countLeft!=0)
        {
            for(int i=0;i<rot.length;i++)
                rot[i][0]+=countLeft;
        }

        if(countRight!=0)
        {
            for(int i=0;i<rot.length;i++)
                rot[i][0]-=countRight;
        }
        return  rot;

    }

    //Проверка конца игры. Получает массив координат. Если по этим координатам на игровом поле хоть одна ячейка занята возвращает false-
    //игра закончена.
    boolean CheckEnd(int rot[][])
    {
        try {
            for (int i = 0; i < rot.length; i++)
                if (currentState.GetGamePanel(rot[i][0],rot[i][1]) == true)
                    return false;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    //Проверяем возможность поворота фигуры. Получаем координаты повернутого объекта. Затем проверяем эти координаты в нашем игровом поле
    //если хоть одни из них заняты и это не координаты объекта который мы хотим повернуть значит повернуть фигуру невозможно и мы возвращаем
    //"false" иначе "true". Также мы проверяем не выходят ли точки повернутого объекта за пределы игрового поля снизу
   boolean CheckPosibility(int rot[][])
    {
        try {
            for (int i = 0; i < rot.length; i++)
                if (rot[i][1]>=GameHeight || (currentState.GetGamePanel(rot[i][0],rot[i][1]) == true && !CheckObjectPoints(rot[i][0],rot[i][1],currentState.GetObject().GetObjectCoord())) )
                {
                    return false;
                }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    //Функция поворота фигуры
    public void RotateObject()
    {
        try {
            //Поворачиваем текущий объект, и выравниваем его по краю если при повороте он вышел за пределы игрового поля
            int rot[][]= BoundaryAlignment(currentState.GetObject().IfRotation());
            //Проверяем можно ли поместить повернутый лбъект на игровое поле без пересечений с другими объектами
            if(CheckPosibility(rot))
            {
                //Если поворот возможен то заменяем основную фигуру повернутои и перерисовываем
                ClearObjectOnPlane();
                for (int i = 0; i < currentState.GetObject().GetObjectCoord().length; i++)
                    for (int j = 0; j < currentState.GetObject().GetObjectCoord()[0].length; j++)
                        currentState.GetObject().GetObjectCoord()[i][j] = rot[i][j];
                PlaceObjectOnPlane();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//Удаление указанной линии
    void DeleteLine(int numberLine)
    {
        for(int i=numberLine;i>=0;i--)
        {
            for (int j = 0; j < GameWidth; j++)
            {
                if(i!=0)
                    currentState.SetGamePanel(j,i,currentState.GetGamePanel(j,i-1));
                else
                    currentState.SetGamePanel(j,i,false);
            }
        }
    }
//Проверяет Заполнена ли хоть одна линия
void LineFillinCheck()
{
    for(int i=0;i<GameHeight;i++)
    {
        for (int j = 0; j < GameWidth; j++) {
            if (currentState.GetGamePanel(j,i) == false)
            {
                break;
            }else
            {
                if(j==GameWidth-1) {
                    DeleteLine(i);
                    currentState.IncrementScore();
                }
            }

        }
    }
}

//Вызывается в конце движения фигуры
 public void CheckAtEndMove()
 {
     //Проверяем на заполненные линии
     LineFillinCheck();
     //Создаем новый объект и если для него уже нет места заканчиваем игру
     if(!CreateObject())
         gameover=true;
 }

 //Движение вниз
    public void MoveDown()
    {
            //Обнуляем координаты текущего объекта в нашем игровом поле
            ClearObjectOnPlane();
        //Если внизу еще не дно
            if(BottomCheck())
            {
                //Если внизу другая фигура
                if(BottomFiguresCheck())
                {
                    //Прорисовываем текущий объект в игровом поле
                    PlaceObjectOnPlane();
                    //Конец хода
                    CheckAtEndMove();
                }else
                {
                    //Двигаем фигуру на шаг вниз
                    currentState.GetObject().Down();
                    //Прорисовываем текущий объект в игровом поле
                    PlaceObjectOnPlane();
                }
            }
            else
            {
                //Прорисовываем текущий объект в игровом поле
                PlaceObjectOnPlane();
                //Конец хода
                CheckAtEndMove();
            }

    }

    //Проверяет точки слева от  всех точек текущей фигуры, если слева хоть одной есть край карты поля возвращает false, иначе true
    boolean LeftCheck()
    {
        int limit=0;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if (currentState.GetObject().GetObjectCoord()[i][0]==limit)
                return false;
        return true;
    }

    //Проверяет точки слева от  всех точек текущей фигуры, если слева хоть одной есть другие фигуры и это не точки текущей фигуры
    // возвращает false, иначе true
    boolean LeftFiguresCheck()
    {
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if(currentState.GetGamePanel(currentState.GetObject().GetObjectCoord()[i][0]-1,currentState.GetObject().GetObjectCoord()[i][1])==true && !CheckObjectPoints(currentState.GetObject().GetObjectCoord()[i][0]-1,currentState.GetObject().GetObjectCoord()[i][1],currentState.GetObject().GetObjectCoord()))
                return true;
        return false;
    }

    public void MoveLeft()
{
    //Обнуляем координаты текущего объекта в нашем игровом поле
    ClearObjectOnPlane();
    if(LeftCheck())
        if(!LeftFiguresCheck())
            currentState.GetObject().Left();
    PlaceObjectOnPlane();

}


    //Проверяет точки справа от  всех точек текущей фигуры, если справа хоть одной есть край карты поля возвращает true, иначе false
    boolean RightCheck()
    {
        int limit=GameWidth-1;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if (currentState.GetObject().GetObjectCoord()[i][0]==limit)
                return false;
        return true;
    }

    //Проверяет точки справа от  всех точек текущей фигуры, если справа хоть одной есть другие фигуры и это не точки текущей фигуры
    // возвращает true, иначе false
    boolean RightFiguresCheck()
    {
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if(currentState.GetGamePanel(currentState.GetObject().GetObjectCoord()[i][0]+1,currentState.GetObject().GetObjectCoord()[i][1])==true && !CheckObjectPoints(currentState.GetObject().GetObjectCoord()[i][0]+1,currentState.GetObject().GetObjectCoord()[i][1],currentState.GetObject().GetObjectCoord()))
                return true;
        return false;
    }
    public void MoveRight()
    {
        ClearObjectOnPlane();
        if(RightCheck())
            if(!RightFiguresCheck())
                currentState.GetObject().Right();
        PlaceObjectOnPlane();
    }
}
