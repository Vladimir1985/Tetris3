package com.example.vladimir.logic.IDoInterfacesImplementation;
import com.example.vladimir.struct.CurrentState;

public abstract class DoMove implements IDo {

    CurrentState currentState=new CurrentState();
    //Обнуляет ячейки игрового поля в которых находится текущая фигура
    //Или заполняет ячейки игрового поля в которых находится текущая фигура
    //используется для затирания старой позиции фигуры, перед тем как прорисовать её на новой
    //или для прорисовки фигуры
    void setStateCurrentObjectOnPlane(boolean b)
    {
        int x,y;
        for(int i=0;i<currentState.GetObject().GetObjectCoord().length;i++){
            //Перебираем все ячейки игрового поля которые покрывает текущая фигура
            //Берем их координаты и устанавливаем в соответствии с входным параметром
            x=currentState.GetObject().GetObjectCoord()[i][0];
            y=currentState.GetObject().GetObjectCoord()[i][1];
            currentState.SetGamePanel(x, y, b);
        }
    }
    //Получает координаты точки на игровом поле и массив координат, возвращает true если эта точка входит в координаты, false если нет
    boolean CheckObjectPoints(int w, int h,int coord[][]){
        boolean flag=false;
        for(int i=0;i<coord.length;i++)
            if(coord[i][0]==w && coord[i][1]==h)
                flag=true;
        return flag;
    }
}
