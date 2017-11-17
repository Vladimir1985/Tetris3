package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.logic.LogicConst;
import com.example.vladimir.struct.CurrentState;


public final class MoveRightControl extends DoMove{

    //Проверяет точки справа от  всех точек текущей фигуры, если справа хоть одной есть край карты поля возвращает true, иначе false
    boolean RightCheck()
    {
        int limit= LogicConst.WIDTH_GAMEFIELD-1;
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

    public CurrentState Do(CurrentState state){
        currentState=state;
        setStateCurrentObjectOnPlane(false);
        if(RightCheck())
            if(!RightFiguresCheck())
                currentState.GetObject().Right();
        setStateCurrentObjectOnPlane(true);
        return  currentState;
    }
}
