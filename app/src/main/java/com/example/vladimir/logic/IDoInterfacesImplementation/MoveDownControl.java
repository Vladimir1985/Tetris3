package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.logic.LogicConst;
import com.example.vladimir.struct.CurrentState;


public final class MoveDownControl extends DoMove {

    public  void MoveControl(){

    }
    //Проверяет точки под всеми точками текущей фигуры, если хоть под одной игровое поле заканчивается, возвращает false, иначе true
    boolean BottomCheck()
    {
        int limit= LogicConst.HEIGHT_GAMEFIELD-1;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if (currentState.GetObject().GetObjectCoord()[i][1]==limit)
                return false;
        return true;
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

    //Движение вниз
    public CurrentState Do(CurrentState state)
    {
        currentState=state;
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
                currentState.SetEndOfTurn(true);
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
            currentState.SetEndOfTurn(true);
        }
        return currentState;
    }
}
