package com.example.vladimir.logic;
import android.util.Log;

import com.example.vladimir.struct.*;

import java.util.Random;



public class LogicMachine
{
    //Размеры нашего игрового поля в игровых "кубиках"
    int GameWidth ;
    int  GameHeight;

    final int CUBE=0;
    final int STRAIGHT=1;
    final int T_OBJECT=2;
    final int R_ANGLE=3;
    final int L_ANGLE=4;
    final int LZ=5;
    final int RZ=6;

    //Класс хранящий данные о текущем состоянии игры
    CurrentState currentState;

    //Флаг конца игры
    boolean gameover=false;

    public  boolean getGameOverState()
    {
        return  gameover;
    }
     public int GetLevel()
     {
         return  currentState.GetLevel();
     }

     public CurrentState GetCurrentState()
     {
         return  currentState;
     }

    public void SetCurrentStateAfterLoad(CurrentState currentState)
    {
        this.currentState=currentState;
    }

    public  LogicMachine()
    {
        //Размеры игрового поля берем из класса текущего состояния
        currentState=new CurrentState();
        GameWidth = currentState.getWidth();
        GameHeight = currentState.getHeight();

        //Генерируем предсказание на первые 3 фигуры
        SetStartFigures();
    }


    //Возвращаем игровое поле
    public boolean[][] GetBattlefield()
    {
        return currentState.GetPanel();
    }

