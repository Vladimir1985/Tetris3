package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.logic.LogicConst;
import com.example.vladimir.struct.CurrentState;



public final class MoveLeftControl extends DoMove{

    //Проверяет точки слева от  всех точек текущей фигуры, если слева хоть одной есть край карты поля возвращает false, иначе true
    boolean LeftCheck(){
        int limit= LogicConst.STARTING_POINT;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++)
            if (currentState.GetObject().GetObjectCoord()[i][0]==limit)
                return false;
        return true;
    }

    //Проверяет точки слева от  всех точек текущей фигуры, если слева хоть одной есть другие фигуры и это не точки текущей фигуры
    // возвращает false, иначе true
    boolean LeftFiguresCheck(){
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++){
            x=currentState.GetObject().GetObjectCoord()[i][0] - 1;
            y=currentState.GetObject().GetObjectCoord()[i][1];
            if (currentState.GetGamePanel(x, y) == true && !CheckObjectPoints(x, y, currentState.GetObject().GetObjectCoord()))
                return true;
        }
        return false;
    }

    public CurrentState Do(CurrentState state){
        currentState=state;
        //Обнуляем координаты текущего объекта в нашем игровом поле
        setStateCurrentObjectOnPlane(false);
        if(LeftCheck())
            if(!LeftFiguresCheck())
                currentState.GetObject().Left();
        setStateCurrentObjectOnPlane(true);
        return  state;
    }
}
