package com.example.vladimir.logic;

import com.example.vladimir.logic.IDoInterfacesImplementation.ContextDo;
import com.example.vladimir.logic.IDoInterfacesImplementation.CreateObject;
import com.example.vladimir.logic.IDoInterfacesImplementation.GenerateFigure;
import com.example.vladimir.logic.IDoInterfacesImplementation.IDo;
import com.example.vladimir.logic.IDoInterfacesImplementation.MoveDownControl;
import com.example.vladimir.logic.IDoInterfacesImplementation.MoveLeftControl;
import com.example.vladimir.logic.IDoInterfacesImplementation.MoveRightControl;
import com.example.vladimir.logic.IDoInterfacesImplementation.RotateFigure;
import com.example.vladimir.logic.IDoInterfacesImplementation.ScoreControl;
import com.example.vladimir.struct.*;

public class LogicMachine
{
    //Размеры нашего игрового поля в игровых "кубиках"
    int GameWidth ;
    int GameHeight;

    //Класс хранящий данные о текущем состоянии игры
    CurrentState currentState;

    //Флаг конца игры
    boolean gameover=false;

    com.example.vladimir.logic.IDoInterfacesImplementation.ContextDo ContextDo=new ContextDo();

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
    public void SetCurrentStateAfterLoad(CurrentState currentState){
        this.currentState=currentState;
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
    public int GetScore()
    {
        return currentState.GetScore();
    }

    public  LogicMachine()
    {
        //Размеры игрового поля берем из класса текущего состояния
        currentState=new CurrentState();
        GameWidth = LogicConst.WIDTH_GAMEFIELD;
        GameHeight = LogicConst.HEIGHT_GAMEFIELD;

        //Генерируем предсказание на первые 3 фигуры
        DoLogic(new GenerateFigure());
    }

    void DoLogic(IDo Ido){
        ContextDo.setStrategy(Ido);
        currentState=ContextDo.DoStrategy(currentState);
    }
    //Создаем случайный объект
    public boolean CreateObject()
    {
        DoLogic(new CreateObject());
        return CheckEnd(currentState.GetObject().GetObjectCoord());
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

    //Функция поворота фигуры
    public void RotateObject()
    {
        ContextDo.setStrategy(new RotateFigure());
        currentState=ContextDo.DoStrategy(currentState);
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
                    ContextDo.setStrategy(new ScoreControl());
                    currentState=ContextDo.DoStrategy(currentState);
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
    public void MoveDown(){
        ContextDo.setStrategy(new MoveDownControl());
        currentState=ContextDo.DoStrategy(currentState);
        if(currentState.GetEndOfTurn())
            CheckAtEndMove();

    }

    public void MoveLeft(){
    ContextDo.setStrategy(new MoveLeftControl());
    currentState=ContextDo.DoStrategy(currentState);
}

    public void MoveRight(){
        ContextDo.setStrategy(new MoveRightControl());
        currentState=ContextDo.DoStrategy(currentState);
    }
}