    //Возвращаем массив с предсказанными фигурами
    public int[] GetFigures()
    {
        return currentState.GetFigures();
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
            case CUBE:currentObject=new CubeObject();currentState.SetObject(currentObject);break;
            case STRAIGHT:currentObject=new StraightObject();currentState.SetObject(currentObject);break;
            case T_OBJECT:currentObject=new TObject();currentState.SetObject(currentObject);break;
            case R_ANGLE:currentObject=new RAngleObject();currentState.SetObject(currentObject);break;
            case L_ANGLE:currentObject=new LAngleObject();currentState.SetObject(currentObject);break;
            case LZ:currentObject=new LZObject();currentState.SetObject(currentObject);break;
            case RZ:currentObject=new RZObject();currentState.SetObject(currentObject);break;
            default:break;
        }
    }

    //Создаем случайный объект
    public boolean CreateObject()
    {
        //В зависимости от числа мы инициализируем объект родительского класса нужным классом потомком
        InitializeGameObject(currentState.GetFigures()[0]);
        //Номер текущей фигуры
        currentState.SetCurrentFigure(currentState.GetFigures()[0]);

        //Сдвигаем массив предсказанных фигур на 1 элемент
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
    //Обнуляет ячейки игрового поля в которых находится текущая фигура
    //Или заполняет ячейки игрового поля в которых находится текущая фигура
    //используется для затирания старой позиции фигуры, перед тем как прорисовать её на новой
    //или для прорисовки фигуры
    void setStateCurrentObjectOnPlane(boolean b)
    {
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
        {
            //Перебираем все ячейки игрового поля которые покрывает текущая фигура
            //Берем их координаты и устанавливаем в соответствии с входным параметром
            x=currentState.GetObject().GetObjectCoord()[i][0];
            y=currentState.GetObject().GetObjectCoord()[i][1];
            currentState.SetGamePanel(x, y, b);
        }
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
// Проверяет точки под всеми точками текущей фигуры, если хоть под одной есть занятое пространство и эта точка не принадлежит самой
// фигуре возвращает true, иначе false
    boolean BottomFiguresCheck()
    {
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
        {
            x=currentState.GetObject().GetObjectCoord()[i][0];
            y=currentState.GetObject().GetObjectCoord()[i][1] + 1;
            if (currentState.GetGamePanel(x, y) == true && !CheckObjectPoints(x, y, currentState.GetObject().GetObjectCoord()))
                return true;
        }
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
        for (int i = 0; i < rot.length; i++)
                if (currentState.GetGamePanel(rot[i][0],rot[i][1]) == true)
                    return false;

        return true;
    }

    //Проверяем возможность поворота фигуры. Получаем координаты повернутого объекта. Затем проверяем эти координаты в нашем игровом поле
    //если хоть одни из них заняты и это не координаты объекта который мы хотим повернуть значит повернуть фигуру невозможно и мы возвращаем
    //"false" иначе "true". Также мы проверяем не выходят ли точки повернутого объекта за пределы игрового поля снизу
   boolean CheckPosibility(int rot[][])
    {
        boolean cell_employment_flag, cell_belong_flag;
        //Перебираем все ячейки
        for (int i = 0; i < rot.length; i++)
        {
            //Принадлежит ли ячейка нашему игровому полю
            cell_belong_flag=CheckObjectPoints(rot[i][0], rot[i][1], currentState.GetObject().GetObjectCoord());
            //Активна или нет ячейка с этими координатами
            cell_employment_flag=currentState.GetGamePanel(rot[i][0], rot[i][1]);
            if (rot[i][1] >= GameHeight || (cell_employment_flag && !cell_belong_flag))
                return false;
        }
        return true;
    }

    //Функция поворота фигуры
    public void RotateObject()
    {
        //Поворачиваем текущий объект, и выравниваем его по краю если при повороте он вышел за пределы игрового поля
        int rot[][]= BoundaryAlignment(currentState.GetObject().IfRotation());
        //Проверяем можно ли поместить повернутый объект на игровое поле без пересечений с другими объектами
        if(CheckPosibility(rot))
        {
            //Если поворот возможен то заменяем основную фигуру повернутой и перерисовываем
            setStateCurrentObjectOnPlane(false);
            for (int i = 0; i < currentState.GetObject().GetObjectCoord().length; i++)
                for (int j = 0; j < currentState.GetObject().GetObjectCoord()[0].length; j++)
                    currentState.GetObject().GetObjectCoord()[i][j] = rot[i][j];
            setStateCurrentObjectOnPlane(true);

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
        for (int j = 0; j < GameWidth; j++)
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
            setStateCurrentObjectOnPlane(false);
        //Если внизу еще не дно
            if(BottomCheck())
            {
                //Если внизу другая фигура
                if(BottomFiguresCheck())
                {
                    //Прорисовываем текущий объект в игровом поле
                    setStateCurrentObjectOnPlane(true);
                    //Конец хода
                    CheckAtEndMove();
                }else
                {
                    //Двигаем фигуру на шаг вниз
                    currentState.GetObject().Down();
                    //Прорисовываем текущий объект в игровом поле
                    setStateCurrentObjectOnPlane(true);
                }
            }
            else
            {
                //Прорисовываем текущий объект в игровом поле
                setStateCurrentObjectOnPlane(true);
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
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
        {
            x=currentState.GetObject().GetObjectCoord()[i][0] - 1;
            y=currentState.GetObject().GetObjectCoord()[i][1];
            if (currentState.GetGamePanel(x, y) == true && !CheckObjectPoints(x, y, currentState.GetObject().GetObjectCoord()))
                return true;
        }
        return false;
    }

    public void MoveLeft()
{
    //Обнуляем координаты текущего объекта в нашем игровом поле
    setStateCurrentObjectOnPlane(false);
    if(LeftCheck())
        if(!LeftFiguresCheck())
            currentState.GetObject().Left();
    setStateCurrentObjectOnPlane(true);

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
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
        {
            x=currentState.GetObject().GetObjectCoord()[i][0]+1;
            y=currentState.GetObject().GetObjectCoord()[i][1];
            if (currentState.GetGamePanel(x, y) == true && !CheckObjectPoints(x, y, currentState.GetObject().GetObjectCoord()))
                return true;
        }
        return false;
    }
    public void MoveRight()
    {
        setStateCurrentObjectOnPlane(false);
        if(RightCheck())
            if(!RightFiguresCheck())
                currentState.GetObject().Right();
        setStateCurrentObjectOnPlane(true);
    }
}
